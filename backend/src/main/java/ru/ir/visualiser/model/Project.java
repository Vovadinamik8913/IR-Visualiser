package ru.ir.visualiser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToMany(mappedBy = "project")
    @Setter
    @JsonIgnore
    private List<Ir> irs = new ArrayList<>();
    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate changed;

    public Project(String name) {
        this.name = name;
        changed = LocalDate.now();
    }
}
