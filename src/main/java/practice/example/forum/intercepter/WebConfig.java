package practice.example.forum.intercepter;

import com.mysql.cj.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author jiang
 * @Created Project on 2022/1/11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer  {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有方法
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }
}
