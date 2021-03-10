package tech.claudioed.police.man.data;

import io.vertx.core.json.JsonObject;

public class MessageContent {

  private String threadId;

  private String userId;

  private String messageId;

  private String data;

  public MessageContent(){}

  public MessageContent(String threadId, String userId, String messageId, String data) {
    this.threadId = threadId;
    this.userId = userId;
    this.messageId = messageId;
    this.data = data;
  }

  public MessageContent(JsonObject jsonObject){
    this.data = jsonObject.getString("data");
    this.messageId = jsonObject.getString("messageId");
    this.threadId = jsonObject.getString("threadId");
    this.userId = jsonObject.getString("userId");
  }

  public JsonObject toJson(){
    var jsonObject = new JsonObject();
    jsonObject.put("threadId",this.threadId);
    jsonObject.put("userId",this.userId);
    jsonObject.put("messageId",this.messageId);
    jsonObject.put("data",this.data);
    return jsonObject;
  }

  public boolean containsWord(String word){
    return this.data.contains(word);
  }

  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

}
