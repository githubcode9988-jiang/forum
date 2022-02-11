package practice.example.forum.dto;

import lombok.Data;

/**
 * @Author jiang
 * @Created Project on 2022/2/1
 */
@Data
public class QuestionQueryDTO {

    private String search;
    private Integer offset;
    private Integer size;
}
