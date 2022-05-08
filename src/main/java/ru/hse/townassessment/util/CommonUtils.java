package ru.hse.townassessment.util;

import ru.hse.townassessment.model.AnalyticsResponse;
import ru.hse.townassessment.model.DataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CommonUtils {

    public static void weighAssessments(List<DataModel> dataModels) {
        dataModels.forEach(dataModel -> {
            for (int i = 0; i < dataModel.getAssessments().length; i++) {
                for (int j = 0; j < dataModel.getAssessments()[i].length; j++) {
                    dataModel.getAssessments()[i][j] = dataModel.getAssessments()[i][j] * dataModel.getCriteriaByIndex().get(j).getK();
                }
            }
        });
    }

    public static void townAssessmentBySum(List<DataModel> dataModels) {
        dataModels.forEach(dataModel -> {
            for (int i = 0; i < dataModel.getAssessments().length; i++) {
                double sum = 0;
                for (int j = 0; j < dataModel.getAssessments()[i].length; j++) {
                    if (dataModel.getCriteriaByIndex().get(j).getIsStimulus()) {
                        sum += dataModel.getAssessments()[i][j];
                    } else {
                        sum -= dataModel.getAssessments()[i][j];
                    }
                }
                dataModel.getTownAssessment().put(dataModel.getTownsByIndex().get(i).getName(), sum);
            }
        });
    }

    /** Блок (в т.ч. общая оценка) - [Город/Оценка]. */
    public static List<AnalyticsResponse> prepareResponse(List<DataModel> dataModels, Boolean weightBlock) {
        List<AnalyticsResponse> response = new ArrayList<>();
        AtomicInteger order = new AtomicInteger(2);
        dataModels.stream().sorted(Comparator.comparing(DataModel::getBlockName)).collect(Collectors.toList()).forEach(dataModel -> {
            response.add(new AnalyticsResponse(
                    dataModel.getBlockName(),
                    dataModel.getSortedTownAssessment(),
                    order.getAndIncrement()
            ));
        });

        Map<String, Double> totalSum = new HashMap<>();
        dataModels.forEach(dataModel -> {
            dataModel.getTownAssessment().forEach((k, v) -> {
                totalSum.merge(k, v * (weightBlock ? dataModel.getBlockK() : 1), Double::sum);
            });
        });
        List<Map.Entry<String, Double>> tempList = totalSum.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        Collections.reverse(tempList);
        Map<String, Double> result = new LinkedHashMap<>();
        tempList.forEach(t -> {
            result.put(t.getKey(), t.getValue());
        });

        response.add(new AnalyticsResponse(
                "Общая оценка",
                result,
                1
        ));

        return response.stream().sorted(Comparator.comparing(AnalyticsResponse::getOrder)).collect(Collectors.toList());
    }


}
