package com.myalice.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import com.myalice.beans.CoolQMessage;
import com.myalice.beans.CoolQResponse;
import com.myalice.domain.TalkRecord;
import com.myalice.services.ESQuestionService;
import com.myalice.services.TalkRecordService;
import com.myalice.utils.MyAliceUtils;
import com.myalice.utils.Tools;

public class InputAnswerAdapter extends ChatAdapter {

	protected ApplicationContext context;

	protected String qq;

	public InputAnswerAdapter(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public boolean isThisChat(String message, String[] qqs) {
		if (ArrayUtils.isEmpty(qqs)) {
			return false;
		}
		qq = qqs[0];
		return qqs.length == 1;
	}

	@Override
	public CoolQResponse chat(CoolQMessage cqMessage) {
		TalkRecordService talkRecordService = context.getBean(TalkRecordService.class);
		
		
		
		CoolQResponse cqResponse = new CoolQResponse();
		ESQuestionService esQuestionService = context.getBean( ESQuestionService.class ) ;
		String message = MyAliceUtils.trimQQ(cqMessage.getMessage() ) ; 
		message = message.replaceAll("　", " ");
		message = message.trim() ;
		
		cqMessage.setAnwser( true );
		cqMessage.setSearchData( true );
		
		if(StringUtils.startsWith(message, "建议答案")){
			TalkRecord talkRecord = talkRecordService.selectLastAsk(MyAliceUtils.toString(cqMessage.getGroup_id()),qq , 0);
			if (null == talkRecord) {
				return null ; 
			}
			List<Map<String, Object>> datas = esQuestionService.getQuestionEsService().queryList(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("talkId", talkRecord.getId()))) ;
			if(CollectionUtils.isEmpty(datas)){
				Map<String, Object> questionMap = new HashMap<>();
				questionMap.put("title", talkRecord.getContent());
				questionMap.put("state", 2);
				questionMap.put("questionType", 1);
				questionMap.put("create_user", MyAliceUtils.toString(cqMessage.getUser_id()) );
				questionMap.put("talkId", talkRecord.getId());
				questionMap.put("create_date", Tools.currentDate());
				message = message.replaceAll("建议答案：", "") ;  
				message = message.replaceAll("建议答案:", "") ;
				message = message.replaceAll("建议答案", "") ;
				Map<String,Object> anwserMap = new HashMap<>() ;
				anwserMap.put("anwser", message); 
				anwserMap.put("create_time", Tools.currentDate()); 
				anwserMap.put("create_user", cqMessage.getUser_id()) ;
				anwserMap.put("source", 0 ) ;
				esQuestionService.addQuestion(questionMap, anwserMap) ; 
			}else{
				Map<String, Object> question = datas.get(0) ;
				Map<String,Object> anwserMap = new HashMap<>() ;
				message = message.replaceAll("建议答案：", "") ;  
				message = message.replaceAll("建议答案:", "") ;
				message = message.replaceAll("建议答案", "") ;
				if(StringUtils.startsWithAny(message, "：" , ":")){
					message = message.substring(1) ;
				}
				anwserMap.put("anwser", message); 
				anwserMap.put("create_time", Tools.currentDate()); 
				anwserMap.put("create_user", cqMessage.getUser_id()) ; 
				anwserMap.put("source", 0 ) ;
				anwserMap.put("question_id", question.get("id"));
				esQuestionService.getAnwserEsService().add( anwserMap ) ; 
			}
			cqResponse.setAt_sender( true );
			cqResponse.setReply("非常感谢您的回答");
		}else if(StringUtils.startsWith(message, "补充")){
			
		}else if(StringUtils.startsWith(message, "评分")){
			
		}
		return cqResponse;
	}

}
