package ru.hse.townassessment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.hse.townassessment.model.entity.dict.DictTown;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Историческая оценка городов.
 */
@Entity
@Table(name = "assessment_hist")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssessmentHist {

    @Id
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "town_id", nullable = false)
    DictTown town;

    @ManyToOne
    @JoinColumn(name = "criteria_id", nullable = false)
    Criteria criteria;

    @Column(name = "value")
    @NotNull
    Double value;

    @Column(name = "description")
    String description;

    @Column(name = "update_date")
    LocalDateTime updateDate;
}
