package com.myalice.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        Settings settings = Settings.builder().put(cluster_name, elasticsearch).build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(cluster_nodes), cluster_port));
        return client;
    }








}
