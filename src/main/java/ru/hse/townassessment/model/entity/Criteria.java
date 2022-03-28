package ru.hse.townassessment.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.hse.townassessment.model.entity.dict.DictCriteriaType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/** Критерий. */
@Entity
@Table(name = "criteria")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Criteria {

    @Id
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "d_criteria_type_id", nullable = false)
    DictCriteriaType type;

    @Column(name = "name")
    @NotNull
    @Size(min = 1, max = 1024)
    String name;

    @Column(name = "k")
    @NotNull
    Double k;

    @Column(name = "is_stimulus")
    @NotNull
    Boolean isStimulus;

    @Column(name = "description")
    String description;

    @Column(name = "update_date")
    LocalDateTime updateDate;

    @Column(name = "delete_date")
    LocalDateTime deleteDate;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    Block block;
}
