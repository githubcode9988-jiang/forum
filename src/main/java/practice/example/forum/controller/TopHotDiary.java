package practice.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import practice.example.forum.dto.ResultDTO;
import practice.example.forum.dto.TopDiaryDTO;
import practice.example.forum.enums.ContentTypeEnum;
import practice.example.forum.mapper.QuestionMapper;
import practice.example.forum.model.Question;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author jiang
 * @Created Project on 2022/2/21
 */
@Controller
public class TopHotDiary {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/queryAllDiary")
    public ResultDTO TopDiary(){
        List<Question> questions =
                questionMapper.queryByTagAllQuestion(ContentTypeEnum.DIARY.getMessage());
        ZSetOperations zops = redisTemplate.opsForZSet();
        for (Question question : questions) {
            zops.add(ContentTypeEnum.DIARY.getMessage(),question.getTitle(),question.getViewCount());
            zops.add(ContentTypeEnum.QUESTIONID.getMessage(),question.getId(),question.getViewCount());
        }
        Set res = zops.reverseRange(ContentTypeEnum.DIARY.getMessage(), 0, 2);
        Set questionRes = zops.reverseRange(ContentTypeEnum.QUESTIONID.getMessage(), 0, 2);
        //将取出的日记和该日记id对应
        TopDiaryDTO topDiaryDTO = new TopDiaryDTO();
        topDiaryDTO.setQuestionId(questionRes);
        topDiaryDTO.setTitle(res);
        return ResultDTO.okOf(topDiaryDTO);
    }
}
