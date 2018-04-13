package ru.vmsystems.vibrocalc.calc;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Value {
    private Double value;
    private EdIzm edIzm;
    private Parameter parameter;
}
