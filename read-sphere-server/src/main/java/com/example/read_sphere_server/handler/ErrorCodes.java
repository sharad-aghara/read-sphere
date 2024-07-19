package com.example.read_sphere_server.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public enum ErrorCodes {
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No Code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current Password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "New Password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User Account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User Account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Email/Password is incorrect!"),
    ;


    @Getter
    private int code;
    @Getter
    private String description;
    @Getter
    private HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
