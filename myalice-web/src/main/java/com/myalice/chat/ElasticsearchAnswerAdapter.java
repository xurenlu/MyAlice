package com.myalice.chat;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import com.myalice.beans.CoolQMessage;
import com.myalice.beans.CoolQResponse;
import com.myalice.services.ESQuestionService;
import com.myalice.utils.MyAliceUtils;

public class ElasticsearchAnswerAdapter extends ChatAdapter {

	protected ApplicationContext context;

	public ElasticsearchAnswerAdapter(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public boolean isThisChat(String message, String[] qqs) {
		
		return true;
	}

	@Override
	public CoolQResponse chat(CoolQMessage message) {
		CoolQResponse response = new CoolQResponse();
		response.setAt_sender(true);

		if (StringUtils.isEmpty(message.getMessage())) {
			response.setReply("有什么可以帮助你的?");
			return response;
		}
		ESQuestionService esQuestionService = context.getBean(ESQuestionService.class);
		String messageStr = MyAliceUtils.trimQQ(message.getMessage()).trim();
		Map<String, Object> answer = esQuestionService.searchAnswer(messageStr);
		if (null != answer) {
			String anwser = MyAliceUtils.toString(answer.get("anwser"));

			String user = StringUtils.equalsAny("1", MyAliceUtils.toString(answer.get("state"))) ? "MyCat官方"
					: MyAliceUtils.toString(answer.get("create_user") + " 仅供参考");

			response.setReply(anwser + " 来源：" + user);
			return response;
		} else {
			response.setReply("很抱歉，我还不知道答案 群里知道此问题答案的请 @机器猫 @提问者 建议答案：xxxxx");
			return response;
		}
	}
	
}
