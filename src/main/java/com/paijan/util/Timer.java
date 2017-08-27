package com.paijan.util;

/**
 * Created by Maciej on 2016-05-01.
 */
public class Timer {
	private final long start;
	private long last;
	public Timer() {
		start = System.nanoTime();
		last = start;
	}

	public void next(String note) {
		long now = System.nanoTime();
		float time = (now-last) / 1000000000.f;
		last = now;
		System.out.println(note + ": " + time+"s");
	}

	public void total(String note) {
		System.out.println(note + ": " + (System.nanoTime() - start)/1000000000.f + "s");
	}
}
