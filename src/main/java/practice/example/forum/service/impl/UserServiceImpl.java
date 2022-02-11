package practice.example.forum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.User;
import practice.example.forum.model.UserExample;
import practice.example.forum.service.UserService;

import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/12
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     *   创建/更新用户
     * @param user
     */
    @Override
    public void creatorOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(String.valueOf(user.getAccountId()));
        List<User> users = userMapper.selectByExample(userExample);

        if(users.size() == 0){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(user.getToken());
            updateUser.setName(user.getName());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            UserExample example = new UserExample();
            example.createCriteria()
                            .andAccountIdEqualTo(String.valueOf(dbUser.getAccountId()));
            int updated = userMapper.updateByExampleSelective(updateUser, example);
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }


    }
}
