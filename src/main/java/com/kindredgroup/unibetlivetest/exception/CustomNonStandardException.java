package com.kindredgroup.unibetlivetest.exception;

import com.kindredgroup.unibetlivetest.types.NonStandardExceptionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class CustomNonStandardException extends RuntimeException {

    private final NonStandardExceptionType exception;
    private final String message;

    public CustomNonStandardException(String message, NonStandardExceptionType exception) {
        this.message = message;
        this.exception = exception;
    }

}
