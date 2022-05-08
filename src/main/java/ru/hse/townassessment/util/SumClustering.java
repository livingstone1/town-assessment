package ru.hse.townassessment.util;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import ru.hse.townassessment.model.DataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SumClustering {

    public static void analyze(List<DataModel> dataModels) {
        clustering(dataModels);
        CommonUtils.weighAssessments(dataModels);
        CommonUtils.townAssessmentBySum(dataModels);
    }

    public static void clustering(List<DataModel> dataModels) {
        dataModels.forEach(dataModel -> {
            double[][] transpose = MatrixUtils.createRealMatrix(dataModel.getAssessments()).transpose().getData();
            //by criteria
            for (int j = 0; j < transpose.length; j++) {
                if (dataModel.getCriteriaByIndex().get(j).getType().getCode().equals("number")) {

                    Clusterer<DoublePoint> kmeans = new KMeansPlusPlusClusterer<>(5);

                    List<DoublePoint> points = new ArrayList<>();
                    for (int i = 0; i < transpose[j].length; i++) {
                        points.add(new DoublePoint(new double[]{transpose[j][i]}));
                    }
                    List<CentroidCluster<DoublePoint>> clustersMapSorted = (List<CentroidCluster<DoublePoint>>) kmeans.cluster(points);
                    Map<Double, List<Double>> clustersMap = new TreeMap<>();
                    clustersMapSorted.forEach(cluster -> {
                        List<Double> clusterValues = cluster.getPoints().stream().map(p -> p.getPoint()[0]).collect(Collectors.toList());
                        clustersMap.put(cluster.getCenter().getPoint()[0], clusterValues);
                    });
                    Map<Double, List<Double>> clustersMapForPoint = new TreeMap<>();
                    AtomicInteger point = new AtomicInteger(1);
                    clustersMap.forEach((k, v) -> {
                        clustersMapForPoint.put((double) point.getAndIncrement(), v);
                    });
                    for (int i = 0; i < transpose[j].length; i++) {
                        for (Map.Entry<Double, List<Double>> cluster : clustersMapForPoint.entrySet()) {
                            if (cluster.getValue().contains(transpose[j][i])) {
                                transpose[j][i] = cluster.getKey();
                            }
                        }
                    }
                }
            }
            dataModel.setAssessments(MatrixUtils.createRealMatrix(transpose).transpose().getData());
        });
    }
}
