package tech.claudioed.police.man.data;

public class BlockUserIntent {

  private String userId;

  BlockUserIntent(){}

  public BlockUserIntent(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

}
