package edu.ucdenver.ccp.cooccurrence.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "titles")
public class Title {
    @Column(name = "id")
    @NotEmpty
    @Id
    private int id;

    @Column(name = "hash")
    @NotEmpty
    private String hash;

    @ManyToMany
    @JoinTable(name = "concept_title",
            joinColumns = @JoinColumn(name = "title_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "concept_id", referencedColumnName = "id"))
    private List<Node> nodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
