package com.phuc.tutoring_center.core.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BusinessException extends RuntimeException{
    private String message;
    private int statusCode;
}
