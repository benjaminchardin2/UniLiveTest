package com.kindredgroup.unibetlivetest.types;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionType {

    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    SELECTION_NOT_FOUND(HttpStatus.NOT_FOUND),

    NO_CONTENT(HttpStatus.NO_CONTENT),
    ON_GOING_BET(HttpStatus.CONFLICT);

    @Getter
    final HttpStatus status;

    ExceptionType(HttpStatus status) {
        this.status = status;
    }

}
