package practice.example.forum.cache;

import practice.example.forum.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author jiang
 * @Created Project on 2022/1/23
 */
public class TagCache {

     public static List<TagDTO> get(){
         List<TagDTO> tagDTOS = new ArrayList<>();
         TagDTO tagDTO = new TagDTO();
         tagDTO.setCategoryName("开发语言");
         tagDTO.setTags(Arrays.asList("javascript","php","css"," html","html5","java","node.js","python","c++","c","golang","objective-c","typescript","shell","swift","c#","sass","ruby","bash","less","asp.net","lua","scala","coffeescript","actionscript","rust","erlang","perl"));
         tagDTOS.add(tagDTO);

         TagDTO freamwork = new TagDTO();
         freamwork.setCategoryName("平台框架");
         freamwork.setTags(Arrays.asList("laravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
         tagDTOS.add(freamwork);

         TagDTO server = new TagDTO();
         server.setCategoryName("服务器");
         server.setTags(Arrays.asList("linux","nginx","docker","apache","ubuntu","centos","缓存","tomcat","负载均衡","unix","hadoop","windows-server"));
         tagDTOS.add(server);

         TagDTO db = new TagDTO();
         db.setCategoryName("数据库");
         db.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle","nosql","memcached","sqlserver","postgresql","sqlite"));
         tagDTOS.add(db);

         TagDTO tool = new TagDTO();
         tool.setCategoryName("开发工具");
         tool.setTags(Arrays.asList("Git","github","visua-studio-code","vim","sublime-text","xcode","intellij-idea","eclipse","maven","ide","svn","visual-studio","atom","emacs","textmate","hg"));
         tagDTOS.add(tool);
         return tagDTOS;
     }

    public static String filterInvalid(String tag) {
        String[] split = tag.split(",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tagValue -> tagValue.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
