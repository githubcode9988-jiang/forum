package practice.example.forum.dto;

import lombok.Data;

/**
 * @Author jiang
 * @Created Project on 2022/2/22
 */
@Data
public class WeChatAccessTokenDTO {

      private String appid;

      private String secret;

      private String code;

      private String grant_type;
}
