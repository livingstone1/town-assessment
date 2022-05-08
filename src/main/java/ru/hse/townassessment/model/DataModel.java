package ru.hse.townassessment.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.hse.townassessment.model.entity.Criteria;
import ru.hse.townassessment.model.entity.dict.DictTown;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataModel {

    Double blockK;

    String blockName;

    /** Заголовки столбцов. */
    Map<Integer, Criteria> criteriaByIndex;

    /** Заголовки строк. */
    Map<Integer, DictTown> townsByIndex;

    /** Оценки, строки (i) - города, столбцы (j) - критерии. */
    double[][] assessments;

    /** Город - результирующая оценка. */
    Map<String, Double> townAssessment;

    public Map<String, Double> getSortedTownAssessment() {
        List<Map.Entry<String, Double>> tempList = townAssessment.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        Collections.reverse(tempList);
        Map<String, Double> result = new LinkedHashMap<>();
        tempList.forEach(t -> {
            result.put(t.getKey(), t.getValue());
        });
        return result;
    }
}