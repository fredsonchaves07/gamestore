package com.fredsonchaves07.gamestore.domain.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game {

    @Id
    private Integer id;

    private String name;

    private int rating;

    private int platformId;

    private boolean isGold = false;

    private boolean isPlatina = false;

    private LocalDateTime finishedAt;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "games_platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private final Set<Platform> platforms = new HashSet<>();

    @Deprecated
    public Game() {}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
