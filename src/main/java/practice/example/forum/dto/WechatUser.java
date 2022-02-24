package practice.example.forum.dto;

import lombok.Data;

/**
 * @Author jiang
 * @Created Project on 2022/2/22
 */
@Data
public class WechatUser {

    private String openid;
    private String nickname;
    private String sex;
    private String city;
    private String headimgurl;

}
