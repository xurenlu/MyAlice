package com.myalice.ctrl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myalice.domain.QuestionOrder;
import com.myalice.properties.AttachmentProperties;
import com.myalice.services.QuestionOrderService;
import com.myalice.utils.ResponseMessageBody;

@RequestMapping("/admin/qr/")
@RestController
public class QuestionOrderCtrl {
	protected static Logger logger = org.slf4j.LoggerFactory.getLogger("QuestionRecord") ; 
	
	@Autowired
	protected AttachmentProperties attachmentProperties;
	
	@Autowired
	protected QuestionOrderService questionOrderService  ;
	
	@RequestMapping("upload")
	public ResponseMessageBody upload(@Valid QuestionOrder order ,BindingResult result , 
			 @RequestParam(value = "attachments") MultipartFile[] attachments){
		
		String headpath = attachmentProperties.getCurrentPath() ; 
		try {
			List<String>attachmentFile = new Vector<>();
			for(MultipartFile attachment:attachments){
				String fileName = attachmentProperties.getNewFileName(attachment.getOriginalFilename()) ; 
				File file = new File(headpath);
				file.mkdirs();
				FileOutputStream out = new FileOutputStream(headpath+"/" + fileName) ;
				String addFile = attachmentProperties.getCurrentDate() + "/" + fileName ;
				logger.debug("上传文件:" + addFile) ; 
				try {
					IOUtils.copy(attachment.getInputStream(), out) ;
					attachmentFile.add( addFile ) ; 
				} finally {
					IOUtils.closeQuietly(out); 
				}
			}
			questionOrderService.insert(order, attachmentFile);
			
			return new ResponseMessageBody("工单创建成功" , true) ;
		} catch (Exception e) {
			logger.error( "/admin/qr/upload", e ); 
			return new ResponseMessageBody("工单创建失败,原因："
					+ e.getMessage() , true) ;
		}
	}
}
