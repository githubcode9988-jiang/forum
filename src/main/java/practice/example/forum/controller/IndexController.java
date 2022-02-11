package practice.example.forum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.Question;
import practice.example.forum.model.User;
import practice.example.forum.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/8
 */
@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "10") Integer size,
                        @RequestParam(name = "search",required = false)String search){
        PaginationDTO pagination = questionService.findListQuestion(search,page, size);
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        logger.info("Visit the home page...");
//        if(cookies == null || cookies.length == 0){
//            PaginationDTO pagination = new PaginationDTO();
//            List<QuestionDTO> res = new ArrayList<>();
//            pagination.setQuestions(res);
//            model.addAttribute("pagination",pagination);
//            return "index";
//        }
         return "index";
    }


}
