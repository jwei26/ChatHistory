package jwei26.chathistory.model;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "messages", schema = "public")
public class Message {
    @Id
    private Long messageId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Timestamp dateTime;
    private String content;
    private String messageUrl;

    // Getters and Setters

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }
}
