package com.myalice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alice.elasticsearch")
public class ElasticsearchProporties {

	private String clusterNodes;
	private String elasticsearch;
	private String clusterName;
	private Integer clusterPort;

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
