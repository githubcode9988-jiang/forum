package practice.example.forum.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import practice.example.forum.dto.ResultDTO;
import practice.example.forum.enums.OperationFriendsRequestTypeEnum;
import practice.example.forum.enums.SearchFriendsEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.service.ChatService;

/**
 *  沟通controller
 * @Author jiang
 * @Created Project on 2022/2/14
 */
@Controller
public class ChatController {

    @Autowired
    ChatService chatService;

    @ResponseBody
    @GetMapping("/attention")
    public ResultDTO attentionRequest(@RequestParam(name = "myAccountId") String myAccountId,
                                   @RequestParam(name = "acceptUserName") String acceptUserName,
                                   Model model){
         if(StringUtils.isBlank(myAccountId) || StringUtils.isBlank(acceptUserName)){
              return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
         }
         // 请求关注前置操作
         Integer status = chatService.priconditionSearchFriends(myAccountId,acceptUserName);
         if(status == SearchFriendsEnum.SUCCESS.getStatus()){
             return chatService.sendAttentionRequest(myAccountId, acceptUserName);
         }else{
             String msgByKey = SearchFriendsEnum.getMsgByKey(status);
             return ResultDTO.errorOf(status,msgByKey);
         }
    }

    @ResponseBody
    @GetMapping("/operFriendsRequest")
    public ResultDTO operFriendsRequest(String sendAccountId, String acceptAccountId,
                                        Integer operType,String notid){
           if(StringUtils.isBlank(OperationFriendsRequestTypeEnum.getMsgKey(operType))){
               return ResultDTO.errorOf("");
           }
           chatService.read(sendAccountId,notid);
           if(OperationFriendsRequestTypeEnum.ACCEPT.getType() == operType){
               //接受请求关注
               chatService.acceptFriendsRequest(sendAccountId,acceptAccountId);
           }else if(OperationFriendsRequestTypeEnum.IGNORE.getType() == operType){
               //拒绝请求关注
               return chatService.deleteFriendsRequest(sendAccountId,acceptAccountId);
           }

           return ResultDTO.okOf();
    }

    @ResponseBody
    @GetMapping("/isAttention")
    public ResultDTO isAttentionRequest(String sendAccountId,String acceptAccountId){
        boolean attention = chatService.isAttention(sendAccountId, acceptAccountId);
        if(attention){
            return ResultDTO.errorOf(SearchFriendsEnum.ATTENTION_USER_EXSIT);
        }else{
            return chatService.isIgnore(sendAccountId,acceptAccountId);
        }
    }
}
