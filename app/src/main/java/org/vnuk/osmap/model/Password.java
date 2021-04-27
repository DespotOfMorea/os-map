package org.vnuk.osmap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Password {
    private static final int MIN_PASSWORD_LENGTH = 8;

    private String password;

    // This is just basic implementation of password validation.
    // It could be written in way that it returns specific error(s)/problem(s) with password.
    public boolean isPasswordValid() {
        return password.length() >= MIN_PASSWORD_LENGTH
                && Character.isUpperCase(password.charAt(0))
                && Character.isUpperCase(password.charAt(password.length() - 1));
    }
}
