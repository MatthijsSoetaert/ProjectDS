package Models.Objects;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String tag;
    private int index;

    public Message(String message, String tag, int index) {
        this.message = message;
        this.tag = tag;
        this.index = index;
    }

    public Message() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
