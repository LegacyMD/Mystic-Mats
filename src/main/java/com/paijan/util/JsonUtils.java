package com.paijan.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Maciej on 2016-04-17.
 */
public class JsonUtils {
	public enum Type {price, item, forge}
	private static final String[] urls = {Paths.url_price, Paths.url_item, Paths.url_forge};


	public static JsonNode getJsonNode(int id, Type type) throws IOException {
		JsonNode result = new ObjectMapper().readTree(getStream(id, type));
		return locate(result, type);
	}

	public static InputStream getStream(int id, Type type) throws IOException {
		String url = urls[type.ordinal()] + id;
		InputStream stream = new URL(url).openStream();
		return stream;
	}

	private static JsonNode locate(JsonNode node, Type type) {
		if(type==Type.item || type==Type.forge) {
			node = node.path(0);
		}
		return node;
	}

	public static Price getItemPrice(int id) {
		JsonNode root;
		try {
			root = getJsonNode(id, Type.price);
			Price result = new Price();
			result.buy = root.get("buys").get("unit_price").asInt();
			result.sell = root.get("sells").get("unit_price").asInt();
			return result;
		} catch (IOException e) {
			return null;
		}
	}

	public static class Price {
		public int buy, sell;
	}
}
