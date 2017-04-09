package cn.hnisi.mail.services;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import cn.hnisi.mail.iface.IMailConfig;
import cn.hnisi.mail.iface.IMailService;

@Service
public class MailService implements IMailService{

	@Override
	public void sendMail(IMailConfig config) throws Exception {
		
		//配置参数
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", config.getHost());//存储发送邮件服务器的信息  
		props.put("mail.smtp.port", config.getPort());//端口号
        props.put("mail.smtp.auth", "true");//同时通过验证  
        
        //获取会话
        Session session = Session.getInstance(props);
        
        //设置邮件信息
        MimeMessage message = new MimeMessage(session);//由邮件会话新建一个消息对象  
        message.setFrom(new InternetAddress(config.getFromMail()));//设置发件人的地址  
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(config.getToMail()));//设置收件人,并设置其接收类型为TO  
        
        //设置信件内容  
        message.setSubject(config.getMailTitle());//设置标题  
        message.setContent(config.getMailContent(), "text/html;charset=gbk"); //发送HTML邮件，内容样式比较丰富  
        message.setSentDate(new Date());//设置发信时间  
        message.saveChanges();//存储邮件信息  
  
        //发送邮件  
        Transport transport = session.getTransport();
        transport.connect(config.getUser(), config.getPassword());  
        transport.sendMessage(message, message.getAllRecipients());//发送邮件,其中第二个参数是所有已设好的收件人地址  
        transport.close(); 
        
	}

}
