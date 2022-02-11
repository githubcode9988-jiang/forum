package practice.example.forum.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.example.forum.dto.CommentDTO;
import practice.example.forum.enums.CommentTypeEnum;
import practice.example.forum.enums.NotificationEnum;
import practice.example.forum.enums.NotificationTypeEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;
import practice.example.forum.mapper.*;
import practice.example.forum.model.*;
import practice.example.forum.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author jiang
 * @Created Project on 2022/1/16
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void insert(Comment comment,User commentator) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

            Question question = questionMapper.getById(dbComment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            // 创建通知
            createNotify(comment,dbComment.getCommentator(),commentator.getName(),question.getTitle(),NotificationEnum.REPLY_COMMENT, Long.valueOf(question.getId()));
        }else{
            //回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionMapper.incCommentCount(question);
            // 创建通知
            createNotify(comment,question.getCreator(),commentator.getName(),question.getTitle(), NotificationEnum.REPLY_QUESTION, Long.valueOf(question.getId()));
        }
    }

    @Override
    public List<CommentDTO> listByQuestionId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size() == 0){
            return new ArrayList<>();
        }

        //获取去重的评论人
        Set<String> commentors = comments.stream().map(comment -> String.valueOf(comment.getCommentator())).collect(Collectors.toSet());
        List<String> userAccountIds = new ArrayList();
        userAccountIds.addAll(commentors);

        // 获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdIn(userAccountIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getAccountId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(String.valueOf(comment.getCommentator())));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    @Override
    public List<CommentDTO> listByCommentId(Long id, CommentTypeEnum comment) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(comment.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size() == 0){
            return new ArrayList<>();
        }

        //获取去重评论人
        Set<String> commentor = comments.stream().map(comment1 -> String.valueOf(comment1.getCommentator())).collect(Collectors.toSet());
        ArrayList<String> usersId = new ArrayList<>();
        usersId.addAll(commentor);


        //获取评论人转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdIn(usersId);
        List<User> users = userMapper.selectByExample(userExample);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getAccountId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment1 -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment1, commentDTO);
            commentDTO.setUser(userMap.get(String.valueOf(comment1.getCommentator())));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    public void createNotify(Comment comment, Long receiver,
                             String notifierName, String outerTitle, NotificationEnum notificationType, Long outerId){
        if(receiver.equals(comment.getCommentator())){
            //回复人为自己时不需要创建通知
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationTypeEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }
}
