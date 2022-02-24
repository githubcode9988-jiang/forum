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

    /**
     *  回复问题，评论未读数
     * @param accountId
     * @return
     */
    Long unReadByReceiver(String accountId);

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

    /**
     *  回复私信未读数
     * @param accountId
     * @return
     */
    Long unReadByLetter(String accountId);
    /**
     *  查询私信
     * @param accountId
     * @param page
     * @param size
     * @return
     */
    PaginationDTO listByLikeCount(String accountId, Integer page, Integer size);

    /**
     *  查询关注
     * @param accountId
     * @param page
     * @param size
     * @return
     */
    PaginationDTO listByAttention(String accountId, Integer page, Integer size);

    /**
     *  通过用户id查询关注信息
     * @param accountId
     * @return
     */
    Long unReadByAttention(String accountId);

    /**
     *  查询点赞通知
     * @param accountId
     * @return
     */
    Long unReadByLike(String accountId);
}
