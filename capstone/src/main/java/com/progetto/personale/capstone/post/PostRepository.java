package com.progetto.personale.capstone.post;

import com.progetto.personale.capstone.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN p.savedBy u WHERE u.id = :userId")
    List<Post> findAllSavedPostsByUserId(@Param("userId") Long userId);

}
