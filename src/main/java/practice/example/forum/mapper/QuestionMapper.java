package practice.example.forum.mapper;

import practice.example.forum.dto.QuestionQueryDTO;
import practice.example.forum.model.Question;

import java.util.List;

public interface QuestionMapper {

    void create(Question question);

    List<Question> findListQuestion(QuestionQueryDTO questionQueryDTO);

    Integer getQuestionCount(String search);

    List<Question>  findQuestionById(Integer userAccountId,Integer offset,Integer size);

    Integer getQuestionCountById(Integer userAccoundId);

    Question getById(Long id);

    void update(Question question);

    void updateToViewCount(Question question);

    void incCommentCount(Question question);

    List<Question> selectRelated(Question question);

    /**
     *  通过tag查询数据
     * @param questionQueryDTO
     * @return
     */
    List<Question> findListQuestionByTag(QuestionQueryDTO questionQueryDTO);

    /**
     *  通过该tag条数
     * @param tag
     * @return
     */
    Integer getQuestionCountByTag(String tag);

    /**
     *  日记点赞
     * @param likeCount
     */
    void updateToLikeCount(Question likeCount);
}