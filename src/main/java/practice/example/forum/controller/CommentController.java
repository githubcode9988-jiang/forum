package practice.example.forum.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import practice.example.forum.dto.CommentDTO;
import practice.example.forum.dto.ResultDTO;
import practice.example.forum.enums.CommentTypeEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.mapper.CommentMapper;
import practice.example.forum.model.Comment;
import practice.example.forum.model.User;
import practice.example.forum.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jiang
 * @Created Project on 2022/1/16
 */
@Controller
public class CommentController {

    @Autowired
    private CommentMapper  commentMapper;

    @Autowired
    private CommentService commentService;

     @ResponseBody
     @RequestMapping(value = "/comment",method = RequestMethod.POST)
     public Object post(@RequestBody CommentDTO commentDTO,
                        HttpServletRequest request){
         User user = (User) request.getSession().getAttribute("user");
         if(user == null){
             return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
         }

         if(commentDTO == null || StringUtils.isBlank(commentDTO.getContent())){
             return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_NOT_FOUND);
         }

         Comment comment = new Comment();
         comment.setParentId(commentDTO.getParentId());
         comment.setContent(commentDTO.getContent());
         comment.setType(commentDTO.getType());
         comment.setGmtCreate(System.currentTimeMillis());
         comment.setGmtModified(System.currentTimeMillis());
         comment.setCommentator(Long.valueOf(user.getAccountId()));
         comment.setLikeCount(0L);
         commentService.insert(comment,user);
       return ResultDTO.okOf();
     }

     @ResponseBody
     @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
     public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id")Long id){
            if(StringUtils.isBlank(String.valueOf(id))){
                return ResultDTO.errorOf(CustomizeErrorCode.PARAMETER_IS_NULL);
            }
         List<CommentDTO> commentDTOS = commentService.listByCommentId(id, CommentTypeEnum.COMMENT);
         return ResultDTO.okOf(commentDTOS);
     }
}
