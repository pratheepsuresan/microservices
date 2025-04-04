package com.heisienberg.quiz_service.service;

import com.heisienberg.quiz_service.dao.QuizDao;
import com.heisienberg.quiz_service.feign.QuizInterface;
import com.heisienberg.quiz_service.model.QuestionsWrapper;
import com.heisienberg.quiz_service.model.Quiz;
import com.heisienberg.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int limit, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(category,limit).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionsIds(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionsWrapper>> getQuiz(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Integer> questionIDs = quiz.get().getQuestionsIds();
        ResponseEntity<List<QuestionsWrapper>> questionForUser = quizInterface.getQuestiondFromID(questionIDs);
        return questionForUser;
    }

    public ResponseEntity<Integer> calculate(Integer id, List<Response> response) {
        return quizInterface.getScore(response);
    }
}
