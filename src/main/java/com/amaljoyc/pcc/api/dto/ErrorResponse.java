package com.amaljoyc.pcc.api.dto;

/**
 * Created by achemparathy on 04.12.17.
 */
public class ErrorResponse {

    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
