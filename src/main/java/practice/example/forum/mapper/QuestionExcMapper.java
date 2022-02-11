package practice.example.forum.mapper;

import practice.example.forum.model.Question;

import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/16
 */
public interface QuestionExcMapper {

    void create(Question question);

    List<Question> findListQuestion(Integer offset, Integer size);

    Integer getQuestionCount();

    List<Question>  findQuestionById(Integer userAccoundId,Integer offset,Integer size);

    Integer getQuestionCountById(Integer userAccoundId);

    Question getById(Integer id);

    void update(Question question);

    void updateToViewCount(Question question);

}
