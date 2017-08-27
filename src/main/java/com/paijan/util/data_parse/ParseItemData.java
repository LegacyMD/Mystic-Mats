package com.paijan.util.data_parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.paijan.util.JsonUtils;
import com.paijan.util.Paths;

/**
 * Created by Maciej on 2016-04-17.
 */
public class ParseItemData {
	public static void main(String[] args) {
		parse();
	}

	public static void parse() {
		System.out.println("ParseItemData");
		File outFile = new File(Paths.items);
		outFile.getParentFile().mkdirs();
		try (Scanner scanner = new Scanner(new File(Paths.source));
			 ListWriter listWriter = new ListWriter(outFile)) {

			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(" ");
				int[] ids = getIds(tokens);

				for (int id : ids) {
					JsonNode itemNode = JsonUtils.getJsonNode(id, JsonUtils.Type.item);
					listWriter.write(tokens[0]);
					listWriter.write(Integer.toString(id));
					listWriter.write(itemNode.get("name").asText());

					JsonNode forgeNode = JsonUtils.getJsonNode(id, JsonUtils.Type.forge);
					if(!forgeNode.isMissingNode()) {
						listWriter.write(forgeNode.get("recipe_item_1").asText());
						listWriter.write(forgeNode.get("recipe_item_1_quantity").asText());
						listWriter.write(forgeNode.get("recipe_item_2").asText());
						listWriter.write(forgeNode.get("recipe_item_2_quantity").asText());
						listWriter.write(forgeNode.get("recipe_item_3").asText());
						listWriter.write(forgeNode.get("recipe_item_3_quantity").asText());
						listWriter.write(forgeNode.get("recipe_item_4").asText());
						listWriter.write(forgeNode.get("recipe_item_4_quantity").asText());
					}
					listWriter.newLine();

					URL iconUrl = new URL(itemNode.get("icon").asText());
					downloadImage(id, iconUrl);
				}
				System.out.println(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static int[] getIds(String[] tokens) throws IdFetcher.FetchException {
		int[] numbers = new int[tokens.length-1];
		for(int i=0; i< numbers.length; i++) numbers[i] = IdFetcher.fetchId(tokens[i+1]);
		return numbers;
	}

	private static void downloadImage(int id, URL url) throws IOException {
		InputStream in = url.openStream();
		File file = new File(Paths.img_mats + id + ".png");
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[2048];
		while(in.available() > 0) {
			in.read(buffer);
			out.write(buffer);
		}
	}

	private static class IdWriter implements AutoCloseable {
		private final FileWriter mFileWriter;
		private int count = 0;
		private StringBuilder buffer = new StringBuilder();

		public IdWriter(String fileName) throws IOException {
			this(new File(fileName));
		}
		public IdWriter(File file) throws IOException {
			mFileWriter = new FileWriter(file);
		}

		@Override
		public void close() throws Exception {
			if(buffer.length() > 0) flush();
			mFileWriter.close();
		}

		public void write(int arg) throws IOException {
			buffer.append(arg).append(' ');
			count++;
			if(count % 4 == 0)   flush();
		}

		private void flush() throws IOException {
			mFileWriter.write(buffer.append("\n").toString());
			buffer.setLength(0);
		}
	}

	private static class ListWriter implements AutoCloseable {
		private final FileWriter mFileWriter;
		private boolean isNewLine;
		private StringBuilder buffer = new StringBuilder();

		public ListWriter(File file) throws IOException {
			mFileWriter = new FileWriter(file);
			isNewLine = true;
		}

		@Override
		public void close() throws Exception {
			if(buffer.length() > 0) flush();
			mFileWriter.close();
		}

		public void write(String arg) throws IOException {
			if (isNewLine) isNewLine = false;
			else buffer.append(' ');
			buffer.append(arg.replace(' ', '_'));

			if(buffer.length()>128) flush();
		}

		public void newLine() {
			buffer.append('\n');
			isNewLine = true;
		}

		private void flush() throws IOException {
			mFileWriter.write(buffer.toString());
			buffer.setLength(0);
		}
	}

}
