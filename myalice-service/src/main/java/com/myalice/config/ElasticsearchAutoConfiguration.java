package com.myalice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myalice.es.IElasticsearch;
import com.myalice.es.impl.ElasticsearchService;

@Configuration
@EnableConfigurationProperties(value = ElasticsearchProporties.class)
@ConditionalOnProperty(name = "alice.elasticsearch.enabled")
public class ElasticsearchAutoConfiguration {

	@Autowired
	protected ElasticsearchProporties elasticsearchProporties;
	
	
	@Bean(name="questionEs")
	public IElasticsearch questionEs(){
		ElasticsearchService elasticsearchService = new ElasticsearchService("myalice", "question") ; 
		elasticsearchService.setElasticsearchProporties(elasticsearchProporties); 
		return elasticsearchService;
	}
	
	
	@Bean(name="answerEs")
	public IElasticsearch answerEs(){
		ElasticsearchService elasticsearchService = new ElasticsearchService( "myalice", "answer") ; 
		elasticsearchService.setElasticsearchProporties(elasticsearchProporties);
		return elasticsearchService;
	}
}
