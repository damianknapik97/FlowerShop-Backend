package com.dknapik.flowershop.utils;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ToString
@Log4j2
public final class PasswordUtils {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Checks two provided strings in context of matching each other. If they don't match,
     * a newly encrypted newPassword is returned, if they match, the old password is returned.
     */
    public String replacePassword(String oldPassword, String newPassword) {
        log.traceEntry();

        if (!oldPassword.contentEquals(newPassword) && !passwordEncoder.matches(oldPassword, newPassword)) {
            log.info("Passwords doesn't match each other, encoding and updating new one");
            return passwordEncoder.encode(newPassword);
        }

        log.info("Password does match, no changes needed");
        log.traceExit();
        return oldPassword;
    }
}
