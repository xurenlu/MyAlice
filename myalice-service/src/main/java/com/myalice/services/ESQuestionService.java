package com.myalice.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.myalice.config.ElasticsearchProporties;
import com.myalice.domain.ElasticsearchData;
import com.myalice.es.impl.ElasticsearchService;

@Service
public class ESQuestionService {

	protected ElasticsearchProporties elasticsearchProporties;

	protected ElasticsearchService elasticsearchService;

	public ESQuestionService(ElasticsearchProporties elasticsearchProporties) {
		this.elasticsearchProporties = elasticsearchProporties;
		elasticsearchService = new ElasticsearchService("myalice", "question");
		elasticsearchService.setElasticsearchProporties(elasticsearchProporties);
	}

	public boolean add(Map<String, Object> data) {
		return elasticsearchService.add(data);
	}

	public boolean adds(List<Map<String, Object>> datas) {
		return elasticsearchService.adds(datas);
	}

	public boolean remove(String id) {
		return elasticsearchService.remove(id);
	}

	public boolean removes(String... ids) {
		return elasticsearchService.removes(ids);
	}

	public void query(ElasticsearchData searchData) {
		elasticsearchService.query(searchData);
	}

	public Map<String, Object> get(String id) {
		return elasticsearchService.get(id);
	}
}
