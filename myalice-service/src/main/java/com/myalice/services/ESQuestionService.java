package com.myalice.services;

import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Service;

import com.myalice.config.ElasticsearchProporties;
import com.myalice.domain.ElasticsearchData;
import com.myalice.es.impl.ElasticsearchService;

@Service
public class ESQuestionService {

	protected ElasticsearchService questionEsService;
	protected ElasticsearchService anwserEsService;

	public ESQuestionService(ElasticsearchProporties elasticsearchProporties) {
		questionEsService = new ElasticsearchService("myalice", "question");
		questionEsService.setElasticsearchProporties(elasticsearchProporties);

		anwserEsService = new ElasticsearchService("myalice", "anwser");
		anwserEsService.setElasticsearchProporties(elasticsearchProporties);
	}

	public void addQuestion(Map<String, Object> question, Map<String, Object> anwser) {
		questionEsService.add(question);
		anwser.put("question_id", question.get("id"));
		anwserEsService.add(anwser);
	}

	public boolean add(Map<String, Object> data) {
		return questionEsService.add(data);
	}

	public boolean remove(String id) {
		return questionEsService.remove(id);
	}

	public void query(ElasticsearchData searchData) {
		questionEsService.query(searchData);
	}

	public Map<String, Object> get(String id) {
		return questionEsService.get(id);
	}

	public List<Map<String, Object>> queryAnswer(QueryBuilder builder) {
		List<Map<String, Object>> datas = anwserEsService.queryList(builder) ;
		return datas ;
	}
}
