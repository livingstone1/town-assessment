package ru.hse.townassessment.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum AlgorithmEnum implements Serializable {

    SUM_NORMALIZATION("Взвешенная нормализованная сумма"),
    SUM_WITH_CLUSTERING("Взвешенная сумма с кластеризацией"),
    HELLWIG("Метод Хельвига"),
    HELLWIG_WITH_WEIGHT("Метод Хельвига с учетом весов"),
    PROMETHEE("PROMETHEE II"),
    ENSEMBLE("Ансамбль алгоритмов");

    String name;
}
