package practice.example.forum.dto;

import lombok.Data;

/**
 * @Author jiang
 * @Created Project on 2022/1/8
 */
@Data
public class GiteeTokenInfoDTO {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;

}
