package practice.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.User;
import practice.example.forum.service.NotificationService;
import practice.example.forum.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author jiang
 * @Created Project on 2022/1/10
 */
@Controller
public class ProfileController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name= "action")String action,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size,
                          Model model){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if("questions".equals(action)){
            PaginationDTO pagination = questionService.findQuestionById(Integer.valueOf(user.getAccountId()),page, size);
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            model.addAttribute("pagination",pagination);
        }else if("replies".equals(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getAccountId(),page,size);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            model.addAttribute("pagination",paginationDTO);
        }else if("letters".equals(action)){
            PaginationDTO paginationDTO = notificationService.listByLetter(user.getAccountId(),page,size);
            model.addAttribute("section","letters");
            model.addAttribute("sectionName","你的私信");
            model.addAttribute("pagination",paginationDTO);
        }

        return "profile";
    }
}
