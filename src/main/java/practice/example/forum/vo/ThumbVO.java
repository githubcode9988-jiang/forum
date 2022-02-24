package practice.example.forum.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author jiang
 * @Created Project on 2022/2/17
 */
@Data
public class ThumbVO {

        @NotNull(message = "目标id不允许为空")
        private Long  targetId;

        @NotNull(message = "目标类型不允许为空")
        private Integer type;
}
