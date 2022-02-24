package practice.example.forum.service;

import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.dto.ResultDTO;
import practice.example.forum.dto.ThumbDTO;
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

    void incView(QuestionDTO questionDTO);

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
     * @param thumbDTO
     */
    ResultDTO incLikeCount(ThumbDTO thumbDTO);

    /**
     *  是否已经点赞
     * @param thumbDTO
     * @return
     */
    ResultDTO isLikeCount(ThumbDTO thumbDTO);
}
