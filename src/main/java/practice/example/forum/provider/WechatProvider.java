package practice.example.forum.provider;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;
import practice.example.forum.dto.*;

import java.io.IOException;

/**
 * 微信登录
 * @Author jiang
 * @Created Project on 2022/2/22
 */
@Component
@Slf4j
public class WechatProvider {


    public String getAccessToken(WeChatAccessTokenDTO accessTokenDTO){
        MediaType JSONs = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String json = JSON.toJSONString(accessTokenDTO);
        RequestBody body = RequestBody.create(JSONs,json);
        Request request = new Request.Builder()
                .url("api.weixin.qq.com/sns/oauth2/access_token")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            WechatTokenInfoDTO wct = JSON.parseObject(res, WechatTokenInfoDTO.class);
            String token = wct.getAccess_token();
            log.info("Token Obtained by the user {}",token);
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public WechatUser getToke(String accessToke,String openId) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/userinfo?access_token="+accessToke+"&openid="+openId+"&lang="+"zh_CN")
                .build();
        try{
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            WechatUser wechatUser = JSON.parseObject(str, WechatUser.class);
            return wechatUser;
        }catch (IOException e){
            e.printStackTrace();
        }
        return new WechatUser();
    }

    @Data
    public static class wcDTO{
        private String access_token;
        private String expires_in;

    }

    public static String getToken(){
        String grant_type = "client_credential";
        String appid = "wx2cd4356622024b79";
        String secret = "4ca028b88be2b408afb4b97d3de9f495";
        OkHttpClient client = new OkHttpClient();
        String str = "";
        Request request =
                new Request.Builder().url("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret).build();
        try {
            Response response = client.newCall(request).execute();
            str = response.body().string();
            wcDTO wcDTO = JSON.parseObject(str, wcDTO.class);
            return wcDTO.getAccess_token();

        }catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }

    private final static String OPENID = "wx97efc5965e47b1db";

    public static WechatUser getUser(String token){
        OkHttpClient client = new OkHttpClient();
        WechatUser wechatUser = null;
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/userinfo?access_token="+token+"&openid="+OPENID+"&lang="+"zh_CN").build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            System.out.println(str);
            wechatUser = JSON.parseObject(str, WechatUser.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return wechatUser;
    }

    public static void main(String[] args) {

        String token = getToken();
        WechatUser user = getUser(token);
        System.out.println(user);
    }


}
