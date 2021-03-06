package practice.example.forum.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import practice.example.forum.dto.*;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.User;
import practice.example.forum.provider.GithubProvider;
import practice.example.forum.provider.WechatProvider;
import practice.example.forum.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 *
 */
@Controller
@Log4j2
public class AuthorizeController {

     @Autowired
     GithubProvider githubProvider;

     @Value("${github.client.id}")
     private String clientId;

     @Value("${github.client.secret}")
     private String clientSecret;

     @Value("${github.redirect.url}")
     private String redirectUrl;

     @Autowired
     WechatProvider wechatProvider;

     @Value("${wechat.client.id}")
     private String wechatId;

     @Value("${wechat.client.secret}")
     private String wechatSecret;

     @Value("${wechat.redirect.url}")
     private String wechatRedirectUrl;

     @Autowired
     private UserMapper userMapper;

     @Autowired
     private UserService userService;

     @GetMapping("/callback")
     public String callBack(@RequestParam(name = "code") String code,
                            @RequestParam(name = "state") String state,
                            HttpServletResponse response){
         GiteeAccessTokenDTO accessTokenDTO = new GiteeAccessTokenDTO();
         accessTokenDTO.setClient_id(clientId);
         accessTokenDTO.setClient_secret(clientSecret);
         accessTokenDTO.setRedirect_uri(redirectUrl);
         accessTokenDTO.setCode(code);
         accessTokenDTO.setGrant_type("authorization_code");
         String accessToken = githubProvider.getAccessToken(accessTokenDTO);
         GiteeUser giteeUser = githubProvider.getToke(accessToken);
         if(giteeUser != null){
             User user = new User();
             String token = UUID.randomUUID().toString();
             user.setToken(token);
             user.setName(giteeUser.getName());
             user.setAccountId(giteeUser.getId());
             user.setAvatarUrl(giteeUser.getAvatarUrl());
             userService.creatorOrUpdate(user);
             response.addCookie(new Cookie("token",token));
             return "redirect:/";
         }else{
             log.error("callBack get github error,{}",giteeUser);
             return "redirect:/";
         }
     }

     @GetMapping("/callbackweixin")
     public String callBackWeiXin(@RequestParam(name = "code") String code,
                                  @RequestParam(name = "state") String state,
                                  HttpServletResponse response){
         WeChatAccessTokenDTO weChatAccessTokenDTO = new WeChatAccessTokenDTO();
         weChatAccessTokenDTO.setCode(code);
         weChatAccessTokenDTO.setAppid(wechatId);
         weChatAccessTokenDTO.setSecret(wechatSecret);
         weChatAccessTokenDTO.setGrant_type("authorization_code");
         String token = wechatProvider.getAccessToken(weChatAccessTokenDTO);
         WechatUser user = wechatProvider.getToke(token, wechatId);
         if(user != null){

         }
         return "redirect:/";
     }


     @GetMapping("/logout")
     public String logout(HttpServletRequest request,HttpServletResponse response){
         request.getSession().removeAttribute("user");

         Cookie[] cookies = request.getCookies();
         for(int i = 0; i < cookies.length; i++){
             Cookie cookie = new Cookie(cookies[i].getName(),null);
             cookie.setMaxAge(0);
             response.addCookie(cookie);
         }

         return "redirect:/";
     }

     @GetMapping("/qrcode")
     public String qrcode(Model model){
        return "qrcode";
     }
}
