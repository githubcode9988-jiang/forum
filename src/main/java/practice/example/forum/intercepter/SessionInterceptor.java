package practice.example.forum.intercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import practice.example.forum.dto.PaginationDTO;
import practice.example.forum.mapper.UserMapper;
import practice.example.forum.model.User;
import practice.example.forum.model.UserExample;
import practice.example.forum.service.NotificationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author jiang
 * @Created Project on 2022/1/11
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        User user = null;
        if(cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if(users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0) );
                        Long unReadByReceiver = notificationService.unReadByReceiver(users.get(0).getAccountId());
                        Long unReadByLetter =  notificationService.unReadByLetter(users.get(0).getAccountId());
                        Long unReadByLike =  notificationService.unReadByLike(users.get(0).getAccountId());
                        Long unReadByAttention =  notificationService.unReadByAttention(users.get(0).getAccountId());
                        request.getSession().setAttribute("unreadReceive",unReadByReceiver);
                        request.getSession().setAttribute("unReadByLetter",unReadByLetter);
                        request.getSession().setAttribute("unReadByLike",unReadByLike);
                        request.getSession().setAttribute("unReadByAttention",unReadByAttention);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
