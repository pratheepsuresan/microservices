package com.heisienberg.question_service.dao;

import com.heisienberg.question_service.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Questions,Integer>{

    List<Questions> findByCategory(String category);

    @Query(value="select q.id from questions q where q.category=:category order by random() limit :limit",nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, int limit);
}
