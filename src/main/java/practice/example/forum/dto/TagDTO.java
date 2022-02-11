package practice.example.forum.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/23
 */
@Data
public class TagDTO {

    private String categoryName;
    private List<String> Tags;

}
