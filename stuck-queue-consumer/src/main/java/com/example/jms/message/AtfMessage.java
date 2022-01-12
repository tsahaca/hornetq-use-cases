package com.example.jms.message;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class AtfMessage implements Serializable{
	private String message;
	private Date date;
	private String queueName;
	private long sleep = 0l;
	private boolean sendToBd = false;
	public AtfMessage(String message, String queueName, Long sleep, boolean sendToBd) {
		super();
		this.message = message;
		this.date = new Date();
		this.queueName = queueName;
		this.sleep = sleep;
		this.sendToBd = sendToBd;
	}
	@Override
	public String toString() {
		return "[message=" + message + ", date=" + date + ", queueName=" + queueName + ", sleep=" + sleep+ "]";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public long getSleep() {
		return sleep;
	}
	public void setSleep(long sleep) {
		this.sleep = sleep;
	}
	public boolean isSendToBd() {
		return sendToBd;
	}
	public void setSendToBd(boolean sendToBd) {
		this.sendToBd = sendToBd;
	}
	
	
}
