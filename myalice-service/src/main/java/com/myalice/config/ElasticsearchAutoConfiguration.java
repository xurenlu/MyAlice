package com.myalice.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableConfigurationProperties(value = ElasticsearchProporties.class)
@ConditionalOnProperty(name = "alice.elasticsearch.enabled")
public class ElasticsearchAutoConfiguration {

    @Autowired
    private ElasticsearchProporties elasticsearchProporties;

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        Settings settings = Settings.builder().put(elasticsearchProporties.getClusterName(), elasticsearchProporties.getElasticsearch()).build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticsearchProporties.getClusterNodes()), elasticsearchProporties.getClusterPort()));
        return client;
    }


}
