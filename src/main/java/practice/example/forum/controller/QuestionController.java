package practice.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import practice.example.forum.cache.TagCache;
import practice.example.forum.dto.CommentDTO;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.enums.CommentTypeEnum;
import practice.example.forum.service.CommentService;
import practice.example.forum.service.QuestionService;
import practice.example.forum.utile.FastClientUtile;

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


    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")String id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOS = commentService.listByQuestionId(Long.valueOf(id), CommentTypeEnum.QUESTION);
        //增加阅读数
        questionService.incView(id);
        //访问fastdfs服务器
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOS);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
