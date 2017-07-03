package com.myalice.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myalice.beans.CoolQMessage;
import com.myalice.beans.CoolQResponse;
import com.myalice.domain.TalkRecord;
import com.myalice.services.ESQuestionService;
import com.myalice.services.SysDictionariesService;
import com.myalice.services.TalkRecordService;
import com.myalice.utils.MyAliceUtils;
import com.myalice.utils.Tools;

@Service
public class CoolQMessageService {

	@Autowired
	protected ESQuestionService esQuestionService;

	@Autowired
	protected TalkRecordService talkRecordService;
	
	@Autowired
	protected SysDictionariesService dictionariesService ;

	public CoolQResponse getMessageType(CoolQMessage cqMessage) {
		String message = MyAliceUtils.trimQQ(cqMessage.getMessage());
		String[] qqs = MyAliceUtils.parseQqs(cqMessage.getMessage());
		qqs = dictionariesService.findQQ( qqs ) ;
		CoolQResponse response = new CoolQResponse();
		/* 如果没有AT其他QQ号，则是认为是提问 */
		if (ArrayUtils.isEmpty(qqs)) {
			cqMessage.setSearchData( searchAnswer(message, response) );
		} else {
			if (qqs.length > 1) {
				
				cqMessage.setSearchData( searchAnswer(message, response) );
			} else {
				TalkRecord talkRecord = talkRecordService.selectLastAsk(MyAliceUtils.toString(cqMessage.getGroup_id()),
						MyAliceUtils.toString(cqMessage.getUser_id()));
				if (null == talkRecord) {
					response.setReply("很抱歉，您AT的 [CQ:at,qq=" + qqs[0] + "] 没有提过未匹配的问题");
				} else {
					Map<String, Object> questionMap = new HashMap<>();
					questionMap.put("title", talkRecord.getContent());
					questionMap.put("state", 2);
					questionMap.put("questionType", 1);
					questionMap.put("create_user", "网友：" + qqs[0]);
					questionMap.put("create_date", Tools.currentDate());
					message = message.replaceAll("建议答案", "") ;  
					if(StringUtils.startsWithAny(message, "：" , ":")){
						message = message.substring(0) ;
					}
					Map<String,Object> anwserMap = new HashMap<>() ;
					anwserMap.put("anwser", message); 
					anwserMap.put("create_time", Tools.currentDate()); 
					anwserMap.put("source", 0 ) ;
					esQuestionService.addQuestion(questionMap, anwserMap) ; 
					response.setReply("非常感谢您的回答");
				}
			}
		}
		return response;
	}
	
	private boolean searchAnswer(String message, CoolQResponse response) {
		Map<String, Object> answer = esQuestionService.searchAnswer(message);
		if (null != answer) {
			String anwser = MyAliceUtils.toString(answer.get("anwser")) ;
			
			String user=StringUtils.equalsAny("1", MyAliceUtils.toString(answer.get("state"))) ? "官方"
					:MyAliceUtils.toString(answer.get("create_user")) ;
			
			response.setReply( anwser ) ;
			return true ;
		} else {
			response.setReply("很抱歉，我还不知道答案"); //，群里知道此问题答案的请 @机器猫 @提问者 建议答案：xxxxx
			return false ;
		}
	}
}