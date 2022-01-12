package com.example.jms.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.example.jms.controller.HandlerLookup;
import com.example.jms.message.AtfMessage;
import com.example.jms.task.Optimization;
class ATFReceiver implements SessionAwareMessageListener{

	@Override
	public void onMessage(Message message, Session arg1) {
		/*try {
			System.out.println("queueName= Thread="+Thread.currentThread()+"="+message.getJMSMessageID());
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		if(message instanceof ObjectMessage){
			ObjectMessage obj = (ObjectMessage) message;
			try {
				message.acknowledge();
				AtfMessage atf =  (AtfMessage) obj.getObject();
				processMessage(atf);
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void processMessage(AtfMessage atf) {
		Optimization opt = null;
		switch (atf.getQueueName()) {
		case "queue/ATFQueue":
			opt = new Optimization(atf.toString());
			opt.run(atf.getSleep());
			JmsTemplate template = (JmsTemplate) HandlerLookup.getInstance().getBean("BDJmsTemplate");
			if(atf.isSendToBd()){
				atf.setQueueName("queue/BDQueue");
				System.out.println("Send Start for ("+atf.toString()+")");
				template.convertAndSend(atf.getQueueName(), atf);
				System.out.println("Send End for ("+atf.toString()+")");
			}
			break;
		case "queue/BDQueue":
			opt = new Optimization(atf.toString());
			opt.run(atf.getSleep());
		default:
			break;
		}
	}

}
