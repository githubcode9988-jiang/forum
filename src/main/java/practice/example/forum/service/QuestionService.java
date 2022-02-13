package practice.example.forum.service;

import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.model.Question;

import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/9
 */
public interface QuestionService {


    PaginationDTO findListQuestion(String search,Integer page, Integer size);

    PaginationDTO findQuestionById(Integer userAccoundId, Integer page, Integer size);

    QuestionDTO getById(String userId);

    void createOrUpdate(Question question);

    void incView(String id);

    List<QuestionDTO> selectRelated(QuestionDTO questionDTO);

    /**
     *   通过tag查询数据
     * @param tag
     * @param page
     * @param size
     * @return
     */
    PaginationDTO findListQuestionByTag(String tag, Integer page, Integer size);

    /**
     *  日记点赞
     * @param id
     */
    void incLikeCount(String id);
}
