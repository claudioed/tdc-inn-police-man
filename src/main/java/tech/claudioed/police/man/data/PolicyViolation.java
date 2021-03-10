package tech.claudioed.police.man.data;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.util.UUID;

@DataObject(generateConverter = true)
@RowMapped(formatter = SnakeCase.class)
@ParametersMapped(formatter = SnakeCase.class)
public class PolicyViolation {

  private String id;

  @Column(name = "message_id")
  private String messageId;

  @Column(name = "thread_id")
  private String threadId;

  @Column(name = "user_id")
  private String userId;

  private String word;

  PolicyViolation(){}

  public PolicyViolation(JsonObject json){
    PolicyViolationConverter.toJson(this,json);
  }

  private PolicyViolation(String id,String messageId, String threadId, String userId, String word) {
    this.id = id;
    this.messageId = messageId;
    this.threadId = threadId;
    this.userId = userId;
    this.word = word;
  }

  public static PolicyViolation createNew(String messageId, String threadId, String userId, String word) {
    return new PolicyViolation(UUID.randomUUID().toString(),messageId, threadId, userId, word);
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
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

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
