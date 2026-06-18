package org.example.Class;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "healers")
@PrimaryKeyJoinColumn(name = "human_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Healer extends Human {
    @Column(name = "heal_power")
    private Integer healPower;

    @Column(name = "can_resurrect")
    private Boolean canResurrect;
}