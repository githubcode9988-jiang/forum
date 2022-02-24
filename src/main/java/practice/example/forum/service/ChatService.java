package practice.example.forum.service;

import org.springframework.stereotype.Service;
import practice.example.forum.dto.ResultDTO;

/**
 * @Author jiang
 * @Created Project on 2022/2/14
 */
public interface ChatService {
    /**
     *  搜索好友前置条件
     * @param myAccountId
     * @param acceptUserName
     * @return
     */
    Integer priconditionSearchFriends(String myAccountId, String acceptUserName);

    /**
     *  发送关注请求
     * @param myAccountId
     * @param acceptUserName
     */
    ResultDTO sendAttentionRequest(String myAccountId, String acceptUserName);

    /**
     *  同意请求关注
     * @param sendAccountId
     * @param acceptAccountid
     */
    void acceptFriendsRequest(String sendAccountId, String acceptAccountid);

    /**
     *  拒绝请求关注
     * @param sendAccountId
     * @param acceptAccountid
     */
    ResultDTO deleteFriendsRequest(String sendAccountId, String acceptAccountid);

    /**
     *  是否已关注
     * @param sendAccountId
     * @param acceptAccountId
     * @return
     */
    boolean isAttention(String sendAccountId, String acceptAccountId);

    /**
     *  是否点击拒绝
     * @param sendAccountId
     * @param acceptAccountId
     * @return
     */
    ResultDTO isIgnore(String sendAccountId, String acceptAccountId);

    /**
     *  已读通知信息
     * @param sendAccountId
     * @param notid
     */
    void read(String sendAccountId, String notid);
}
