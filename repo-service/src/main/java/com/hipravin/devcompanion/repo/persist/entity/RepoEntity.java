package com.hipravin.devcompanion.repo.persist.entity;

import javax.persistence.*;

@Entity
@Table(name = "REPO")
public class RepoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repoIdSeq")
    @SequenceGenerator(sequenceName = "REPO_ID_SEQ", allocationSize = 100, name = "repoIdSeq")
    @Column(name = "ID")
    private Long id;

    @Basic
    @Column(name = "NAME")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
