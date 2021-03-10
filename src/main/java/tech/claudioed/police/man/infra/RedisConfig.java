package tech.claudioed.police.man.infra;

import io.vertx.core.json.JsonObject;

public class RedisConfig {

  private final String host;

  public RedisConfig(JsonObject datasourceConfig) {
    host = datasourceConfig.getString("host", "localhost");
  }

  public String getHost() {
    return host;
  }

}
