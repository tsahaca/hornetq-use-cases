package com.example.jms.task;

public class Optimization {
	private String s;
	
	public Optimization(String s) {
		super();
		this.s = s;
	}

	public void run(long sleep) {
		try {
			System.out.println(Thread.currentThread().getName() + " Start with Data["+s+"]");
			Thread.sleep(sleep);
			System.out.println(Thread.currentThread().getName() + " End with Data["+s+"]");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
