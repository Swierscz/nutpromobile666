package pl.wat.nutpromobile.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private int age;
    private int height;
    private String login;
}
