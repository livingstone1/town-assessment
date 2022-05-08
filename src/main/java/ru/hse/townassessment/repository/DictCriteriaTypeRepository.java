package ru.hse.townassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.townassessment.model.entity.dict.DictCriteriaType;

@Repository
public interface DictCriteriaTypeRepository extends JpaRepository<DictCriteriaType, Integer> {
}
