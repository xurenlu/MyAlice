package com.myalice.services;

import com.myalice.domain.Elasticsearch;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {

    @Autowired
    TransportClient client;

    public void createIndex(Elasticsearch es) {
        IndexResponse create = client.prepareIndex(es.getIndex(), es.getType(), es.getId()).setSource(es.getData()).execute().actionGet();
        System.out.println("创建:" + create.status());//如果存在 status == OK 否则  == CREATE
        client.close();
    }


}
