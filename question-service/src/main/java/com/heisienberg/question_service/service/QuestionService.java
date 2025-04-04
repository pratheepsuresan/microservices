package com.heisienberg.question_service.service;

import com.heisienberg.question_service.dao.QuestionDao;
import com.heisienberg.question_service.model.Questions;
import com.heisienberg.question_service.model.QuestionsWrapper;
import com.heisienberg.question_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Questions>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Questions>> getByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Questions> addQuestion(Questions questions) {
        return new ResponseEntity<>(questionDao.save(questions),HttpStatus.CREATED);
    }

    public ResponseEntity<Questions> updateQuestion(Questions questions) {
        return new ResponseEntity<>(questionDao.save(questions),HttpStatus.OK);
    }

    public ResponseEntity<String> deleteQuestionById(Integer id) {
        questionDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<Integer>> getQuestionsforQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName,numQuestions);
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionsWrapper>> getQuestionsFromId(List<Integer> questionsIds) {
        List<QuestionsWrapper> wrappers = new ArrayList<>();
        List<Questions> questions = new ArrayList<>();

        for(Integer id : questionsIds){
            questions.add(questionDao.findById(id).get());
        }

        for(Questions q : questions){
            QuestionsWrapper wrapper = new QuestionsWrapper();
            wrapper.setId(q.getId());
            wrapper.setQuestiontitle(q.getQuestiontitle());
            wrapper.setOption1(q.getOption1());
            wrapper.setOption2(q.getOption2());
            wrapper.setOption3(q.getOption3());
            wrapper.setOption4(q.getOption4());
            wrappers.add(wrapper);
        }

        return new ResponseEntity<>(wrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateScore(List<Response> responses) {

        int right = 0;
        for(Response r : responses){
            Questions questions = questionDao.findById(r.getId()).get();
            if(r.getResponse().equals(questions.getRightAnswer()))
                right++;
        }
        return new ResponseEntity<>(right,HttpStatus.OK);
    }
}
