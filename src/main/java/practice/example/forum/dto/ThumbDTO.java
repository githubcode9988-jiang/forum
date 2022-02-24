package practice.example.forum.dto;

import lombok.Data;
import practice.example.forum.model.User;

/**
 * @Author jiang
 * @Created Project on 2022/2/17
 */
@Data
public class ThumbDTO {

    private Long id;
    private Long targetId;
    private Integer type;
    private User user;
    private Long gmtCreate;
    private Long gmtModified;


}
