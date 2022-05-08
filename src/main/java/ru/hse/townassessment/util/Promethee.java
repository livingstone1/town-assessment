package ru.hse.townassessment.util;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import ru.hse.townassessment.model.DataModel;

import java.util.List;

public class Promethee {

    public static void analyze(List<DataModel> dataModels) {
        calculate(dataModels);
    }

    public static void calculate(List<DataModel> dataModels) {
        dataModels.forEach(dataModel -> {
            double[][] temp = new double[dataModel.getAssessments().length][dataModel.getAssessments().length];
            for (int a = 0; a < dataModel.getAssessments().length; a++) {
                for (int b = 0; b < dataModel.getAssessments().length; b++) {
                    double sumAB = 0;
                    double sumBA = 0;
                    for (int j = 0; j < dataModel.getAssessments()[b].length; j++) {
                        if (dataModel.getCriteriaByIndex().get(j).getIsStimulus()) {
                            sumAB += dataModel.getCriteriaByIndex().get(j).getK() * (dataModel.getAssessments()[a][j] - dataModel.getAssessments()[b][j]) > 0 ? 1 : 0;
                            sumBA += dataModel.getCriteriaByIndex().get(j).getK() * (dataModel.getAssessments()[b][j] - dataModel.getAssessments()[a][j]) > 0 ? 1 : 0;
                        } else {
                            sumAB -= dataModel.getCriteriaByIndex().get(j).getK() * (dataModel.getAssessments()[a][j] - dataModel.getAssessments()[b][j]) > 0 ? 1 : 0;
                            sumBA -= dataModel.getCriteriaByIndex().get(j).getK() * (dataModel.getAssessments()[b][j] - dataModel.getAssessments()[a][j]) > 0 ? 1 : 0;
                        }
                        temp[a][b] = sumAB;
                        temp[b][a] = sumBA;
                    }
                }
            }
            // + stream
            for (int i = 0; i < temp.length; i++) {
                dataModel.getTownAssessment().put(dataModel.getTownsByIndex().get(i).getName(), new Sum().evaluate(temp[i]));
            }
            // - stream
            double[][] tempTranspose = MatrixUtils.createRealMatrix(temp).transpose().getData();
            for (int i = 0; i < tempTranspose.length; i++) {
                dataModel.getTownAssessment().merge(dataModel.getTownsByIndex().get(i).getName(),
                        new Sum().evaluate(tempTranspose[i]) * -1, Double::sum);
            }
        });
    }
}
