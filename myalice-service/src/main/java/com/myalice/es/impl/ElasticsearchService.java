package com.myalice.es.impl;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.util.StringUtils;

import com.myalice.config.ElasticsearchProporties;
import com.myalice.domain.ElasticsearchData;
import com.myalice.es.IElasticsearch;
import com.myalice.utils.MyAliceUtils;
import com.myalice.utils.Tools;

public class ElasticsearchService implements IElasticsearch {

	protected String index;

	protected String type;

	protected ElasticsearchProporties elasticsearchProporties;

	public ElasticsearchService() {
	}

	public ElasticsearchService(String index, String type) {
		this.index = index;
		this.type = type;
	}

	@Override
	public boolean add(Map<String, Object> data) {
		TransportClient client = elasticsearchProporties.createTransportClient();
		String id = MyAliceUtils.toString(data.get("id"));
		if (StringUtils.isEmpty(id)) {
			id = Tools.uuid();
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
		TransportClient client = elasticsearchProporties.createTransportClient();
		GetResponse actionGet = client.prepareGet(index, type, id).execute().actionGet();

		return actionGet.getSource();
	}

	@Override
	public boolean remove(String id) {
		TransportClient client = elasticsearchProporties.createTransportClient();
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
		TransportClient client = elasticsearchProporties.createTransportClient();

		SearchRequestBuilder requestBuilder = client.prepareSearch(index).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setFrom(searchData.getFrom()).setSize(searchData.getSize());
		if (null != searchData.getBuilder()) {
			requestBuilder.setQuery(searchData.getBuilder());
		}
		SearchResponse response = requestBuilder.execute().actionGet();
		SearchHits hits = response.getHits();
		searchData.setDocCount(hits.getTotalHits());
		List<Map<String, Object>> docs = new Vector<>();
		for (SearchHit hit : hits.getHits()) {
			Map<String, Object> source = hit.getSource();
			source.put("id", hit.getId());
			docs.add(source);
		}
		searchData.setDocs(docs);
	}

	public String getIndex() {
		return index;
	}

	public ElasticsearchProporties getElasticsearchProporties() {
		return elasticsearchProporties;
	}

	public void setElasticsearchProporties(ElasticsearchProporties elasticsearchProporties) {
		this.elasticsearchProporties = elasticsearchProporties;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
