package practice.example.forum.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import practice.example.forum.dto.ResultDTO;
import practice.example.forum.enums.NotificationEnum;
import practice.example.forum.enums.NotificationTypeEnum;
import practice.example.forum.enums.SearchFriendsEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;
import practice.example.forum.mapper.FriendsRequestMapper;
import practice.example.forum.mapper.MyFriendsMapper;
import practice.example.forum.mapper.NotificationMapper;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.*;
import practice.example.forum.service.ChatService;

import java.util.List;
import java.util.Objects;

/**
 * @Author jiang
 * @Created Project on 2022/2/14
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    MyFriendsMapper myFriendsMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    FriendsRequestMapper friendsRequestMapper;

    @Autowired
    NotificationMapper notificationMapper;



    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer priconditionSearchFriends(String myAccountId, String acceptUserName) {
        List<User> users = queryUserByAcceptName(acceptUserName);
        //关注的用户不存在
        if(users.get(0) == null){
            return SearchFriendsEnum.ATTENTION_USER_NOT_EXSIT.getStatus();
        }
        //不能添加自己为好友
        if(myAccountId == users.get(0).getAccountId()){
            return SearchFriendsEnum.NOT_YOURSELF.getStatus();
        }
        //如果已经关注则退出
        MyFriendsExample mfExample = new MyFriendsExample();
        mfExample.createCriteria().andMyUserIdEqualTo(myAccountId)
                .andMyFriendUserIdEqualTo(users.get(0).getAccountId());
        List<MyFriends> myFriends = myFriendsMapper.selectByExample(mfExample);
        if(myFriends.size() != 0){
            return SearchFriendsEnum.ATTENTION_USER_EXSIT.getStatus();
        }
        //如果请求表已经有相同数据则返回
        FriendsRequestExample frExample = new FriendsRequestExample();
        frExample.createCriteria().andSendUserIdEqualTo(myAccountId)
                .andAcceptUserIdEqualTo(users.get(0).getAccountId());
        List<FriendsRequest> friendsRequests = friendsRequestMapper.selectByExample(frExample);
        if(friendsRequests.size() != 0){
            return SearchFriendsEnum.NOT_REPETITION_REQUEST.getStatus();
        }
        return SearchFriendsEnum.SUCCESS.getStatus();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultDTO sendAttentionRequest(String myAccountId, String acceptUserName) {
        List<User> users = queryUserByAcceptName(acceptUserName);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(myAccountId);
        List<User> myUsers = userMapper.selectByExample(userExample);
        FriendsRequestExample frExample = new FriendsRequestExample();
        frExample.createCriteria().andSendUserIdEqualTo(myAccountId)
                .andAcceptUserIdEqualTo(users.get(0).getAccountId());
        List<FriendsRequest> friendsRequests = friendsRequestMapper.selectByExample(frExample);

        if(myAccountId.equals(users.get(0).getAccountId())){
            return ResultDTO.errorOf(SearchFriendsEnum.NOT_YOURSELF);
        }

        //如果请求关注表中没有数据则加入表中
        if(friendsRequests.size() == 0){
            FriendsRequest friendsRequest = new FriendsRequest();
            friendsRequest.setSendUserId(myAccountId);
            friendsRequest.setAcceptUserId(users.get(0).getAccountId());
            friendsRequest.setRequestDataTime(System.currentTimeMillis());
            friendsRequestMapper.insert(friendsRequest);
        }
        //创建通知
        createNotify(myAccountId,Long.valueOf(users.get(0).getAccountId()),myUsers.get(0).getName(),
                myUsers.get(0).getName(), NotificationEnum.ATTENTION_USER, Long.valueOf(myAccountId));
        //如果已经有通知则取消已有的通知
        NotificationExample ne = new NotificationExample();
        ne.createCriteria().andNotifierEqualTo(Long.valueOf(myAccountId))
                .andReceiverEqualTo(Long.valueOf(users.get(0).getAccountId()))
                .andTypeEqualTo(NotificationEnum.ATTENTION_USER.getType());
        List<Notification> notifications = notificationMapper.selectByExample(ne);
        if(notifications.size() > 1){
            notificationMapper.deleteByPrimaryKey(notifications.get(0).getId());
        }
        return ResultDTO.okOf();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void acceptFriendsRequest(String sendAccountId, String acceptAccountid) {
        saveFriendsRequest(acceptAccountid,sendAccountId);
        saveFriendsRequest(sendAccountId,acceptAccountid);

        //删除请求关注记录
        dropFriendsRequest(sendAccountId,acceptAccountid);

        //TODO 可以使用实时通知用户


    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultDTO deleteFriendsRequest(String sendAccountId, String acceptAccountid) {
           dropFriendsRequest(sendAccountId,acceptAccountid);
           // webscoket 实时通知用户
           return ResultDTO.okOf(SearchFriendsEnum.IGNORE_REQUEST);
    }

    @Override
    public boolean isAttention(String sendAccountId, String acceptAccountId) {
        MyFriendsExample mf = new MyFriendsExample();
        mf.createCriteria().andMyFriendUserIdEqualTo(sendAccountId).andMyUserIdEqualTo(acceptAccountId);
        List<MyFriends> myFriends = myFriendsMapper.selectByExample(mf);
        return myFriends.size() == 0 ? false : true;
    }

    @Override
    public ResultDTO isIgnore(String sendAccountId, String acceptAccountId) {
        FriendsRequestExample fr = new FriendsRequestExample();
        fr.createCriteria().andSendUserIdEqualTo(sendAccountId).andAcceptUserIdEqualTo(acceptAccountId);
        List<FriendsRequest> friendsRequests = friendsRequestMapper.selectByExample(fr);
        if(friendsRequests.size() == 0){
            return ResultDTO.errorOf(SearchFriendsEnum.ALREADY_IGNORE);
        }else{
            return ResultDTO.errorOf(SearchFriendsEnum.NOT_IGNORE);
        }
    }

    @Override
    public void read(String sendAccountId, String  notid) {
        Notification notification = notificationMapper.selectByPrimaryKey(Long.valueOf(notid));
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notification.setStatus(NotificationTypeEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> queryUserByAcceptName(String acceptUserName){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(acceptUserName);
        return userMapper.selectByExample(userExample);
    }

    public void createNotify(String myAccountId, Long receiver,
                             String notifierName, String outerTitle, NotificationEnum notificationType, Long outerId){
        if(receiver.equals(myAccountId)){
            //回复人为自己时不需要创建通知
            return;
        }

        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(Long.valueOf(myAccountId));
        notification.setStatus(NotificationTypeEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public void saveFriendsRequest(String sendId,String acceptId){
            MyFriends myFriends = new MyFriends();
            myFriends.setMyUserId(acceptId);
            myFriends.setMyFriendUserId(sendId);
            myFriendsMapper.insert(myFriends);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void dropFriendsRequest(String sendAccountId, String acceptAccountid) {
           FriendsRequestExample fre = new FriendsRequestExample();
           fre.createCriteria().andSendUserIdEqualTo(sendAccountId).andAcceptUserIdEqualTo(acceptAccountid);
           friendsRequestMapper.deleteByExample(fre);
    }
}
