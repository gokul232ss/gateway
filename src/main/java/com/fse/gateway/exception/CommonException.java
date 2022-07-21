package com.fse.gateway.exception;

import com.fse.gateway.response.ErrResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonException extends Exception {

    ErrResponse response;

    public CommonException(ErrResponse response) {
        this.response = response;
    }
}
