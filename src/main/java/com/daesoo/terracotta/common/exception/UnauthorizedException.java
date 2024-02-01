package com.daesoo.terracotta.common.exception;

public class UnauthorizedException extends RuntimeException {


	public UnauthorizedException(String message) {
        super(message);
    }
	
	private static final long serialVersionUID = 8149665081818783750L;
}
