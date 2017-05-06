package com.myalice.config;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.NamedThreadLocal;

@ConfigurationProperties(prefix = "alice.elasticsearch")
public class ElasticsearchProporties {

	Logger logger = LoggerFactory.getLogger(ElasticsearchProporties.class);
	private String clusterNodes;
	private String elasticsearch;
	private String clusterName;
	private Integer clusterPort;

	protected static NamedThreadLocal<TransportClient> CLIENT_THREAD_LOCAL = new NamedThreadLocal<TransportClient>(
			"es_client");

	public TransportClient createTransportClient() {
		TransportClient transportClient = null;
		try {
			transportClient = CLIENT_THREAD_LOCAL.get();
			if (null == transportClient) {
				Settings settings = Settings.builder().put(getClusterName(), getElasticsearch()).build();
				transportClient = new PreBuiltTransportClient(settings);
				transportClient.addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(getClusterNodes()), getClusterPort()));
				CLIENT_THREAD_LOCAL.set(transportClient);
			}
			return transportClient;
		} catch (Exception e) {
			logger.error("createTransportClient had error:" + e.getMessage(), e);
		}
		return transportClient;
	}

	public TransportClient getTransportClient() {

		return CLIENT_THREAD_LOCAL.get();

	}

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public String getElasticsearch() {
		return elasticsearch;
	}

	public void setElasticsearch(String elasticsearch) {
		this.elasticsearch = elasticsearch;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public Integer getClusterPort() {
		return clusterPort;
	}

	public void setClusterPort(Integer clusterPort) {
		this.clusterPort = clusterPort;
	}

}
