package com.kindredgroup.unibetlivetest.types;

import lombok.Getter;

public enum NonStandardExceptionType {
    INSUFICIENT_FUNDS(600),
    ODDS_CHANGED(601),
    SELECTION_CLOSED(602);

    @Getter
    final int code;

    NonStandardExceptionType(int code) {
        this.code = code;
    }
}
