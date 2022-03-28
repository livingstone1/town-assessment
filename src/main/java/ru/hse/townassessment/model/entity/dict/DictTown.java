package ru.hse.townassessment.model.entity.dict;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.hse.townassessment.model.entity.Assessment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Город.
 */
@Entity
@Table(name = "d_town")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DictTown {

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

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    DictRegion region;

    @OneToMany(mappedBy = "town")
    @JsonManagedReference
    List<Assessment> assessmentList;
}