package practice.example.forum.dto;

import lombok.Data;

/**
 * @Author jiang
 * @Created Project on 2022/1/20
 */
@Data
public class CommentCreatorDTO {
    private Long parentId;

    private String content;

    private Integer type;
}
