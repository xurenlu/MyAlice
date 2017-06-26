package com.test;

import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.myalice.MyAliceSpringConfig;
import com.myalice.services.ESQuestionService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = MyAliceSpringConfig.class)
public class SpringTest {

	@Autowired
	ESQuestionService esQuestionService;

	@Test
	public void test() {
		String text = "Mycat问题";
		List<Map<String, Object>> searchAnswer = esQuestionService.getQuestionEsService()
				.queryList(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", text))
						.must(QueryBuilders.matchPhraseQuery("state", 1)));
		try {
			System.out.println(searchAnswer);
			List<Map<String, Object>> sort = esQuestionService.getQuestionEsService().sort(searchAnswer, text);
			System.out.println(sort); 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}