package practice.example.forum.exception;

/**
 * @Author jiang
 * @Created Project on 2022/1/15
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不在了,要不要换个试试?"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复."),
    NO_LOGIN(2003,"当前操作需要登录,请登录后重试."),
    SYS_ERROR(2004,"服务冒烟了，要不然你稍后再试试！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006,"回复评论不存在！"),
    CONTENT_IS_EMPTY(2007,"内容为空！"),
    PARAMETER_IS_NULL(2008,"参数为Null!"),
    READ_NOTIFICATION_FALL(2009,"兄弟，你这是读别人的信息呢！"),
    NOTIFICATION_NOT_FOUND(2010,"消息莫非不翼而飞了！");

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code,String message){
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
