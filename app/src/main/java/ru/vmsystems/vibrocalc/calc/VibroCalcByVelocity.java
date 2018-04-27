package ru.vmsystems.vibrocalc.calc;

import java.util.HashMap;
import java.util.Map;

public class VibroCalcByVelocity extends VibroCalc {

    @Override
    public Result calculate(Value value, Double freq) {

        if (!(value.getParameter() == Parameter.V_db_m_sec
                || value.getParameter() == Parameter.V_db_mm_sec
                || value.getParameter() == Parameter.V_m_sec
                || value.getParameter() == Parameter.V_mm_sec)) {
            throw new RuntimeException("Не правильный параметр");
        }

        Double velocityRms = prepareValue(value);
        velocityRms = translationValue(value, velocityRms);

        Double _2piFreq = calc2PiFreq(freq);

        Map<String, Value> valueMap = new HashMap<>();

        for (Map.Entry<Parameter, EdIzm> parameter : getParameters().entrySet()) {
            Double result = null;
            switch (parameter.getKey()) {
                case A_g:
                    result = (velocityRms * _2piFreq) / g / mashtabKoeff;
                    break;
                case A_m_sec2:
                    result = velocityRms * _2piFreq / mashtabKoeff;
                    break;
                case A_mm_sec2:
                    result = velocityRms * _2piFreq;
                    break;
                case V_mm_sec:
                    result = velocityRms;
                    break;
                case V_m_sec:
                    result = velocityRms / mashtabKoeff;
                    break;
                case D_m:
                    result = velocityRms / (_2piFreq * mashtabKoeff);
                    break;
                case D_mm:
                    result = velocityRms / _2piFreq;
                    break;
                case A_db:
                    result = 20.0 * Math.log10(velocityRms * _2piFreq / mashtabKoeff / (g * Math.pow(10, -6)));
                    break;
                case V_db_m_sec:
                    result = 20.0 * Math.log10(velocityRms / mashtabKoeff / Math.pow(10, -8));
                    break;
                case V_db_mm_sec:
                    result = 20.0 * Math.log10(velocityRms / Math.pow(10, -6));
                    break;
            }

            setParameters(null);

            if (result != null) {
                Value resultValue = prepareResult(parameter.getValue(), result, parameter.getKey());
                valueMap.put(resultValue.getParameter().name(), resultValue);
            }
        }

        return new Result(valueMap);
    }

    private Double translationValue(Value value, Double rms) {
        switch (value.getParameter()) {
            case V_db_m_sec:
                rms = Math.pow(10, rms / 20.0) * mashtabKoeff * Math.pow(10, -8);
                break;
            case V_db_mm_sec:
                rms = Math.pow(10, rms / 20.0) * Math.pow(10, -6);
                break;
            case V_m_sec:
                rms *= mashtabKoeff;
                break;
            case V_mm_sec:
                break;
        }

        return rms;
    }
}
