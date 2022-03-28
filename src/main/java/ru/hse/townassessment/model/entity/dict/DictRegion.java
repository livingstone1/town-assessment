package ru.hse.townassessment.model.entity.dict;

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

/** Регион. */
@Entity
@Table(name = "d_region")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DictRegion {

    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "code")
    @NotNull
    @Size(min = 1, max = 16)
    String code;

    @Column(name = "name")
    @NotNull
    @Size(min = 1, max = 64)
    String name;
}
