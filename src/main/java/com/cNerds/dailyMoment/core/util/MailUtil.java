package com.cNerds.dailyMoment.core.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cNerds.dailyMoment.invite.dto.InviteInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MailUtil {
	
	@Autowired
    private JavaMailSenderImpl javaMailSender;
	
	
	@Value("${spring.file.upload.location}")
	private String emailHtmlFilePath;
	
	
	public boolean sendMail(String subejct, String bodySubject , String bodyContent, InviteInfo inviteInfo) {
		Boolean success = false;
		String emailType ="etc";

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // use multipart (true)

            mimeMessageHelper.setSubject(subejct); // Base64 encoding
            Map<String, String> map = new HashMap<String, String>();
            map.put("subject", bodySubject);
            map.put("desc", "<pre>"+bodyContent+"</pre>");
            String [] emailTypeCheck = inviteInfo.getEmail().split("@");
            if(emailTypeCheck[1].equals("naver.com")) {
            	emailType = "naver";
            }
            String mailContent = getMailContent(map,emailType);               
            mimeMessageHelper.setText(mailContent, true); 
            mimeMessageHelper.setFrom("dailymomentoperator@naver.com");
            mimeMessageHelper.setTo(inviteInfo.getEmail());
            
            javaMailSender.send(mimeMessage);

            success = true;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return success;
    }
  	



	
	public String getMailContent(Map<String, String> keyValue, String emailType){
		String contents;
			contents =  getMailContentsTemplate(emailType);
		
		
		Set<String> keySet = keyValue.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while(keyIt.hasNext()){
		    String key = keyIt.next();
		    String value = keyValue.get(key);
		    if( !StringUtils.isEmpty(key) && !StringUtils.isEmpty(value) ){
    		    // [[_key_]]
    		    contents = contents.replaceAll("\\[\\[\\_" + key + "\\_\\]\\]", value);
		    }
		}
		
		return contents;
	}
	
	private String getMailContentsTemplate(String emailType){
		FileUtil fileUtil = new FileUtil();
		if(emailType.equals("naver")) {
			File file = new File(emailHtmlFilePath+"/email/type_emailNaver.html");
			return fileUtil.readFile(file,"UTF8");
		}else {
			File file = new File(emailHtmlFilePath+"/email/type_emailEtc.html");
			return fileUtil.readFile(file,"UTF8");
		}
		
	}
	
	
	
}


