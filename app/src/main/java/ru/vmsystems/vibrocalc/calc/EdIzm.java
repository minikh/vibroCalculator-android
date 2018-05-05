package ru.vmsystems.vibrocalc.calc;

import java.util.ArrayList;
import java.util.List;

public enum EdIzm {
    RMS(1, "Rms", "СКЗ"),
    PEAK(3, "Pk", "Пик"),
    PEAK_TO_PEAK(4, "Pk-pk", "Размах"),
    NONE(5, "no", "нет");

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

    public static List<String> getEnglishList() {
        List<String> list = new ArrayList<>();
        for (EdIzm edIzm : EdIzm.values()) {
            if (edIzm == NONE) continue;
            list.add(edIzm.english);
        }
        return list;
    }

    public static List<String> getMetricList() {
        List<String> list = new ArrayList<>();
        for (EdIzm edIzm : EdIzm.values()) {
            if (edIzm == NONE) continue;
            list.add(edIzm.metric);
        }
        return list;
    }
}
