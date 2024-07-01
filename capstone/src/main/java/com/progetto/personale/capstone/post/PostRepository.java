package com.progetto.personale.capstone.post;

import com.progetto.personale.capstone.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

}
