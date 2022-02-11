package practice.example.forum.service;

import practice.example.forum.dto.NotificationDTO;
import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.model.User;

/**
 * @Author jiang
 * @Created Project on 2022/1/24
 */
public interface NotificationService {
    PaginationDTO list(String accountId, Integer page, Integer size);

    Long unreadCount(String accountId);

    NotificationDTO read(String id, User user);

    /**
     *   邀请作者回复信息
     * @param user           该登录用户
     * @param inviteMessage  邀请回复信息
     * @param questionId     该话题id
     */
    void iniviteMessage(User user, String inviteMessage, String questionId);

    /**
     *  查询私信
     * @param accountId
     * @param page
     * @param size
     * @return
     */
    PaginationDTO listByLetter(String accountId, Integer page, Integer size);
}
