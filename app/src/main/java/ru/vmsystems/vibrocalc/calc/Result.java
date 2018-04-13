package ru.vmsystems.vibrocalc.calc;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
public class Result {
    private Map<String, Value> values;
}
