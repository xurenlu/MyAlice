package com.myalice.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticsearchClient {

    @Value("${cluster-nodes}")
    private String cluster_nodes;
    @Value("${elasticsearch}")
    private String elasticsearch;
    @Value("${cluster-name}")
    private String cluster_name;
    @Value("${cluster-port}")
    private Integer cluster_port;
    @Value("${elasticsearch.enabled}")
    private boolean elasticsearchEnabled;

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        TransportClient client = null;
        if (elasticsearchEnabled) {
            Settings settings = Settings.builder().put(cluster_name, elasticsearch).build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(cluster_nodes), cluster_port));
        }
        return client;
    }


}
