package com.codefellowship.restap.Repository;


import com.codefellowship.restap.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {

    List<Posts> findAllByUserUsername(String username);

}