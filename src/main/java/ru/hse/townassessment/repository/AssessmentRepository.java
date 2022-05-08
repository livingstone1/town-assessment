package ru.hse.townassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.townassessment.model.entity.Assessment;
import ru.hse.townassessment.model.entity.Criteria;
import ru.hse.townassessment.model.entity.dict.DictTown;

import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {

    Optional<Assessment> findByTownAndCriteria(DictTown town, Criteria criteria);
}
