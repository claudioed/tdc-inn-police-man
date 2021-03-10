package tech.claudioed.police.man.data;

import io.vertx.core.json.JsonObject;

public class BlockUserData {

  private Boolean blocked;

  BlockUserData(){}

  public BlockUserData(Boolean blocked) {
    this.blocked = blocked;
  }

  public Boolean getBlocked() {
    return blocked;
  }

  public void setBlocked(Boolean blocked) {
    this.blocked = blocked;
  }

  public JsonObject toJson(){
    var json = new JsonObject();
    json.put("blocked",this.blocked);
    return json;
  }

}
