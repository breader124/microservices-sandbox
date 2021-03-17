package com.breader.cubetransformationstats.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String action;
    private String reason;
}
