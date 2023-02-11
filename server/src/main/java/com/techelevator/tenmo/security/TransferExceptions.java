package com.techelevator.tenmo.security;

import org.springframework.security.core.AuthenticationException;

public class TransferExceptions extends RuntimeException {

    public TransferExceptions(String msg) {
        super(msg);
    }

}
