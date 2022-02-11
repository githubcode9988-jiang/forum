package practice.example.forum.dto;

import lombok.Data;

/**
 * @Author jiang
 * @Created Project on 2022/1/8
 */
@Data
public class GiteeAccessTokenDTO {

    private String client_id;

    private String grant_type;

    private String client_secret;

    private String code;

    private String redirect_uri;

}
