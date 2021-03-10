package tech.claudioed.police.man.data;

public class PolicyViolationData {

  private String messageId;

  private String threadId;

  private String userId;

  private String word;

  PolicyViolationData() {
  }

  private PolicyViolationData(String messageId, String threadId, String userId, String word) {
    this.messageId = messageId;
    this.threadId = threadId;
    this.userId = userId;
    this.word = word;
  }

  public static PolicyViolationData createNew(String messageId, String threadId, String userId, String word) {
    return new PolicyViolationData(messageId, threadId, userId, word);
  }

  public PolicyViolation toPolicyViolation(){
    return PolicyViolation.createNew(this.messageId,this.threadId,this.userId,this.word);
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

}
