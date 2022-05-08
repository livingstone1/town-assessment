package ru.hse.townassessment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.hse.townassessment.model.AlgorithmEnum;
import ru.hse.townassessment.model.AnalyticsResponse;
import ru.hse.townassessment.model.DataModel;
import ru.hse.townassessment.model.entity.Assessment;
import ru.hse.townassessment.model.entity.Criteria;
import ru.hse.townassessment.model.entity.dict.DictTown;
import ru.hse.townassessment.repository.AssessmentRepository;
import ru.hse.townassessment.repository.BlockRepository;
import ru.hse.townassessment.repository.CriteriaRepository;
import ru.hse.townassessment.repository.DictTownRepository;
import ru.hse.townassessment.util.CommonUtils;
import ru.hse.townassessment.util.Ensemble;
import ru.hse.townassessment.util.Hellwig;
import ru.hse.townassessment.util.HellwigWithWeight;
import ru.hse.townassessment.util.Promethee;
import ru.hse.townassessment.util.SumClustering;
import ru.hse.townassessment.util.SumNormalization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnalyticsService {

    /**
     * Репозиторий оценок.
     */
    AssessmentRepository assessmentRepository;

    /**
     * Репозиторий городов.
     */
    DictTownRepository dictTownRepository;

    /**
     * Репозиторий критериев.
     */
    CriteriaRepository criteriaRepository;

    /**
     * Репозиторий блоков.
     */
    BlockRepository blockRepository;

    private List<DataModel> getDataModels() {
        List<DataModel> dataModels = new ArrayList<>();

        AtomicInteger townsCount = new AtomicInteger(0);
        Map<Integer, DictTown> townsByIndex = new HashMap<>();
        dictTownRepository.findAll().forEach(town -> {
            townsByIndex.put(townsCount.getAndIncrement(), town);
        });

        blockRepository.findAllByDeleteDateIsNull().forEach(block -> {
            DataModel dataModel = new DataModel();
            dataModel.setBlockK(block.getK());
            dataModel.setBlockName(block.getName());

            dataModel.setTownsByIndex(townsByIndex);
            AtomicInteger criteriaCount = new AtomicInteger(0);
            Map<Integer, Criteria> criteriaByIndex = new HashMap<>();
            criteriaRepository.findAllByDeleteDateIsNullAndBlock(block).forEach(criteria -> {
                criteriaByIndex.put(criteriaCount.getAndIncrement(), criteria);
            });
            dataModel.setCriteriaByIndex(criteriaByIndex);

            // строки (i) - города, столбцы (j) - критерии
            double[][] assessments = new double[townsCount.get()][criteriaCount.get()];

            townsByIndex.forEach((i, town) -> {
                criteriaByIndex.forEach((j, criteria) -> {
                    assessments[i][j] = assessmentRepository.findByTownAndCriteria(town, criteria).map(Assessment::getValue).orElse(0D);
                });
            });
            dataModel.setAssessments(assessments);
            dataModel.setTownAssessment(new HashMap<>());

            dataModels.add(dataModel);
        });
        return dataModels;
    }


    public List<AnalyticsResponse> analyze(AlgorithmEnum algorithm) {
        List<DataModel> dataModels = getDataModels();
        switch (algorithm) {
            case SUM_NORMALIZATION: {
                SumNormalization.analyze(dataModels);
                return CommonUtils.prepareResponse(dataModels, true);
            }
            case SUM_WITH_CLUSTERING: {
                SumClustering.analyze(dataModels);
                return CommonUtils.prepareResponse(dataModels, true);
            }
            case HELLWIG: {
                Hellwig.analyze(dataModels);
                return CommonUtils.prepareResponse(dataModels, false);
            }
            case HELLWIG_WITH_WEIGHT: {
                HellwigWithWeight.analyze(dataModels);
                return CommonUtils.prepareResponse(dataModels, true);
            }
            case PROMETHEE: {
                Promethee.analyze(dataModels);
                return CommonUtils.prepareResponse(dataModels, true);
            }
            case ENSEMBLE: {
                return Ensemble.analyze(dataModels);
            }
        }
        return null;
    }


}
