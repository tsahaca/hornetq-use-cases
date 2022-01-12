package com.example.jms.task;

import java.util.Date;

public class Optimization {
	private String s;
	
	public Optimization(String s) {
		super();
		this.s = s;
	}

	public void run(long sleep) {
		try {
			System.out.println(Thread.currentThread().getName() + " Start with Data["+s+"] at=" + new Date());
			Thread.sleep(sleep);
			System.out.println(Thread.currentThread().getName() + " End with Data["+s+"] at="  + new Date());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
