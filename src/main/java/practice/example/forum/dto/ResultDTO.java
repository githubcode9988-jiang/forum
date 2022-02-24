package practice.example.forum.dto;

import lombok.Data;
import practice.example.forum.enums.SearchFriendsEnum;
import practice.example.forum.exception.CustomizeErrorCode;
import practice.example.forum.exception.CustomizeException;

import javax.servlet.http.HttpSession;

/**
 * @Author jiang
 * @Created Project on 2022/1/16
 */
@Data
public class ResultDTO<T> {

    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e){
          return errorOf(e.getCode(),e.getMessage());
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode){
        return errorOf(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO errorOf(SearchFriendsEnum searchCode){
        return errorOf(searchCode.getStatus(),searchCode.getMessage());
    }

    public static ResultDTO errorOf(String message){
        return errorOf(500,message);
    }

    public static ResultDTO okOf(){
         ResultDTO resultDTO = new ResultDTO();
         resultDTO.setCode(200);
         resultDTO.setMessage("请求成功");
         return resultDTO;
    }

    public static ResultDTO okOf(Integer code){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
