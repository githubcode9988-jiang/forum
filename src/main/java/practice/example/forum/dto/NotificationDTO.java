package practice.example.forum.dto;

import lombok.Data;
import practice.example.forum.model.User;

/**
 * @Author jiang
 * @Created Project on 2022/1/24
 */
@Data
public class NotificationDTO {

    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
    private String message;
}
