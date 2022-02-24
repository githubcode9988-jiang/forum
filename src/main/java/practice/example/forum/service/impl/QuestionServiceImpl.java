package practice.example.forum.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import practice.example.forum.dto.*;
import practice.example.forum.enums.ContentTypeEnum;
import practice.example.forum.enums.LikeTypeEnum;
import practice.example.forum.enums.NotificationEnum;
import practice.example.forum.enums.NotificationTypeEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;
import practice.example.forum.mapper.NotificationMapper;
import practice.example.forum.mapper.QuestionMapper;
import practice.example.forum.mapper.ThumbMapper;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.*;
import practice.example.forum.service.QuestionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author jiang
 * @Created Project on 2022/1/9
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ThumbMapper thumbMapper;

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public PaginationDTO findListQuestion(String search,Integer page, Integer size) {

        if(StringUtils.isNotBlank(search)){
            String[] split = search.split(" ");
            search = Arrays.stream(split).collect(Collectors.joining("|"));
        }


        //分页 size*(page-1),size
        Integer offset = size * (page-1);
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setOffset(offset);
        questionQueryDTO.setSize(size);
        List<Question> listQuestion = questionMapper.findListQuestion(questionQueryDTO);

        List<QuestionDTO> res = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : listQuestion) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(example);
            QuestionDTO queryDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,queryDTO);
            queryDTO.setUser(users.get(0));
            res.add(queryDTO);
        }

        Integer total = questionMapper.getQuestionCount(search);
        paginationDTO.setData(res);
        Integer totalPage;

        if(total % size == 0){
              totalPage = total / size;
        }else{
              totalPage = total / size + 1;
        }

        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        return paginationDTO;
    }

    @Override
    public PaginationDTO findQuestionById(Integer userAccountId, Integer page, Integer size) {

        //分页 size*(page-1),size
        Integer offset = size * (page-1);
        if(offset < 0){
            offset = 1;
        }
        List<Question> listQuestion = questionMapper.findQuestionById(userAccountId,offset,size);

        List<QuestionDTO> res = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : listQuestion) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDTO queryDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,queryDTO);
            queryDTO.setUser(users.get(0));
            res.add(queryDTO);
        }

        Integer total = questionMapper.getQuestionCountById(userAccountId);
        paginationDTO.setData(res);

        Integer totalPage;

        if(total % size == 0){
            totalPage = total / size;
        }else{
            totalPage = total / size + 1;
        }

        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        return paginationDTO;
    }

    @Override
    public QuestionDTO getById(String id) {
        Question question = questionMapper.getById(Long.valueOf(id));
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO queryDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,queryDTO);
        queryDTO.setCreator(String.valueOf(question.getCreator()));
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
        List<User> users = userMapper.selectByExample(example);
        queryDTO.setUser(users.get(0));
        return queryDTO;
    }

    @Override
    public void createOrUpdate(Question question) {
        if(question.getId() == null){
             //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.create(question);
            //对应数据加入缓存
            redisTemplate.opsForZSet().add(ContentTypeEnum.DIARY.getMessage(),question.getTitle(),question.getViewCount());
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }

    }

    @Override
    public void incView(QuestionDTO questionDTO) {
        //TODO 注意问题高并发情况下 阅读数会出现数据不一致
        Question record = new Question();
        record.setViewCount(1);
        record.setId(Integer.valueOf(questionDTO.getId()));
        questionMapper.updateToViewCount(record);
        //缓存中浏览量加1
        redisTemplate.opsForZSet().incrementScore(
                ContentTypeEnum.DIARY.getMessage(),questionDTO.getTitle(),1);
    }

    @Override
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setTag(regexTag);
        question.setId(queryDTO.getId());
        List<Question> questions = questionMapper.selectRelated(question);
        List<QuestionDTO> queryDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return queryDTOS;
    }

    @Override
    public PaginationDTO findListQuestionByTag(String tag, Integer page, Integer size) {

        //分页 size*(page-1),size
        Integer offset = size * (page-1);
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(tag);
        questionQueryDTO.setOffset(offset);
        questionQueryDTO.setSize(size);
        List<Question> listQuestion = questionMapper.findListQuestionByTag(questionQueryDTO);

        List<QuestionDTO> res = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : listQuestion) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(example);
            QuestionDTO queryDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,queryDTO);
            queryDTO.setUser(users.get(0));
            res.add(queryDTO);
        }

        Integer total = questionMapper.getQuestionCountByTag(tag);
        paginationDTO.setData(res);
        Integer totalPage;

        if(total % size == 0){
            totalPage = total / size;
        }else{
            totalPage = total / size + 1;
        }

        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        return paginationDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultDTO incLikeCount(ThumbDTO thumbDTO) {
        //TODO 注意问题高并发情况下 点赞数会出现数据不一致
        Question question = questionMapper.getById(thumbDTO.getTargetId());
        if(thumbDTO.getUser() == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if(thumbDTO.getType() == null || !LikeTypeEnum.isExist(thumbDTO.getType())){
            throw new CustomizeException(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        if(thumbDTO.getType() == LikeTypeEnum.NO_LIKE.getType()){
            return ResultDTO.okOf(CustomizeErrorCode.CAN_NOT_LIKE_AGAIN.getCode());
        }
        //点赞日记
        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria().andTargetIdEqualTo(thumbDTO.getTargetId())
                                     .andTypeEqualTo(thumbDTO.getType())
                .andLikerEqualTo(Long.valueOf(thumbDTO.getUser().getAccountId()));
        List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
        //查询点赞表中是否已经有记录
        if(thumbs.size() != 0){
           return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE_AGAIN);
        }
        //插入记录
        Thumb thumb = new Thumb();
        BeanUtils.copyProperties(thumbDTO,thumb);
        thumb.setTargetId(thumbDTO.getTargetId());
        thumb.setLiker(Long.valueOf(thumbDTO.getUser().getAccountId()));
        thumbMapper.insert(thumb);
        //增加点赞
        if(thumb.getType() == LikeTypeEnum.DIARY.getType())
        question.setLikeCount(1);
        question.setId(Integer.valueOf(thumb.getTargetId().toString()));
        questionMapper.updateToLikeCount(question);
        //创建通知
        createNotify(thumb,question.getCreator(),thumbDTO.getUser().getName(),question.getTitle(), NotificationEnum.LIKE_SUCCESS, Long.valueOf(question.getId()));
        return ResultDTO.okOf();
    }

    @Override
    public ResultDTO isLikeCount(ThumbDTO thumbDTO) {
        if(thumbDTO.getUser() == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if(thumbDTO.getType() == null || !LikeTypeEnum.isExist(thumbDTO.getType())){
            throw new CustomizeException(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        if(thumbDTO.getType() == LikeTypeEnum.NO_LIKE.getType()){
            return ResultDTO.okOf(CustomizeErrorCode.CAN_NOT_LIKE_AGAIN.getCode());
        }
        //点赞日记
        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria().andTargetIdEqualTo(thumbDTO.getTargetId())
                .andTypeEqualTo(thumbDTO.getType())
                .andLikerEqualTo(Long.valueOf(thumbDTO.getUser().getAccountId()));
        List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
        //查询点赞表中是否已经有记录
        if(thumbs.size() != 0){
            return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE_AGAIN);
        }else{
            return ResultDTO.okOf();
        }
    }

    public void createNotify(Thumb thumb, Long receiver,
                             String notifierName, String outerTitle, NotificationEnum notificationType, Long outerId){
        if(receiver.equals(thumb.getLiker())){
            //回复人为自己时不需要创建通知
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(thumb.getLiker());
        notification.setStatus(NotificationTypeEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }
}