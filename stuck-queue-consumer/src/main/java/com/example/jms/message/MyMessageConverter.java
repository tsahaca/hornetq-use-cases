package com.example.jms.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;


public class MyMessageConverter implements MessageConverter{

	@Override
	public Object fromMessage(Message arg0) throws JMSException, MessageConversionException {
		ObjectMessage mapMessage = (ObjectMessage) arg0;
        AtfMessage jobInfo = (AtfMessage) mapMessage.getObject();
        return jobInfo;
	        
	}

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		 ObjectMessage message = session.createObjectMessage();
		 if(object instanceof AtfMessage){
			 AtfMessage obj = (AtfMessage)object;
			 message.setObject(obj);
		 }
		 return message;
	}

}
