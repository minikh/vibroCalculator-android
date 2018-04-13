package ru.vmsystems.vibrocalc.calc;

import java.util.HashMap;
import java.util.Map;

public class VibroCalcByAcceleration extends VibroCalc {

    @Override
    public Result calculate(Value value, Double freq) {

        if (!(value.getParameter() == Parameter.A_g
                || value.getParameter() == Parameter.A_db
                || value.getParameter() == Parameter.A_mm_sec2
                || value.getParameter() == Parameter.A_m_sec2)) {
            throw new RuntimeException("Не правильный параметр");
        }

        Double accelerationRms = prepareValue(value);
        accelerationRms = translationValue(value, accelerationRms);

        Double _2piFreq = calc2PiFreq(freq);

        Map<String, Value> valueMap = new HashMap<>();

        for (Map.Entry<Parameter, EdIzm> parameter : getParameters().entrySet()) {
            Double result = null;
            switch (parameter.getKey()) {
                case A_g:
                    result = accelerationRms / g;
                    break;
                case A_m_sec2:
                    result = accelerationRms;
                    break;
                case A_mm_sec2:
                    result = accelerationRms * mashtabKoeff;
                    break;
                case V_mm_sec:
                    result = accelerationRms / _2piFreq * mashtabKoeff;
                    break;
                case V_m_sec:
                    result = accelerationRms / _2piFreq;
                    break;
                case D_m:
                    result = ftToInch * accelerationRms / (_2piFreq) / (_2piFreq);
                    break;
                case D_mm:
                    result = inchToMil * accelerationRms / (_2piFreq / mashtabKoeff) / (_2piFreq);
                    break;
                case A_db:
                    result = 20.0 * Math.log10(accelerationRms / (g * Math.pow(10, -6)));
                    break;
                case V_db_m_sec:
                    result = 20.0 * Math.log10(accelerationRms / _2piFreq / Math.pow(10, -8));
                    break;
                case V_db_mm_sec:
                    result = 20.0 * Math.log10(accelerationRms * mashtabKoeff / _2piFreq / Math.pow(10, -6));
                    break;
            }

            setParameters(null);

            if (result != null) {
                Value.ValueBuilder valueBuilder = Value.builder()
                        .parameter(parameter.getKey());
                Value resultValue = prepareResult(parameter.getValue(), result, valueBuilder);
                valueMap.put(resultValue.getParameter().name(), resultValue);
            }
        }

        return Result.builder().values(valueMap).build();
    }

    private Double translationValue(Value value, Double rms) {
        switch (value.getParameter()) {
            case A_db:
                rms = Math.pow(10, rms / 20.0) * (g * Math.pow(10, -6));
                break;
            case A_g:
                rms *= g;
                break;
            case A_mm_sec2:
                rms /= mashtabKoeff;
                break;
            case A_m_sec2:
                break;
        }

        return rms;
    }
}
