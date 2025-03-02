package ru.ir.visualiser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToOne(mappedBy = "project")
    @Setter
    @JsonIgnore
    private Ir ir;

    public Project(String name) {
        this.name = name;
    }
}
