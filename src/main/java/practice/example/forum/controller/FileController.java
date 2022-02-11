package practice.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import practice.example.forum.dto.FileDTO;
import practice.example.forum.utile.FastClientUtile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author jiang
 * @Created Project on 2022/1/26
 */
@Controller
public class FileController {

    @Autowired
    FastClientUtile fastClientUtile;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {

       // 拿到前端数据流 fast上传文件
        MultipartRequest multipartRequest = (MultipartRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String path = fastClientUtile.uploadFile(file);
        System.out.println(path);

       FileDTO fileDTO = new FileDTO();
       fileDTO.setSuccess(1);
       fileDTO.setMessage("上传成功！");
       fileDTO.setUrl("/images/p4.webp");
        return fileDTO;
    }

}
