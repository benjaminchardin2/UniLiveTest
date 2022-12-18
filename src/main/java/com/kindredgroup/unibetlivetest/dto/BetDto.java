package com.kindredgroup.unibetlivetest.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kindredgroup.unibetlivetest.jackson.MoneyDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
public class BetDto {
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private Long selectionId;
    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonAlias("cote")
    private BigDecimal odd;
    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonAlias("mise")
    private BigDecimal amount;
}
