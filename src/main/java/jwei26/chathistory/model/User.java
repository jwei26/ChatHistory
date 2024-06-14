package jwei26.chathistory.model;


import jakarta.persistence.*;

@Entity
@Table(name = "users", schema = "public") // 根据实际schema名称调整
public class User {
    @Id
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}