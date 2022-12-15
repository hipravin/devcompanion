package com.hipravin.devcompanion.repo.persist.entity;

import com.hipravin.devcompanion.repo.model.ContentType;

import javax.persistence.*;

@Entity
@Table(name = "REPO_FILE")
@NamedQueries({
        @NamedQuery(name = "RepoTextFileEntity.findByRepoId",
                query="select rf from RepoTextFileEntity rf where rf.repo.id = :repoId"),
})
public class RepoTextFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repoFileIdSeq")
    @SequenceGenerator(sequenceName = "REPO_FILE_ID_SEQ", allocationSize = 100, name = "repoFileIdSeq")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPO_ID")
    private RepoEntity repo;

    @Enumerated(EnumType.STRING)
    @Column(name="CONTENT_TYPE")
    private ContentType contentType;

    @Basic
    @Column(name = "SIZE_BYTES")
    private long sizeBytes;

    @Basic
    @Column(name = "NAME")
    private String name;

    @Basic
    @Column(name = "RELATIVE_PATH")
    private String relativePath;

    @Basic
    @Column(name = "CONTENT")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RepoEntity getRepo() {
        return repo;
    }

    public void setRepo(RepoEntity repo) {
        this.repo = repo;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
