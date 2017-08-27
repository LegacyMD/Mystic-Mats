package com.paijan.util.data_parse;

public class IdFetcher {

	public static int fetchId(String link) throws FetchException {
		link = strip(link);
		if (link.length()%4 != 0) throw new FetchException("invalid base64");
		String hex = Base64ToHex(link);
		String sub = hex.substring(4, 12);
		String reversed = reversePairs(sub);
		int id = Integer.parseInt(reversed, 16);
		return checkedId(id);
	}

	private static String strip(String arg) {
		StringBuilder result = new StringBuilder(arg);
		for (int i = result.length() - 1; i >= 0; i--) {
			char c = result.charAt(i);
			if (c == '[' || c == ']' || c == '&' || c == '"' || c == ',') result.deleteCharAt(i);
		}
		return result.toString().trim();
	}

	private static String Base64ToHex(String arg) throws FetchException {
		StringBuilder result = new StringBuilder(arg.length()*3/2);
		for (int i = 0; i < arg.length() - 3; i += 4) {
			int value = 0;
			for (int j = 0; j < 4; j++) {
				value <<= 6;
				value += signValue(arg.charAt(i + j));
			}
			result.append(Integer.toString(value, 16));
		}
		while (result.length()%3 != 0) result.insert(0, '0');
		return result.toString();
	}
	private static int signValue(char c) throws FetchException {
		if ('A' <= c && c <= 'Z') return c - 'A';
		if ('a' <= c && c <= 'z') return 26 + c - 'a';
		if ('0' <= c && c <= '9') return 51 + c - '0';
		if (c == '+' || c == '-') return 62;
		if (c == '/' || c == '_') return 63;

		throw new FetchException("Invalid sign");
	}
	private static String reversePairs(String arg) {
		StringBuilder result = new StringBuilder(arg.length());
		for (int i = arg.length() - 2; i >= 0; i -= 2) {
			result.append(arg.charAt(i)).append(arg.charAt(i + 1));
		}
		return result.toString();
	}

	private static int checkedId(int id) {
		//Id for philosopher's stone is just wrong on gw2shinies.com
		//And it does not exist on api.guildwars2.com
		switch (id) {
			case 20795: return 20796;
			default: return id;
		}
	}

	public static class FetchException extends Exception {
		public FetchException(String message) {
			super(message);
		}
	}
}
