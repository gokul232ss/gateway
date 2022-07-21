package com.fse.gateway.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ErrResponse {

    private String errCode;
    private String message;
    private long timestamp;

    /**
     * parameterised constructor
     *
     * @param errCode
     * @param message
     */
    public ErrResponse(String errCode, String message) {
        this.errCode = errCode;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}