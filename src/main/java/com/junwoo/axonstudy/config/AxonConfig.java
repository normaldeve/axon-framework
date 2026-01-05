package com.junwoo.axonstudy.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Configuration
public class AxonConfig {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[]{
                "com.junwoo.**"
        });

        return xStream;
    }
}
