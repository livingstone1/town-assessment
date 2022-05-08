package ru.hse.townassessment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.townassessment.model.entity.Assessment;
import ru.hse.townassessment.model.entity.Block;
import ru.hse.townassessment.model.entity.Criteria;
import ru.hse.townassessment.model.entity.dict.DictTown;
import ru.hse.townassessment.repository.AssessmentRepository;
import ru.hse.townassessment.repository.BlockRepository;
import ru.hse.townassessment.repository.CriteriaRepository;
import ru.hse.townassessment.repository.DictTownRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/assessment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssessmentController {

    CriteriaRepository criteriaRepository;

    DictTownRepository dictTownRepository;

    AssessmentRepository assessmentRepository;

    BlockRepository blockRepository;

    @GetMapping("/init")
    public void init() {
        List<Criteria> criteria = criteriaRepository.findAllByDeleteDateIsNull();
        List<DictTown> towns = dictTownRepository.findAll();
        towns.forEach(t -> {
            criteria.forEach(c -> {
                Assessment assessment = Assessment.builder()
                        .town(t)
                        .criteria(c)
                        .value(c.getType().getCode().equals("point") ?
                                (double) ThreadLocalRandom.current().nextInt(1, 6)
                                : (double) ThreadLocalRandom.current().nextInt(0, 100))
                        .updateDate(LocalDateTime.now())
                        .build();
                assessmentRepository.save(assessment);
            });
        });
    }

    @GetMapping("/init/weight")
    public void initWeight() {
        List<Block> blocks = blockRepository.findAllByDeleteDateIsNull();
        blocks.forEach(block -> {
            block.setK(ThreadLocalRandom.current().nextDouble(0.1, 10));
        });
        blockRepository.saveAll(blocks);

        List<Criteria> criteria = criteriaRepository.findAllByDeleteDateIsNull();
        criteria.forEach(cr -> {
            cr.setK(ThreadLocalRandom.current().nextDouble(0.1, 10));
        });
        criteriaRepository.saveAll(criteria);
    }

    @GetMapping("/init/weight/1")
    public void setWeightAsOne() {
        List<Block> blocks = blockRepository.findAllByDeleteDateIsNull();
        blocks.forEach(block -> {
            block.setK(1D);
        });
        blockRepository.saveAll(blocks);

        List<Criteria> criteria = criteriaRepository.findAllByDeleteDateIsNull();
        criteria.forEach(cr -> {
            cr.setK(1D);
        });
        criteriaRepository.saveAll(criteria);
    }
}
