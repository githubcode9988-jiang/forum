package practice.example.forum.mapper;

import practice.example.forum.model.Comment;

/**
 * @Author jiang
 * @Created Project on 2022/1/22
 */
public interface CommentExtMapper {

    void incCommentCount(Comment comment);

    Integer getCommentCount();
}
