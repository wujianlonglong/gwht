package com.longer.utils.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by wujianlong on 2017/5/22.
 */
@Component
@Data
@ConfigurationProperties(prefix="fileload")
public class GwhtFileUrl {

    private String gwhtimgurl;

    private String gwimgurl;

    private String gwnewsrefreshurl;

    private String gwurl;
}
