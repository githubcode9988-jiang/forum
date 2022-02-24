package practice.example.forum.dto;

import lombok.Data;

/**
 * @Author jiang
 * @Created Project on 2022/2/22
 */
@Data
public class WechatTokenInfoDTO {

    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
}
