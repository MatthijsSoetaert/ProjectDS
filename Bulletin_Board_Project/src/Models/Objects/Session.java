package Models.Objects;

public class Session {
    private String name;
    private String sendSymKey;
    private String sendTag;
    private int sendIndex;
    private String receiveSymKey;
    private String receiveTag;
    private int receiveIndex;
    private String messageBoard;

    //For testing
    private String nextTag;
    private int nextIndex;

    public String getNextTag() {
        return nextTag;
    }

    public void setNextTag(String nextTag) {
        this.nextTag = nextTag;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
    //End testing

    public Session() {
    }

    public Session(String sendSymKey, String sendSymTag, int sendIndex, String name) {
        this.sendSymKey = sendSymKey;
        this.sendTag = sendSymTag;
        this.sendIndex = sendIndex;
        this.name = name;
        this.messageBoard = "";
    }

    public void addReceivedMessagesToBoard(String newMessage){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageBoard);
        stringBuilder.append("\n");
        stringBuilder.append(name);
        stringBuilder.append(": ");
        stringBuilder.append(newMessage);
        messageBoard = stringBuilder.toString();
    }

    public void addSendMessageToBoard(String newMessage, String name){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageBoard);
        stringBuilder.append("\n");
        stringBuilder.append(name);
        stringBuilder.append(": ");
        stringBuilder.append(newMessage);
        messageBoard = stringBuilder.toString();
    }

    //GETTERS AND SETTERS
    public String getSendSymKey() {
        return sendSymKey;
    }

    public void setSendSymKey(String sendSymKey) {
        this.sendSymKey = sendSymKey;
    }

    public String getSendTag() {
        return sendTag;
    }

    public void setSendTag(String sendTag) {
        this.sendTag = sendTag;
    }

    public int getSendIndex() {
        return sendIndex;
    }

    public void setSendIndex(int sendIndex) {
        this.sendIndex = sendIndex;
    }

    public String getReceiveSymKey() {
        return receiveSymKey;
    }

    public void setReceiveSymKey(String receiveSymKey) {
        this.receiveSymKey = receiveSymKey;
    }

    public String getReceiveTag() {
        return receiveTag;
    }

    public void setReceiveTag(String receiveTag) {
        this.receiveTag = receiveTag;
    }

    public int getReceiveIndex() {
        return receiveIndex;
    }

    public void setReceiveIndex(int receiveIndex) {
        this.receiveIndex = receiveIndex;
    }

    public String getMessageBoard()
    {
        return messageBoard;
    }

    public String getName()
    {
        return name;
    }
}
