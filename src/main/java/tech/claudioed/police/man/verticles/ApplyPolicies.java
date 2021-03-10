package tech.claudioed.police.man.verticles;

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
import io.vertx.redis.client.*;
import tech.claudioed.police.man.data.MessageContent;
import tech.claudioed.police.man.data.PolicyViolationData;
import tech.claudioed.police.man.infra.RedisConfig;

public class ApplyPolicies extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  private RedisConfig redisConfig;

  @Override
  public void start(Promise<Void> startPromise) {
    initConfig().compose(this::redis).compose(connection -> {
      var redisAPI = RedisAPI.api(connection);
      vertx.eventBus().consumer("request.apply.policies", message -> {
        var json = new JsonObject(message.body().toString());
        var messageContent = new MessageContent(json);
        redisAPI.smembers("words").onSuccess(response -> {
          LOG.info("Checking words... ");
          if (response.type() == ResponseType.MULTI) {
            for (Response item : response) {
              var word = item.toString();
              if (messageContent.containsWord(word)) {
                LOG.info("Violation was found..");
                vertx.eventBus().send("request.policy.violation", Json.encode(PolicyViolationData.createNew(messageContent.getMessageId(), messageContent.getThreadId(), messageContent.getUserId(), word)));
                break;
              }
            }
          }
        }).onFailure(err -> {
          LOG.error("Error to execute instruction in redis ", err);
        });
      });
      startPromise.complete();
      return Future.succeededFuture();
    });
  }

  public Future<RedisConnection> redis(JsonObject cfg){
    this.redisConfig = new RedisConfig(cfg.getJsonObject("redis", new JsonObject()));
    var client = Redis.createClient(this.vertx, redisConfig.getHost());
    return client.connect();
  }

  public Future<JsonObject> initConfig() {
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", "src/main/resources/config.json"));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    return retriever.getConfig();
  }

}
