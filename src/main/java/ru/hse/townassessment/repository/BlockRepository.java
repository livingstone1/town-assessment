package ru.hse.townassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.townassessment.model.entity.Block;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Integer> {

    List<Block> findAllByDeleteDateIsNull();
}
