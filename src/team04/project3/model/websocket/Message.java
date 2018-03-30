package team04.project3.model.websocket;

public class Message {
    private String sender;
    private String recipient;
    private String content;

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }
}