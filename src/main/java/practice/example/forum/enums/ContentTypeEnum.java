package practice.example.forum.enums;

/**
 * @Author jiang
 * @Created Project on 2022/2/21
 */
public enum ContentTypeEnum {

    DIARY(1,"日记"),
    QUESTIONID(2,"diaryid");


    private Integer type;
    private String message;

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    ContentTypeEnum(Integer type,String message){
        this.type = type;
        this.message = message;
    }
}
