package practice.example.forum.utile;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author jiang
 * @Created Project on 2022/1/28
 */
@Data
@Component
public class FastConfig {


    @Value("${fdfs.resHost}")
    private String resHost;

    @Value("${fdfs.storagePort}")
    private String storagePort;


}
