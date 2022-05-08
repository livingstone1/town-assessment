package ru.hse.townassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.townassessment.model.entity.Block;
import ru.hse.townassessment.model.entity.Criteria;

import java.util.List;

@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Integer> {

    List<Criteria> findAllByDeleteDateIsNull();

    List<Criteria> findAllByDeleteDateIsNullAndBlock(Block block);
}
