package com.myalice;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.myalice")
@MapperScan("com.myalice.mapping")
@AutoConfigureBefore(PageHelperAutoConfiguration.class)
public class MyAliceSpringConfig extends SpringBootServletInitializer {

	@Autowired
	DataSource dataSource;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MyAliceSpringConfig.class);
	}
	
	@Bean
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
}
