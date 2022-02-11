package practice.example.forum.enums;

/**
 * @Author jiang
 * @Created Project on 2022/1/24
 */
public enum NotificationTypeEnum {
    UNREAD(0),READ(1);

    private int status;


    public int getStatus() {
        return status;
    }

    NotificationTypeEnum(int status){
        this.status = status;
    }

}
