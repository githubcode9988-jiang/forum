package practice.example.forum.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author jiang
 * @Created Project on 2022/2/22
 */
@Data
public class TopDiaryDTO {

    private Set title;
    private Set questionId;

    public  Map titleToquestionId(Set title,Set questionIds){
        Map<String,String> map = new HashMap<>();
        String[] split = title.toString().split(",");
        String[] split1 = questionIds.toString().split(",");
        for (int i = 0; i < split.length; i++) {
            map.put(split[i],split1[i]);
        }
        return map;
    }
}
