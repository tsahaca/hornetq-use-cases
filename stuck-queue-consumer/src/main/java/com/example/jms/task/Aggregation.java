package com.example.jms.task;

public class Aggregation  implements Runnable {

	@Override
	public void run() {
		try {
			for (int i = 0; i < 100; i++) {
				System.out.println("From " + this.getClass().getName() + " (" + i + ") times");
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
