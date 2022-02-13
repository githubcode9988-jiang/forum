package practice.example.forum.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import practice.example.forum.cache.TagCache;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.model.Question;
import practice.example.forum.mapper.QuestionMapper;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.User;
import practice.example.forum.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author jiang
 * @Created Project on 2022/1/8
 */
@Controller
public class PublishController {


    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionService questionService;


    @GetMapping("/edit/{action}")
    public String edit(@PathVariable(name = "action") String action,
                       Model model){
        //转到该用户对应的发布页面
        QuestionDTO question = questionService.getById(action);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",action);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam("id") String id,
                            HttpServletRequest request,
                            Model model){
        //从session中获取user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //过滤非法标签
        boolean invalidTags = TagCache.filterInvalid(tag);
        if(!invalidTags){
            model.addAttribute("error","有非法标签！"+invalidTags);
            return "publish";
        }

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        if(title == null || title.equals("")){
            model.addAttribute("error","标题不能为空！");
            return "publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error","问题描述不能为空！");
            return "publish";
        }
        if(tag == null || tag.equals("")){
            model.addAttribute("error","标签不能为空！");
            return "publish";
        }
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }else{
            Question question = new Question();
            question.setTitle(title);
            question.setDescription(description);
            question.setCreator(Long.valueOf(user.getAccountId()));
            question.setTag(tag);
            questionService.createOrUpdate(question);
            return "redirect:/";
        }
    }
}
