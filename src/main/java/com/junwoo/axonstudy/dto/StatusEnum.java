package com.junwoo.axonstudy.dto;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
public enum StatusEnum {
    READY("Ready"),
    ENTER("Enter"),
    EXIT("Exit");

    private String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
