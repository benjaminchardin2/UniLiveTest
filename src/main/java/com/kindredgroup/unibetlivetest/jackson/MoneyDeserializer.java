package com.kindredgroup.unibetlivetest.jackson;

import java.io.IOException;
import java.math.BigDecimal;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class MoneyDeserializer  extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        BigDecimal bigDecimal = new BigDecimal(parser.getText());
        // If the scale is not two then the amount entered
        // is not a typical money amount
        if (bigDecimal.scale() != 2) {
            throw new IllegalArgumentException(String.format(
                    "Invalid Amount for %s: scale need to be equals to two",
                    parser.getCurrentName()));
        }
        // If the amount field or the odd field is less than 0
        // we need to throw an exception
        if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(String.format(
                    "Invalid Amount for %s: amount need to be positive",
                    parser.getCurrentName()));
        }
        return bigDecimal;
    }
}

