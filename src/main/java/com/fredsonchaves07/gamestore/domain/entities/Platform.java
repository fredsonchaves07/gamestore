package com.fredsonchaves07.gamestore.domain.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "platforms")
public class Platform {

    @Id
    private Integer id;

    private String name;

    private boolean isActive = false;

    private boolean isLastChosen = false;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "platforms")
    private Set<Game> games;

    @Deprecated
    public Platform(){}

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setLastChosen(boolean lastChosen) {
        isLastChosen = lastChosen;
    }
}
