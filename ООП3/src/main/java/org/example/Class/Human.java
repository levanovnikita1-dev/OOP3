package org.example.Class;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "humans")
@Inheritance(strategy = InheritanceType.JOINED)
@Data // Lombok теперь будет работать!
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer level;
    private Integer hp;
    private Integer position;
    private Integer damage;

    @Column(name = "hero_type")
    private String heroType;

    @OneToMany(mappedBy = "human", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ActionLog> actionLogs;
}