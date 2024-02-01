package com.daesoo.terracotta.common.dto;

public enum ErrorType {
    NONE("",""),
    EXCEPTION("E001","Exception error"),
    RUNTIME_EXCEPTION("E002","Runtime Exception error"),
    ENTITY_NOT_FOUND_EXCEPTION("E003","entity not found error"),
    VALIDATION_EXCEPTION("E004","validation fail error"),
    JWT_EXCEPTION("E005","token invalid error"),
    BAD_CREDENTIALS_EXCEPTION("E006","user's password is wrong"),
    ILLEGAL_ARGUMENT_EXCEPTION("E007","wrong argument error"),
    ACCESS_DENIED_EXCEPTION("E008","don't have authority to do so"),
    AUTHENTICATION_EXCEPTION("E009","authentication has failed"),
    DUPLICATION_EXCEPTION("E010", "duplication error"),
    UNATHORIZED_EXCEPTION("E011", "authentication required"),;

	String errorCode;
    String description;

    ErrorType(String code, String description) {
    	this.errorCode = code;
        this.description = description;
    }

    public String getMessage() {
        return this.description;
    }
    
    public String getCode() {
        return this.errorCode;
    }
}