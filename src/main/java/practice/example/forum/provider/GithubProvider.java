package practice.example.forum.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;
import practice.example.forum.dto.GiteeAccessTokenDTO;
import practice.example.forum.dto.GiteeTokenInfoDTO;
import practice.example.forum.dto.GiteeUser;
import practice.example.forum.dto.GithubUser;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author jiang
 * @Created Project on 2022/1/7
 */
@Component
public class GithubProvider {


     public String getAccessToken(GiteeAccessTokenDTO accessTokenDTO){
         MediaType JSONs = MediaType.get("application/json; charset=utf-8");

         OkHttpClient client = new OkHttpClient();
         String json = JSON.toJSONString(accessTokenDTO);
         RequestBody body = RequestBody.create(JSONs,json);
         Request request = new Request.Builder()
                 .url("https://gitee.com/oauth/token")
                 .post(body)
                 .build();
         try {
             Response response = client.newCall(request).execute();
             String res = response.body().string();
             GiteeTokenInfoDTO user = JSON.parseObject(res, GiteeTokenInfoDTO.class);
             System.out.println(user.getAccess_token());
             String token = user.getAccess_token();
             return token;
         }catch (Exception e){
            e.printStackTrace();
         }
         return "";
     }

     public GiteeUser getToke(String accessToke) {
         OkHttpClient client = new OkHttpClient();
         Request request = new Request.Builder()
                 .url("https://gitee.com/api/v5/user?access_token="+accessToke)
                 .build();
         try{
             Response response = client.newCall(request).execute();
             String str = response.body().string();
             GiteeUser giteeUser = JSON.parseObject(str, GiteeUser.class);
             return giteeUser;
         }catch (IOException e){
             e.printStackTrace();
         }
         return new GiteeUser();
     }

    public static void main(String[] args) {
        String str = "{\"access_token\":\"7aa11864a6685de0be16332f76a014dc\",\"token_type\":\"bearer\",\"expires_in\":86400,\"refresh_token\":\"f4e8a1a9ccac8e75b6c9a12554949f854f1ac49c2e6acbb96d176f8d233ba36d\",\"scope\":\"user_info\",\"created_at\":1641638506}\n";
        GiteeTokenInfoDTO user = JSON.parseObject(str, GiteeTokenInfoDTO.class);
        System.out.println(user.getAccess_token());
    }

}
