package ru.hse.townassessment.util;

import ru.hse.townassessment.model.AnalyticsResponse;
import ru.hse.townassessment.model.DataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Ensemble {

    public static List<AnalyticsResponse> analyze(List<DataModel> dataModels) {

        List<DataModel> sumNormalization = new ArrayList<>(dataModels);
        SumNormalization.analyze(sumNormalization);
        List<AnalyticsResponse> sumNormalizationResponse = CommonUtils.prepareResponse(sumNormalization, true);

        List<DataModel> sumClustering = new ArrayList<>(dataModels);
        SumClustering.analyze(sumClustering);
        List<AnalyticsResponse> sumClusteringResponse = CommonUtils.prepareResponse(sumClustering, true);

        List<DataModel> hellwigWithWeight = new ArrayList<>(dataModels);
        HellwigWithWeight.analyze(hellwigWithWeight);
        List<AnalyticsResponse> hellwigWithWeightResponse = CommonUtils.prepareResponse(hellwigWithWeight, true);

        List<DataModel> promethee = new ArrayList<>(dataModels);
        Promethee.analyze(promethee);
        List<AnalyticsResponse> prometheeResponse = CommonUtils.prepareResponse(promethee, true);

        List<String> blocks = dataModels.stream().map(DataModel::getBlockName).sorted().collect(Collectors.toList());
        blocks.add(0, "Общая оценка");

        AtomicInteger order = new AtomicInteger(0);
        List<AnalyticsResponse> responses = new LinkedList<>();
        blocks.forEach(block -> {
            Map<String, Double> townRate = new HashMap<>(rate(sumNormalizationResponse.stream().filter(s -> block.equals(s.getBlockName())).findFirst().get().getTownAssessment()));
            rate(sumClusteringResponse.stream().filter(s -> block.equals(s.getBlockName())).findFirst().get().getTownAssessment())
                    .forEach((k, v) -> townRate.merge(k, v, Double::sum));
            rate(hellwigWithWeightResponse.stream().filter(s -> block.equals(s.getBlockName())).findFirst().get().getTownAssessment())
                    .forEach((k, v) -> townRate.merge(k, v, Double::sum));
            rate(prometheeResponse.stream().filter(s -> block.equals(s.getBlockName())).findFirst().get().getTownAssessment())
                    .forEach((k, v) -> townRate.merge(k, v, Double::sum));

            townRate.forEach((k, v) -> {
                townRate.put(k, v / 4);
            });

            List<Map.Entry<String, Double>> tempList = townRate.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
            Map<String, Double> result = new LinkedHashMap<>();
            tempList.forEach(t -> {
                result.put(t.getKey(), t.getValue());
            });

            responses.add(new AnalyticsResponse(block, result, order.getAndIncrement()));
        });
        return responses;
    }

    private static Map<String, Double> rate(Map<String, Double> townAssessment) {
        Map<String, Double> townRate = new HashMap<>();
        AtomicInteger rate = new AtomicInteger(1);
        townAssessment.forEach((k, v) -> {
            townRate.put(k, (double) rate.getAndIncrement());
        });
        return townRate;
    }
}

