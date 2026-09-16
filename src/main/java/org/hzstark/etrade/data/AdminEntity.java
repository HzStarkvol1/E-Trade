package org.hzstark.etrade.data;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class AdminEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Jpa Configuration
    public AdminEntity(){}
    public AdminEntity(String username, String password)
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

}
