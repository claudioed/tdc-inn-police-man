package tech.claudioed.police.man.verticles;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import tech.claudioed.police.man.data.BlockUserIntent;
import tech.claudioed.police.man.data.PolicyViolation;
import tech.claudioed.police.man.data.PolicyViolationData;
import tech.claudioed.police.man.data.PolicyViolationParametersMapper;
import tech.claudioed.police.man.infra.DatasourceConfig;

public class RegistryPolicyViolation extends AbstractVerticle {

  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  private DatasourceConfig datasourceConfig;

  private final DeliveryOptions deliveryOptions = new DeliveryOptions().setTracingPolicy(TracingPolicy.ALWAYS);

  @Override
  public void start(Promise<Void> startPromise) {
    var start = initConfig().compose(this::updateDB).onSuccess(handler -> pool().compose(pgPool -> {
      SqlTemplate<PolicyViolation, SqlResult<Void>> insertTemplate = SqlTemplate
        .forUpdate(pgPool,
          "INSERT INTO policy_violation (id, message_id, thread_id, user_id, word)  VALUES ( #{id}, #{message_id}, #{thread_id}, #{user_id}, #{word} )")
        .mapFrom(
          PolicyViolationParametersMapper.INSTANCE);
      this.vertx.eventBus().consumer("request.policy.violation", message -> {
        var policyViolation = Json.decodeValue(message.body().toString(), PolicyViolationData.class);
        LOG.info("Registering Policy Violation MESSAGE ID: " + policyViolation.getMessageId());
        insertTemplate.execute(policyViolation.toPolicyViolation()).onSuccess(result -> {
          if (result.rowCount() > 0) {
            var blockUserIntent = new BlockUserIntent(policyViolation.getUserId());
            LOG.info("Policy Violation registered MESSAGE ID: " + policyViolation.getMessageId());
            this.vertx.eventBus().send("request.block.user", Json.encode(blockUserIntent),this.deliveryOptions);
          } else {
            LOG.error("Policy Violation was not created MESSAGE ID:" + policyViolation.getMessageId());
          }
        }).onFailure(err -> {
          LOG.error("Error to execute instruction in database ", err);
        });
      });
      return Future.succeededFuture();
    }));
    start.onComplete(startPromise);
  }

  public Future<PgPool> pool() {
    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);
    PgPool client = PgPool.pool(this.vertx, this.datasourceConfig.toPgConnectOptions(), poolOptions);
    return Future.succeededFuture(client);
  }

  public Future<Void> updateDB(JsonObject cfg) {
    return Future.future(ft -> {
      this.datasourceConfig = new DatasourceConfig(cfg.getJsonObject("database", new JsonObject()));
      Configuration config = new FluentConfiguration()
        .dataSource(datasourceConfig.jdbcUrl(), datasourceConfig.getUser(), datasourceConfig.getPassword());
      Flyway flyway = new Flyway(config);
      flyway.migrate();
      ft.complete();
    });
  }

  public Future<JsonObject> initConfig(){
    var configPath = System.getenv("VERTX_CONFIG_PATH");
    LOG.info("Config Path: " + configPath);
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", configPath));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    return retriever.getConfig();
  }

}
