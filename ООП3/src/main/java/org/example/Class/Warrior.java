package org.example.Class;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "warriors")
@PrimaryKeyJoinColumn(name = "human_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Warrior extends Human {
    private String weapon;

    @Column(name = "has_buff")
    private Boolean hasBuff;
}