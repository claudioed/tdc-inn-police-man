package tech.claudioed.police.man.verticles;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import tech.claudioed.police.man.data.BlockUserData;
import tech.claudioed.police.man.data.BlockUserIntent;
import tech.claudioed.police.man.infra.UserServiceConfig;

public class BlockUser extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(this.getClass());

  private UserServiceConfig userServiceConfig;

  @Override
  public void start() {
    WebClient client = WebClient.create(this.vertx);

    initConfig().compose(this::userServiceConfig).onSuccess(userServiceConfig -> {
      this.vertx.eventBus().consumer("request.block.user",handler ->{
        var blockUserIntent = Json.decodeValue(handler.body().toString(), BlockUserIntent.class);
        var rawUrl = String.format("http://%s:%s/users", userServiceConfig.getHost(), userServiceConfig.getPort());
        var url = String.format("%s/%s",rawUrl,blockUserIntent.getUserId());
        var blockUserData = new BlockUserData(true);
        client.patchAbs(url).sendJson(blockUserData.toJson()).onSuccess(userData -> {
          if (userData.statusCode() != 200){
            LOG.info("User was blocked ID: " + blockUserIntent.getUserId());
            handler.reply(Json.encode(userData));
          }else{
            LOG.error("User was not blocked ID:" + blockUserIntent.getUserId());
            handler.fail(2002,"unable to block user on user service");
          }
        }).onFailure(err -> {
          LOG.error("Fail to connect on user service. User was not blocked ID:" + blockUserIntent.getUserId(),err);
          handler.fail(2001,"unable to connect with user service");
        });
      });
    }).onFailure(err -> {
      LOG.error("Error on configure verticle",err);
    });
  }

  public Future<UserServiceConfig> userServiceConfig(JsonObject cfg){
    this.userServiceConfig = new UserServiceConfig(cfg.getJsonObject("user-service", new JsonObject()));
    return Future.succeededFuture(this.userServiceConfig);
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
