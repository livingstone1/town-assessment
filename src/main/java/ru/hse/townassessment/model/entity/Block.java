package ru.hse.townassessment.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/** Концептуальный блок. */
@Entity
@Table(name = "block")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Block {

    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "name")
    @NotNull
    @Size(min = 1, max = 1024)
    String name;

    @Column(name = "k")
    @NotNull
    Double k;

    @Column(name = "update_date")
    LocalDateTime updateDate;

    @Column(name = "delete_date")
    LocalDateTime deleteDate;
}
