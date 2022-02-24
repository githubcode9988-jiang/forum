package practice.example.forum.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 关注作者前置状态 枚举
 * @Author jiang
 * @Created Project on 2022/2/14
 */
public enum SearchFriendsEnum {
    SUCCESS(0,"成功！"),
    ATTENTION_MESSAGE_IS_NULL(1,"关注人信息出错！"),
    ATTENTION_USER_NOT_EXSIT(2,"关注的用户不存在！"),
    ATTENTION_USER_EXSIT(3,"用户以关注！"),
    NOT_YOURSELF(4,"不能关注自己！"),
    NOT_REPETITION_REQUEST(5,"不能重复请求！"),
    IGNORE_REQUEST(6,"拒绝关注！"),
    ALREADY_IGNORE(7,"已点击拒绝！"),
    NOT_IGNORE(8,"没有点击拒绝！");


    private Integer status;
    private String message;

    SearchFriendsEnum(Integer status,String message){
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static String getMsgByKey(Integer status){
        List<SearchFriendsEnum> res = Arrays.stream(SearchFriendsEnum.values())
                .filter(t -> t.getStatus() == status).collect(Collectors.toList());
        return res.get(0).getMessage();
    }
}
