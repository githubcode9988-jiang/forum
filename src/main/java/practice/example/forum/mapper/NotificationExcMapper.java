package practice.example.forum.mapper;

import practice.example.forum.enums.NotificationEnum;
import practice.example.forum.model.Notification;
import practice.example.forum.model.Question;

import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/24
 */
public interface NotificationExcMapper {

    /**
     *  查询回复问题，评论
     * @param userAccountId
     * @param offset
     * @param size
     * @return
     */
    List<Notification> findNotificationById(Integer userAccountId, Integer offset, Integer size,Integer questionType,Integer commentType);

    /**
     *   查询私信
     * @param userAccountId
     * @param offset
     * @param size
     * @param type
     * @return
     */
    List<Notification> findNotificationByLetter(Integer userAccountId, Integer offset, Integer size, Integer type);
}
