package org.example.mybooklibrary.user;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Register{
    private String username;
    private String email;
    private String password;

    public CharSequence Password() {
        return password;
    }
}