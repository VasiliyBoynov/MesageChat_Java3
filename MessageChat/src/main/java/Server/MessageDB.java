package Server;

public class MessageDB {
    private String messageForClient;
    private boolean rez;

    public MessageDB(String messageForClient, Boolean rez) {
        this.messageForClient = messageForClient;
        this.rez = rez;
    }

    public String getMessageForClient() {
        return messageForClient;
    }

    public Boolean getRez() {
        return rez;
    }
}
