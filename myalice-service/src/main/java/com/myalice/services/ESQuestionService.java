package com.myalice.services;

import com.myalice.config.ElasticsearchProporties;
import com.myalice.domain.ElasticsearchData;
import com.myalice.es.impl.ElasticsearchService;
import com.myalice.utils.MyAliceUtils;
import com.myalice.utils.Tools;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ESQuestionService {

    protected ElasticsearchService questionEsService;
    protected ElasticsearchService anwserEsService;
    protected ElasticsearchService wordEsService;

    public ESQuestionService(ElasticsearchProporties elasticsearchProporties) {
        questionEsService = new ElasticsearchService("myalice", "question");
        questionEsService.setElasticsearchProporties(elasticsearchProporties);

        anwserEsService = new ElasticsearchService("myalice", "anwser");
        anwserEsService.setElasticsearchProporties(elasticsearchProporties);

        wordEsService = new ElasticsearchService("myalice", "word");
        wordEsService.setElasticsearchProporties(elasticsearchProporties);
    }

    public ElasticsearchService getWordEsService() {
        return wordEsService;
    }

    public void setWordEsService(ElasticsearchService wordEsService) {
        this.wordEsService = wordEsService;
    }

    public void addQuestion(Map<String, Object> question, Map<String, Object> anwser) {

        String id = MyAliceUtils.toString(question.get("id"));

        if (!StringUtils.isEmpty(id)) {
            List<Map<String, Object>> queryAnswer = queryAnswer(QueryBuilders.matchQuery("question_id", id));
            queryAnswer.forEach(v -> {
                String answerId = MyAliceUtils.toString(v.get("id"));
                anwserEsService.remove(answerId);
            });
        }

        questionEsService.add(question);
        anwser.put("question_id", question.get("id"));
        anwserEsService.add(anwser);
    }


    public void addQuestions(Map<String, Object> question, String[] anwsers) {

        String id = MyAliceUtils.toString(question.get("id"));

        if (!StringUtils.isEmpty(id)) {
            List<Map<String, Object>> queryAnswer = queryAnswer(QueryBuilders.matchQuery("question_id", id));
            queryAnswer.forEach(v -> {
                String answerId = MyAliceUtils.toString(v.get("id"));
                anwserEsService.remove(answerId);
            });
        }

        questionEsService.add(question);
        if (ArrayUtils.isNotEmpty(anwsers)) {
            for (String anwser : anwsers) {
                Map<String, Object> anwserMap = new HashMap<>();
                anwserMap.put("anwser", anwser);
                anwserMap.put("create_time", Tools.currentDate());
                anwserMap.put("source", 0);
                anwserMap.put("question_id", question.get("id"));
                anwserEsService.add(anwserMap);
            }
        }
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
        List<Map<String, Object>> datas = anwserEsService.queryList(builder);
        return datas;
    }

    public Map<String, Object> queryAnswerOne(String id) {
        List<Map<String, Object>> datas = anwserEsService.queryList(QueryBuilders.matchQuery("question_id", id));
        return CollectionUtils.isEmpty(datas) ? null : datas.get(0);
    }

    /**
     * @return Map<String, Object> anwser 答案内容 create_time 创建日期 question_id 问题id
     * id 答案id source 评分
     * @desc:根据问题搜索对应答案方法
     * @param: question
     * 问题内容
     */
    public Map<String, Object> searchAnswer(String question) {
        List<Map<String, Object>> datas = questionEsService.queryList(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title", question)).must(QueryBuilders.matchQuery("state", 1)));

        if (CollectionUtils.isEmpty(datas)) {
            datas = questionEsService.queryList(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("title", question)).must(QueryBuilders.matchQuery("state", 2)));
        }

        if (!CollectionUtils.isEmpty(datas)) {
            this.sort(datas, question, QuestionSortType.keyWordsCount);
            Map<String, Object> data = datas.get(0);
            String id = MyAliceUtils.toString(data.get("id"));
            List<Map<String, Object>> answers = queryAnswer(
                    QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("question_id", id)));

            if (!CollectionUtils.isEmpty(answers)) {
                Map<String, Object> map = answers.get(0);
                map.put("state", data.get("state"));
                map.put("create_user", data.get("create_user"));
                return map;
            }
        }
        return null;
    }

    public void removeStopWords(List<AnalyzeResponse.AnalyzeToken> tokens) {
        List<AnalyzeResponse.AnalyzeToken> stopTokens = new ArrayList<>();
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            List<Map<String, Object>> datas = wordEsService.queryList(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchPhraseQuery("text", token.getTerm())));
            if (!CollectionUtils.isEmpty(datas)) {
                stopTokens.add(token);
            }
        }
        tokens.removeAll(stopTokens);
    }

    public List<Map<String, Object>> sort(List<Map<String, Object>> datas, String text, QuestionSortType sortType) {
        if (sortType == null) {
            return datas;
        } else if (sortType.equals(QuestionSortType.allWordsCount)) {
            return questionEsService.sort(datas, text);
        } else if (sortType.equals(QuestionSortType.keyWordsCount)) {
            return keyWordsCount(datas, text);
        }
        return datas;
    }

    public List<Map<String, Object>> keyWordsCount(List<Map<String, Object>> datas, String text) {
        List<AnalyzeResponse.AnalyzeToken> textTokens = questionEsService.ik(text);
        removeStopWords(textTokens);
        for (Map<String, Object> data : datas) {
            String title = MyAliceUtils.toString(data.get("title"));
            List<AnalyzeResponse.AnalyzeToken> titleTokens = questionEsService.ik(title);
            int sameInt = 0;
            for (AnalyzeResponse.AnalyzeToken titleToken : titleTokens) {
                for (AnalyzeResponse.AnalyzeToken textToken : textTokens) {
                    if (MyAliceUtils.toString(titleToken.getTerm()).equalsIgnoreCase(textToken.getTerm())) {
                        sameInt++;
                    } else if (MyAliceUtils.toString(titleToken.getTerm()).indexOf(textToken.getTerm()) > -1) {
                        sameInt++;
                    }
                }
            }
            data.put("tokenSameCount", sameInt);
        }

        Collections.sort(datas, (v1, v2) -> {
            int tokenSameCount1 = NumberUtils.toInt(MyAliceUtils.toString(v1.get("tokenSameCount")));
            int tokenSameCount2 = NumberUtils.toInt(MyAliceUtils.toString(v2.get("tokenSameCount")));
            if (tokenSameCount1 == tokenSameCount2) {
                return 0;
            }
            if (tokenSameCount1 > tokenSameCount2) {
                return -1;
            }
            return 1;
        });
        return datas;
    }

    public ElasticsearchService getQuestionEsService() {
        return questionEsService;
    }

    public ElasticsearchService getAnwserEsService() {
        return anwserEsService;
    }


}

enum QuestionSortType {
    allWordsCount, keyWordsCount
}