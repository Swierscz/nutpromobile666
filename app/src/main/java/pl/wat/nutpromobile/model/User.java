package pl.wat.nutpromobile.model;

import lombok.Data;

@Data
public class User {
    private String firstName;
    private String lastName;
    private int age;
    private int height;
    private String login;
}
