package com.myalice.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myalice.domain.ElasticsearchData;
import com.myalice.services.ESQuestionService;
import com.myalice.utils.MyAliceUtils;

@RequestMapping("/admin/question")
public class AdminQuestionCtrl {

	@Autowired
	protected ESQuestionService esQuestionService;

	@RequestMapping("/list")
	public ElasticsearchData list(HttpServletRequest request) {
		String title = MyAliceUtils.toString(request.getParameter("title"));
		int pageId = MyAliceUtils.toInt(request.getParameter("pageId")) ;
		ElasticsearchData searchData = new ElasticsearchData();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (!StringUtils.isEmpty(title)) {
			queryBuilder.must(QueryBuilders.matchPhraseQuery("title", title));
		}
		searchData.setPageId(pageId);
		searchData.setSize(10);
		esQuestionService.query(searchData);
		return searchData;
	}

}