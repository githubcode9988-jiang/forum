package practice.example.forum.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import practice.example.forum.dto.CommentDTO;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.dto.ResultDTO;
import practice.example.forum.dto.ThumbDTO;
import practice.example.forum.enums.CommentTypeEnum;
import practice.example.forum.model.User;
import practice.example.forum.service.CommentService;
import practice.example.forum.service.QuestionService;
import practice.example.forum.vo.ThumbVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/11
 */
@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")String id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOS = commentService.listByQuestionId(Long.valueOf(id), CommentTypeEnum.QUESTION);
        //增加阅读数
        questionService.incView(questionDTO);
        //访问fastdfs服务器
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOS);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }

    @ResponseBody
    @PostMapping("/likeCount")
    public ResultDTO likeCount(@RequestBody ThumbVO thumbVO,
                               Model model,
                               HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        ThumbDTO thumbDTO = new ThumbDTO();
        BeanUtils.copyProperties(thumbVO,thumbDTO);
        thumbDTO.setUser(user);
        thumbDTO.setGmtCreate(System.currentTimeMillis());
        thumbDTO.setGmtModified(thumbDTO.getGmtCreate());
        ResultDTO resultDTO = questionService.incLikeCount(thumbDTO);
        return resultDTO;
    }

    @ResponseBody
    @PostMapping("/isLikeCount")
    public ResultDTO isLikeCount(@RequestBody ThumbVO thumbVo,
                                 Model model,
                                 HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        ThumbDTO thumbDTO = new ThumbDTO();
        BeanUtils.copyProperties(thumbVo,thumbDTO);
        thumbDTO.setUser(user);
        thumbDTO.setGmtCreate(System.currentTimeMillis());
        thumbDTO.setGmtModified(thumbDTO.getGmtCreate());
        ResultDTO resultDTO = questionService.isLikeCount(thumbDTO);
        return resultDTO;
    }
}
