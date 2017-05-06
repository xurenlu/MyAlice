package com.myalice.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myalice.config.ElasticsearchProporties;

@Component
@Aspect
public class ElasticsearchAop {
    Logger logger = LoggerFactory.getLogger(ElasticsearchAop.class);
    

	@Autowired
	protected ElasticsearchProporties elasticsearchProporties;
   
    @After("execution(* com.myalice.es.IElasticsearch.*(..)) ")
    public void doAfter() throws Throwable {
    	TransportClient transportClient = elasticsearchProporties.getTransportClient(); 
    	if(null != transportClient ){
    		try {
    			System.out.println("close");
    			transportClient.close();
			} catch (Exception e) { 
			}
    	}
    }


}