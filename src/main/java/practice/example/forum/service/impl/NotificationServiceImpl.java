package practice.example.forum.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.example.forum.dto.NotificationDTO;
import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.enums.NotificationEnum;
import practice.example.forum.enums.NotificationTypeEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;
import practice.example.forum.mapper.NotificationExcMapper;
import practice.example.forum.mapper.NotificationMapper;
import practice.example.forum.mapper.QuestionMapper;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.*;
import practice.example.forum.service.NotificationService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author jiang
 * @Created Project on 2022/1/24
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationExcMapper notificationExcMapper;

    @Autowired
    NotificationMapper notificationMapper;
    
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public PaginationDTO list(String accountId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;

        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(Long.valueOf(accountId));
        Integer total = (int)notificationMapper.countByExample(example);
        if(total % size == 0){
            totalPage = total / size;
        }else{
            totalPage = total / size + 1;
        }
        if(page <= 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        //分页 size*(page-1),size
        Integer offset = size * (page-1);
        if(offset < 0){
            offset = 1;
        }
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.setOrderByClause("gmt_create desc");
        notificationExample.createCriteria().andReceiverEqualTo(Long.valueOf(accountId));
        List<Notification> listNotification = notificationExcMapper.
                findNotificationById(Integer.valueOf(accountId),offset,size,NotificationEnum.REPLY_COMMENT.getType(),NotificationEnum.REPLY_QUESTION.getType());

        if(listNotification.size() == 0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : listNotification) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationEnum.nameOfType(NotificationEnum.REPLY_COMMENT.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    @Override
    public Long unReadByReceiver(String accountId) {
       return  notificationExcMapper.unReadByReceiver(Integer.valueOf(accountId),NotificationTypeEnum.UNREAD.getStatus(),
                                                NotificationEnum.REPLY_COMMENT.getType(),
                                                NotificationEnum.REPLY_QUESTION.getType());
    }


    @Override
    public Long unReadByLetter(String accountId) {
        return  notificationExcMapper.unReadByLetter(Integer.valueOf(accountId),NotificationTypeEnum.UNREAD.getStatus(),
                NotificationEnum.INVITE_MESSAGE.getType());
    }

    @Override
    public NotificationDTO read(String id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(Long.valueOf(id));
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(String.valueOf(notification.getReceiver()),user.getAccountId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FALL);
        }

        notification.setStatus(NotificationTypeEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

    @Override
    public void iniviteMessage(User user, String inviteMessage, String questionId) {
        Question question = questionMapper.getById(Long.valueOf(questionId));
        Notification notification = new Notification();
        notification.setNotifier(Long.valueOf(user.getAccountId()));
        notification.setReceiver(question.getCreator());
        notification.setOuterTitle(question.getTitle());
        notification.setNotifierName(user.getName());
        notification.setOuterid(Long.valueOf(questionId));
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setMessage(inviteMessage);
        notification.setType(NotificationEnum.INVITE_MESSAGE.getType());
        notification.setStatus(NotificationTypeEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    @Override
    public PaginationDTO listByLetter(String accountId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;

        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(Long.valueOf(accountId))
                                .andTypeEqualTo(NotificationEnum.INVITE_MESSAGE.getType());
        Integer total = (int)notificationMapper.countByExample(example);
        if(total % size == 0){
            totalPage = total / size;
        }else{
            totalPage = total / size + 1;
        }
        if(page <= 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        //分页 size*(page-1),size
        Integer offset = size * (page-1);
        if(offset < 0){
            offset = 1;
        }

        List<Notification> listNotification = notificationExcMapper.
                findNotificationByLetter(Integer.valueOf(accountId),
                                         offset,
                                         size,NotificationEnum.INVITE_MESSAGE.getType());

        if(listNotification.size() == 0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : listNotification) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationEnum.nameOfType(NotificationEnum.INVITE_MESSAGE.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

}
