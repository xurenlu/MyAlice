package com.myalice;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAliceSpringConfig {
	
	@Bean("dataSource")
	public DataSource getDataSource(){
		return DataSourceBuilder.create().build();
	}
	
	
	
}
