package practice.example.forum.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 点赞类型 枚举
 * @Author jiang
 * @Created Project on 2022/2/16
 */
public enum LikeTypeEnum {
    NO_LIKE(0),
    DIARY(1);

    private Integer type;

    LikeTypeEnum(Integer type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static boolean isExist(Integer type){
        List<Boolean> collect = Arrays.stream(LikeTypeEnum.values()).map(t -> t.getType() == type).collect(Collectors.toList());
        return collect.size() == 0 ? false : true;
    }


}
