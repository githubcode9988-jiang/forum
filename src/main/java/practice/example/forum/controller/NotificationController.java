package practice.example.forum.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import practice.example.forum.dto.*;
import practice.example.forum.enums.CommentTypeEnum;
import practice.example.forum.enums.NotificationEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.model.User;
import practice.example.forum.service.NotificationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/25
 */
@Controller
@Log4j2
public class NotificationController {


    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name= "id")String id){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.error("NotificationController get user is null!");
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);

        if(NotificationEnum.REPLY_QUESTION.getType() == notificationDTO.getType() ||
           NotificationEnum.REPLY_COMMENT.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterid();
        }
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping(value = "/inviteMessage")
    public Object inviteMessage(@RequestParam(name = "inviteMessage") String inviteMessage,
                                @RequestParam(name = "questionId") String questionId,
                                HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(StringUtils.isBlank(questionId)){
            return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        notificationService.iniviteMessage(user,inviteMessage,questionId);
        return ResultDTO.okOf();
    }

}
