package com.myalice.es.impl;

import java.util.List;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;

import com.myalice.domain.ElasticsearchData;
import com.myalice.es.IElasticsearch;

public class ElasticsearchService implements IElasticsearch {
	
	protected String index;
	
	protected String type;
	
	protected TransportClient client ;
	
	public ElasticsearchService(String index, String type) {
		this.index = index;
		this.type = type;
	}

	@Override
	public boolean add(Map<String, Object> data) {
		
		
		
		return false ;
	}

	@Override
	public boolean adds(List<Map<String, Object>> datas) {

		return false;
	}

	@Override
	public boolean remove(String index) {

		return false;
	}

	@Override
	public boolean removes(String... index) {

		return false;
	}

	@Override
	public void query(ElasticsearchData searchData) {

	}

}
