package com.progetto.personale.capstone.post;

import com.progetto.personale.capstone.comment.Comment;
import com.progetto.personale.capstone.security.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, unique = true)
    private String titolo;

    @Column(length = 500, unique = true)
    private String descrizione;

    @Column
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedBy = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "post_saves",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> savedBy = new HashSet<>();

    @Override
    public String toString() {
        return "Post{id=" + id + ", titolo='" + titolo + "'}";
    }
    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public int getLikeCount() {
        return likedBy != null ? likedBy.size() : 0;
    }

    public int getSaveCount() {
        return savedBy != null ? savedBy.size() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
