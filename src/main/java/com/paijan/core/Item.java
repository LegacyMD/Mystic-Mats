package com.paijan.core;

import com.paijan.util.JsonUtils;

/**
 * Abstract superclass for {@link com.paijan.ui.Item}
 * It contains all unchangeable fields of Item concept, but leaves price handling
 * for subclasses to implement
 */
public abstract class Item {
	//------------------------------------------------------------ Instance
	private final String mCategory;
	private final int mId;
	private final String mName;
	private final ForgeInfo mForgeInfo;

	private boolean mTradable = true;

	protected Item(String line) {
		String[] tokens = line.split(" ");

		mCategory = tokens[0];
		mId = Integer.parseInt(tokens[1]);
		mName = tokens[2].replace('_', ' ');
		if (mCategory.equals("OTHER")) mForgeInfo = null;
		else {
			mForgeInfo = new ForgeInfo();
			mForgeInfo.yield = Yield.yieldOf(this);
			mForgeInfo.components = new Component[4];
			for (int i = 0; i < 4; i++) {
				Component component = new Component();
				component.id = Integer.parseInt(tokens[3 + 2*i]);
				component.quantity = Integer.parseInt(tokens[4 + 2*i]);
				mForgeInfo.components[i] = component;
			}
		}
	}

	public void refreshPrice() {
		if (mTradable) {
			JsonUtils.Price price = JsonUtils.getItemPrice(mId);
			if(price != null) setPrice(price.buy, price.sell);
			else mTradable = false;
		}
	}

	//------------------------------------------------------------ Accessor

	public abstract void setPrice(int buy, int sell);

	public int getId() {
		return mId;
	}
	public String getCategory() {
		return mCategory;
	}
	public String getName() {
		return mName;
	}
	public ForgeInfo getForgeInfo() {
		return mForgeInfo;
	}

	//------------------------------------------------------------ Misc

	public static class ForgeInfo {
		public Yield yield;
		public Component[] components;
	}

	public static class Component {
		public int id, quantity;
	}

	//------------------------------------------------------------
}
