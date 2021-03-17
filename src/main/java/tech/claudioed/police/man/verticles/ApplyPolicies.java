package tech.claudioed.police.man.verticles;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentracing.Tracer;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.micrometer.backends.BackendRegistries;
import io.vertx.redis.client.*;
import tech.claudioed.police.man.data.MessageContent;
import tech.claudioed.police.man.data.PolicyViolationData;
import tech.claudioed.police.man.infra.RedisConfig;

public class ApplyPolicies extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  private RedisConfig redisConfig;

  private Counter violations;

  private Tracer tracer;

  @Override
  public void start(Promise<Void> startPromise) {
    MeterRegistry registry = BackendRegistries.getDefaultNow();
    this.violations = Counter
      .builder("policy_violations")
      .description("Chat Policy violations")
      .register(registry);
    initConfig().compose(this::redis).onSuccess(connection ->{
      LOG.info("Starting redis connection...");
      var redisAPI = RedisAPI.api(connection);
      vertx.eventBus().consumer("request.apply.policies", message -> {
        LOG.info("Checking violations...");
        var json = new JsonObject(message.body().toString());
        var messageContent = new MessageContent(json);
        redisAPI.smembers("words").onSuccess(response -> {
          LOG.info("Checking words... ");
          if (response.size() > 0) {
            if (response.type() == ResponseType.MULTI) {
              for (Response item : response) {
                var word = item.toString();
                if (messageContent.containsWord(word)) {
                  this.violations.increment();
                  LOG.info("Violation was found..");
                  vertx.eventBus().send("request.policy.violation", Json.encode(PolicyViolationData.createNew(messageContent.getMessageId(), messageContent.getThreadId(), messageContent.getUserId(), word)));
                  break;
                }
              }
            }
          }else{
            LOG.info("There are no policies registered!!");
          }
        }).onFailure(err -> {
          LOG.error("Error to execute instruction in redis ", err);
        });
      });
      LOG.info("Redis connected successfully!!!");
      startPromise.complete();
    }).onFailure(err ->{
      LOG.error("Fail on redis connection",err);
      startPromise.fail("Fail on redis connection");
    }).mapEmpty();
  }

  public Future<RedisConnection> redis(JsonObject cfg){
    this.redisConfig = new RedisConfig(cfg.getJsonObject("redis", new JsonObject()));
    var client = Redis.createClient(this.vertx, redisConfig.getHost());
    return client.connect();
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
