package ru.hse.townassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.townassessment.model.entity.dict.DictTown;

@Repository
public interface DictTownRepository extends JpaRepository<DictTown, Integer> {
}
