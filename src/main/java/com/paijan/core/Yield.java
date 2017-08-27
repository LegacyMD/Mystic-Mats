package com.paijan.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maciej on 2016-05-01.
 */
public class Yield {
	private static final Map<String,Yield> YIELD_MAP = new HashMap<>();
	static {
		YIELD_MAP.put("T6", new Yield(5, 12, 6.91f));
		YIELD_MAP.put("T6_dust", new Yield(6, 40, 19.9f));
		YIELD_MAP.put("T5", new Yield(6, 40, 18.51f));
		YIELD_MAP.put("T5_dust", new Yield(40,200, 0)); //TODO avg?
	}

	public static Yield yieldOf(Item item) {
		return yieldOf(item.getCategory(), item.getName().endsWith("Dust"));
	}

	public static Yield yieldOf(String category, boolean dust) {
		String key = dust ? category+"_dust" : category;
		return YIELD_MAP.get(key);
	}

	private final int min, max;
	private final float avg;
	public Yield(int min, int max, float avg) {
		this.min = min;
		this.max = max;
		this.avg = avg;
	}
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public float getAvg() {
		return avg;
	}

	public String getMinStr() {
		return "Minimum: "+min;
	}
	public String getAvgStr() {
		return "Average: "+avg;
	}
	public String getMaxStr() {
		return "Maximum: "+max;
	}

}
