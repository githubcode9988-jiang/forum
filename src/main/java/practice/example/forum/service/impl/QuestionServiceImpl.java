package practice.example.forum.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.dto.QuestionDTO;
import practice.example.forum.dto.QuestionQueryDTO;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;
import practice.example.forum.mapper.QuestionMapper;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.Question;
import practice.example.forum.model.User;
import practice.example.forum.model.UserExample;
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

    /**
     *   点击index页问题进入问题详情
     * @param id
     * @return
     */
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
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }

    }

    @Override
    public void incView(String id) {
        //TODO 注意问题高并发情况下 阅读数会出现数据不一致
        Question record = new Question();
        record.setViewCount(1);
        record.setId(Integer.valueOf(id));
        questionMapper.updateToViewCount(record);
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
}