package practice.example.forum.dto;

import lombok.Data;
import practice.example.forum.model.User;

/**
 * 展示列表问题
 * @Author jiang
 * @Created Project on 2022/1/9
 */
@Data
public class QuestionDTO {

    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private String creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
