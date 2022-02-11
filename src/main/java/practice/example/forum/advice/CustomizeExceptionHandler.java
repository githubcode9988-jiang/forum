package practice.example.forum.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import practice.example.forum.dto.ResultDTO;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 *  请求到后台报错 处理上下文业务异常  springmvc给handler的异常
 *
 * @Author jiang
 * @Created Project on 2022/1/15
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public Object handle(Throwable ex, Model model,
//                         HttpServletRequest request,
//                         HttpServletResponse response) throws Exception {
//
//        String contentType = request.getContentType();
//
//        //不同的请求类型返回的数据格式不同
//        if("application/json".equals(contentType)){
//            // 返回json
//            ResultDTO resultDTO = new ResultDTO();
//            if(ex instanceof CustomizeException){
//                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
//            }else{
//                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
//            }
//            try{
//                response.setContentType("application/json");
//                response.setStatus(200);
//                response.setCharacterEncoding("utf-8");
//                PrintWriter writer = response.getWriter();
////                writer.write(JSON.toJSONString(resultDTO));
//                writer.close();
//            }catch(IOException e){
//
//            }
//            return null;
//        }else{
//            // 错误页面跳转
//            if(ex instanceof CustomizeException){
//                model.addAttribute("message",ex.getMessage());
//            }else{
//                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
//                throw new Exception(ex.getMessage());
//            }
//            return new ModelAndView("error");
//        }
//
//    }

}
