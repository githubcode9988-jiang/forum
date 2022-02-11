package practice.example.forum.dto;

import lombok.Data;
import practice.example.forum.model.User;

/**
 * @Author jiang
 * @Created Project on 2022/1/16
 */
@Data
public class CommentDTO {

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.id
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.parent_id
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Long parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.type
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.commentator
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Long commentator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.gmt_create
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Long gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.gmt_modified
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Long gmtModified;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.like_count
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Long likeCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.content
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.comment_count
     *
     * @mbg.generated Sun Jan 16 18:08:07 CST 2022
     */
    private Integer commentCount;


    private User user;
}