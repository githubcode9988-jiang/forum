package practice.example.forum.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *  处理请求异常
 *
 * @Author jiang
 * @Created Project on 2022/1/15
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@Log4j2
public class CustomizeErrorController implements ErrorController {


    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request,
                                  Model model) {
        HttpStatus status = this.getStatus(request);

//        if(status.is4xxClientError()){
//              model.addAttribute("message","你这个请求出错了,要不然换个姿势试试？");
//        }

        if(status.is5xxServerError()){
            log.error("CustomizeErrorController:请求异常");
            model.addAttribute("message","服务器冒烟了,要不然稍后试试试试？");
        }

        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

}
