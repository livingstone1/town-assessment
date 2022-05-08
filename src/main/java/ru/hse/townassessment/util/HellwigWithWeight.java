package ru.hse.townassessment.util;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import ru.hse.townassessment.model.DataModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HellwigWithWeight {

    public static void analyze(List<DataModel> dataModels) {
        calculate(dataModels);
    }

    public static void calculate(List<DataModel> dataModels) {
        dataModels.forEach(dataModel -> {
            double[][] transpose = MatrixUtils.createRealMatrix(dataModel.getAssessments()).transpose().getData();
            Map<Integer, Double> minOrMaxByIndex = new HashMap<>();
            // by criteria
            for (int j = 0; j < transpose.length; j++) {
                double arithmeticMean = new Mean().evaluate(transpose[j]);
                double standardDeviation = new StandardDeviation().evaluate(transpose[j]);
                for (int i = 0; i < transpose[j].length; i++) {
                    transpose[j][i] = ((transpose[j][i] - arithmeticMean) / standardDeviation) * dataModel.getCriteriaByIndex().get(j).getK();
                }
                if (dataModel.getCriteriaByIndex().get(j).getIsStimulus()) {
                    minOrMaxByIndex.put(j, Arrays.stream(transpose[j]).max().orElse(0));
                } else {
                    minOrMaxByIndex.put(j, Arrays.stream(transpose[j]).min().orElse(0));
                }
            }
            dataModel.setAssessments(MatrixUtils.createRealMatrix(transpose).transpose().getData());

            // by town
            for (int i = 0; i < dataModel.getAssessments().length; i++) {
                double sum = 0;
                for (int j = 0; j < dataModel.getAssessments()[i].length; j++) {
                    sum += Math.pow(dataModel.getAssessments()[i][j] - minOrMaxByIndex.get(j), 2);
                }
                dataModel.getTownAssessment().put(dataModel.getTownsByIndex().get(i).getName(), Math.sqrt(sum));
            }

            double arithmeticMeanTownAssessment = new Mean().evaluate(dataModel.getTownAssessment().values().stream().mapToDouble(Double::doubleValue).toArray());
            double standardDeviationTownAssessment = new StandardDeviation().evaluate(dataModel.getTownAssessment().values().stream().mapToDouble(Double::doubleValue).toArray());

            double d0 = arithmeticMeanTownAssessment + 2 * standardDeviationTownAssessment;

            dataModel.getTownAssessment().forEach((k, v) -> {
                dataModel.getTownAssessment().put(k, 1 - (v / d0));
            });

        });
    }
}
