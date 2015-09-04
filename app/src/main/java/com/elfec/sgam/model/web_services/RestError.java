package com.elfec.sgam.model.web_services;

import com.google.gson.annotations.SerializedName;

/**
 * Error Rest
 */
public class RestError {
    @SerializedName("errors")
    public String message;
}
