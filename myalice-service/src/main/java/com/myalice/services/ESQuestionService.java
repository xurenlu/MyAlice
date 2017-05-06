package com.myalice.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.myalice.es.IElasticsearch;

@Service
public class ESQuestionService {
	
	@Resource(name="questionEs")
	IElasticsearch questionEs ;
	
}
