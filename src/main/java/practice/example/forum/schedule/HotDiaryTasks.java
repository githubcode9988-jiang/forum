package practice.example.forum.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 热门日记
 * @Author jiang
 * @Created Project on 2022/2/21
 */
@Component
@Slf4j
public class HotDiaryTasks {

    private static final SimpleDateFormat sf = new SimpleDateFormat("YYYY:mm:dd HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
//        log.info("The time is now {}", sf.format(System.currentTimeMillis()));
    }
}
