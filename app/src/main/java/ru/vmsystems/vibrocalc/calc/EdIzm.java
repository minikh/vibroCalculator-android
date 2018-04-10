package ru.vmsystems.vibrocalc.calc;

import java.util.HashMap;
import java.util.Map;

public enum EdIzm {
    RMS(1, "rms", "СКЗ"),
    PEAK(3, "pk", "Пик"),
    PEAK_TO_PEAK(4, "pk-pk", "Размах");

    private Integer position;
    private String english;
    private String metric;

    EdIzm(Integer position, String english, String metric) {
        this.position = position;
        this.english = english;
        this.metric = metric;
    }

    public Integer getPosition() {
        return position;
    }

    public static EdIzm getEdIzm(String str) {
        for (EdIzm edIzm : EdIzm.values()) {
            if (edIzm.english.equals(str)) return edIzm;
        }

        for (EdIzm edIzm : EdIzm.values()) {
            if (edIzm.metric.equals(str)) return edIzm;
        }

        throw new RuntimeException("Нет такой единицы измерения: " + str);
    }

    public static Map<Integer, String> getEnglish() {
        Map<Integer, String> map = new HashMap<>();
        for (EdIzm edIzm : EdIzm.values()) {
            map.put(edIzm.position, edIzm.english);
        }
        return map;
    }

    public static Map<Integer, String> getMetric() {
        Map<Integer, String> map = new HashMap<>();
        for (EdIzm edIzm : EdIzm.values()) {
            map.put(edIzm.position, edIzm.metric);
        }
        return map;
    }
}
