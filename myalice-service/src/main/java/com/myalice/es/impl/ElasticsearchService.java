package com.myalice.es.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.util.StringUtils;

import com.myalice.domain.ElasticsearchData;
import com.myalice.es.IElasticsearch;
import com.myalice.utils.MyAliceStringUtils;

public class ElasticsearchService implements IElasticsearch {

	protected String index;

	protected String type;

	protected TransportClient client;

	public ElasticsearchService(TransportClient client ,String index, String type) {
		this.client = client ; 
		this.index = index;
		this.type = type;
	}

	@Override
	public boolean add(Map<String, Object> data) {
		String id = MyAliceStringUtils.toString(data.get("id"));
		if (StringUtils.isEmpty(id)) {
			throw new NullPointerException("data without id value");
		}
		IndexResponse actionGet = client.prepareIndex(index, type, id).setSource(data).execute().actionGet();
		return actionGet.status() == RestStatus.OK;
	}

	@Override
	public boolean adds(List<Map<String, Object>> datas) {
		datas.forEach(data -> add(data));
		return true;
	}

	@Override
	public Map<String, Object> get(String id) {
		GetResponse actionGet = client.prepareGet(index,type,id).execute().actionGet();
		
		return actionGet.getSource() ; 
	}
	
	@Override
	public boolean remove(String id) {
		DeleteResponse actionGet = client.prepareDelete(index, type, id).execute().actionGet();
		return actionGet.status() == RestStatus.OK;
	}

	@Override
	public boolean removes(String... ids) {
		Stream.of(ids).forEach(id -> remove(id));
		return true;
	}
	
	@Override
	public void query(ElasticsearchData searchData) {
		
	}
}
