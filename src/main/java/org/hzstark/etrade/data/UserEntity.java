package org.hzstark.etrade.data;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Jpa configuration
    public UserEntity(){}
    public UserEntity(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    private String username;
    private String password;

    //Getters&Setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
