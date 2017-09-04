package com.longer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//@Configuration
@SpringBootApplication
@EnableScheduling
@EnableRedisHttpSession
public class GwhtnewApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GwhtnewApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(GwhtnewApplication.class, args);
	}


	/**
	 * 文件上传配置
	 * @return
	 */
//	@Bean
//	public MultipartConfigElement multipartConfigElement() {
//		MultipartConfigFactory factory = new MultipartConfigFactory();
//		//文件最大
//		factory.setMaxFileSize("102400KB"); //KB,MB
//		/// 设置总上传数据总大小
//		factory.setMaxRequestSize("1024000KB");
//		return factory.createMultipartConfig();
//	}
}
