package practice.example.forum.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  接受 拒绝 请求关注
 * @Author jiang
 * @Created Project on 2022/2/19
 */
public enum OperationFriendsRequestTypeEnum {
    QUERY(0,"查询是否关注"),
    ACCEPT(1,"接受"),
    IGNORE(2,"拒绝");


    private Integer type;
    private String message;

    OperationFriendsRequestTypeEnum(Integer type,String message){
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public static String getMsgKey(Integer type){
        List<OperationFriendsRequestTypeEnum> collect = Arrays.stream(OperationFriendsRequestTypeEnum.values()).
                filter(t -> t.getType() == type).collect(Collectors.toList());
        return collect.get(0).getMessage();
    }
}
