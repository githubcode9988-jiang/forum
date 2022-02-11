package practice.example.forum.enums;

/**
 * @Author jiang
 * @Created Project on 2022/1/16
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

    private Integer Type;

    public static boolean isExist(Integer type) {
        for(CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()){
            if(commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return Type;
    }

    CommentTypeEnum(Integer type) {
        Type = type;
    }
}
