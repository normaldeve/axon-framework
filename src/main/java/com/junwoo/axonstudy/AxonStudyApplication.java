package com.junwoo.axonstudy;

import com.junwoo.axonstudy.config.AxonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({AxonConfig.class})
@SpringBootApplication
public class AxonStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxonStudyApplication.class, args);
    }

}
