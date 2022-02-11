package practice.example.forum.service;

import practice.example.forum.dto.CommentDTO;
import practice.example.forum.enums.CommentTypeEnum;
import practice.example.forum.model.Comment;
import practice.example.forum.model.Question;
import practice.example.forum.model.User;

import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/16
 */
public interface CommentService {

    void insert(Comment comment, User user);

    List<CommentDTO> listByQuestionId(Long id, CommentTypeEnum type);

    List<CommentDTO> listByCommentId(Long id, CommentTypeEnum comment);


}
