package ru.vmsystems.vibrocalc.calc;

import java.util.HashMap;
import java.util.Map;

public class VibroCalcByDisplacement extends VibroCalc {

    @Override
    public Result calculate(Value value, Double freq) {

        if (!(value.getParameter() == Parameter.D_mm
                || value.getParameter() == Parameter.D_m)) {
            throw new RuntimeException("Не правильный параметр");
        }

        Double displacementRms = prepareValue(value);
        displacementRms = translationValue(value, displacementRms);

        Double _2piFreq = calc2PiFreq(freq);

        Map<String, Value> valueMap = new HashMap<>();

        for (Map.Entry<Parameter, EdIzm> parameter : getParameters().entrySet()) {
            Double result = null;
            switch (parameter.getKey()) {
                case A_g:
                    result = displacementRms * _2piFreq * _2piFreq / g;
                    break;
                case A_m_sec2:
                    result = displacementRms * _2piFreq * _2piFreq ;
                    break;
                case A_mm_sec2:
                    result = displacementRms * _2piFreq * _2piFreq * mashtabKoeff;
                    break;
                case V_mm_sec:
                    result = displacementRms * _2piFreq * mashtabKoeff;
                    break;
                case V_m_sec:
                    result = displacementRms * _2piFreq;
                    break;
                case D_m:
                    result = displacementRms;
                    break;
                case D_mm:
                    result = displacementRms * mashtabKoeff;
                    break;
                case A_db:
                    result = 20.0 * Math.log10(displacementRms * _2piFreq * _2piFreq / (g * Math.pow(10, -6)));
                    break;
                case V_db_m_sec:
                    result = 20.0 * Math.log10(displacementRms * _2piFreq / Math.pow(10, -8));
                    break;
                case V_db_mm_sec:
                    result = 20.0 * Math.log10(displacementRms * _2piFreq * mashtabKoeff / Math.pow(10, -6));
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
            case D_m:
                break;
            case D_mm:
                rms /= mashtabKoeff;
                break;
        }

        return rms;
    }
}
