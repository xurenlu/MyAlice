package com.test;

import com.myalice.MyAliceSpringConfig;
import com.myalice.domain.Elasticsearch;
import com.myalice.services.ElasticsearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = MyAliceSpringConfig.class )
public class ESTest2 {

    @Autowired
    ElasticsearchService service;

    @Test
    public void es(){
        String data = "{\"catIds\":[652076342,767412682,1536510176,642803755,103804452],\"desc\":\"中文490d5f09-88e9-4da1-911f-28bc3ec97bb4\",\"host\":\"10.0.0.1\",\"id\":3877169727794526009,\"subId\":278566723,\"systemName\":\"oa\"}\n";
        Elasticsearch elasticsearch = new Elasticsearch("index-1","1","alice",data);
        service.createIndex(elasticsearch);
    }


}
