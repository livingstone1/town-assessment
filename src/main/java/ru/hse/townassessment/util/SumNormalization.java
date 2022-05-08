package ru.hse.townassessment.util;

import ru.hse.townassessment.model.DataModel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SumNormalization {

    public static void analyze(List<DataModel> dataModels) {
        normalize(dataModels);
        CommonUtils.weighAssessments(dataModels);
        CommonUtils.townAssessmentBySum(dataModels);
    }

    public static void normalize(List<DataModel> dataModels) {
        AtomicReference<Double> minAssessment = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> maxAssessment = new AtomicReference<>(Double.MIN_VALUE);

        dataModels.forEach(dataModel -> {
            // assessment
            double minA = Arrays.stream(dataModel.getAssessments())
                    .flatMapToDouble(Arrays::stream).min().orElse(minAssessment.get());
            if (minA < minAssessment.get()) {
                minAssessment.set(minA);
            }
            double maxA = Arrays.stream(dataModel.getAssessments())
                    .flatMapToDouble(Arrays::stream).max().orElse(minAssessment.get());
            if (maxA > maxAssessment.get()) {
                maxAssessment.set(maxA);
            }
        });

        dataModels.forEach(dataModel -> {
            for (int i = 0; i < dataModel.getAssessments().length; i++) {
                for (int j = 0; j < dataModel.getAssessments()[i].length; j++) {
                    dataModel.getAssessments()[i][j] = norm(dataModel.getAssessments()[i][j], minAssessment.get(), maxAssessment.get());
                }
            }
        });
    }

    private static double norm(double x, double min, double max) {
        return (x - min) / (max - min == 0 ? 1 : max - min);
    }
}
