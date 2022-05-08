package ru.hse.townassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.townassessment.model.entity.AssessmentHist;

@Repository
public interface AssessmentHistRepository extends JpaRepository<AssessmentHist, Integer> {
}
