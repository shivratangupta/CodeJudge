package com.codejudge.onlinejudge.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseDto<T> {

    private T data;

    private HttpStatus status;

    public ResponseDto(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
    }
}
