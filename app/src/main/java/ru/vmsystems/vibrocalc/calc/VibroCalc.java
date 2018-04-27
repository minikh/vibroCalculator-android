package ru.vmsystems.vibrocalc.calc;

import java.util.Map;

public abstract class VibroCalc {

    private final static Double _2_PI = 2 * Math.PI;
    private final static Double _KILO = 1000.0;
    private final static Double _FT_TO_INCH = 12.0;
    private final static Double _FT_TO_METER = 0.3048;
    private final static Double _ONE = 1.0;
    private final static Double _SQRT_2 = Math.sqrt(2);
    private static final Double _G = 9.80665;

    Double g = 9.80665;
    Double mashtabKoeff = 1000.0;
    Double ftToInch = 1.0;
    Double inchToMil = 1.0;

    private Map<Parameter, EdIzm> parameters;

    public abstract Result calculate(Value value, Double freq);

    public void setParameters(Map<Parameter, EdIzm> parameters) {
        this.parameters = parameters;
    }

    Map<Parameter, EdIzm> getParameters() {
        if (parameters == null) throw new RuntimeException("Не заданы параметры");
        return parameters;
    }

    public void setMeasures(Measures measures) {
        switch (measures){
            case METRIC:
                mashtabKoeff = _KILO;
                ftToInch = _ONE;
                inchToMil = _ONE;
                g = _G;
                break;
            case ENGLISH:
                mashtabKoeff = _FT_TO_INCH;
                ftToInch = _FT_TO_INCH;
                inchToMil = _KILO;
                g = _G / _FT_TO_METER;
                break;
        }
    }

    Double calc2PiFreq(Double freq) {
        return _2_PI * freq;
    }

    Value prepareResult(EdIzm edIzm, Double rmsValue, Parameter parameter) {
        Value result;
        Double val;
        switch (edIzm) {
            case RMS:
            case NONE:
                result = new Value(rmsValue, edIzm, parameter);
                break;
            case PEAK:
                val = rmsValue * _SQRT_2;
                result = new Value(val, edIzm, parameter);
                break;
            case PEAK_TO_PEAK:
                val = rmsValue * _SQRT_2 * 2;
                result = new Value(val, edIzm, parameter);
                break;
            default:
                throw new RuntimeException("Нет такогй единицы измерения " + edIzm.name());

        }
        return result;
    }

    public String recalculateValue(Value value, EdIzm currentEdIzm) {
        if (currentEdIzm == value.getEdIzm()) return String.valueOf(value.getValue());

        Double rms = prepareValue(value);

        Value result = prepareResult(currentEdIzm, rms, value.getParameter());
        return String.valueOf(result.getValue());
    }

    Double prepareValue(Value value) {
        Double rms = 0.0;
        if (value.getEdIzm() == null) return rms;

        switch (value.getEdIzm()) {
            case RMS:
                rms = value.getValue();
                break;
            case PEAK:
                rms = value.getValue() / _SQRT_2;
                break;
            case PEAK_TO_PEAK:
                rms = value.getValue() / 2 / _SQRT_2;
                break;
        }
        return rms;
    }
}
