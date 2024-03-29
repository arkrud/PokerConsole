package com.arkrud.pokerconsole.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

// TODO: Auto-generated Javadoc
/**
 * Static methods used to work with MongoDB.<br>
 *
 */
public class MongoDBFactory {
	
	/** The mongo. */
	private static MongoClient mongo;

	/**
	 * Crate mongo connection.
	 */
	public static void crateMongoConnection() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver"); // Suppress warnings
		mongoLogger.setLevel(Level.SEVERE);
		mongo = new MongoClient("localhost", 27017); // Initiate client on default port to locally hosted server
	}

	/**
	 * List all mongo documents.
	 */
	public static void listAllMongoDocuments() {
		MongoDatabase database = mongo.getDatabase("POKER_CHARTS");
		MongoCollection<Document> collection = database.getCollection("charts");
		FindIterable<Document> iterDoc = collection.find();
		Iterator<Document> it = iterDoc.iterator();
		while (it.hasNext()) {
			Document document = (Document) it.next();
			System.out.println(document);
		}
	}

	/**
	 * Adds the document.
	 *
	 * @param iniData the ini data
	 * @param imagePath the image path
	 */
	public static void addDocument(HashMap<String, HashMap<String, String>> iniData, String imagePath) {
		MongoDatabase database = mongo.getDatabase("POKER_CHARTS");
		MongoCollection<Document> collection = database.getCollection("charts");
		Document document = new Document("imagepath", imagePath).append("latest", false).append("colors", iniData);
		collection.insertOne(document);
	}

	/**
	 * Update documents.
	 *
	 * @param filterString the filter string
	 * @param file the file
	 */
	public static void updateDocuments(String filterString, File file) {
		MongoDatabase database = mongo.getDatabase("POKER_CHARTS");
		MongoCollection<Document> collection = database.getCollection("charts");
		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("POKER_CHARTS");
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSInputFile gfsFile = null;
		try {
			gfsFile = gfsPhoto.createFile(file);
			gfsFile.setFilename(file.getPath());
			gfsFile.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		collection.updateMany(Filters.eq("imagepath", filterString), Updates.set("pngfile", gfsFile));
	}

	/**
	 * Gets the image.
	 *
	 * @param newFileName the new file name
	 * @return the image
	 */
	public static void getImage(String newFileName) {
		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("POKER_CHARTS");
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
		try {
			imageForOutput.writeTo(newFileName.replace("\\", "\\\\").split("\\.")[0] + ".gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the color map.
	 *
	 * @param imagePath the image path
	 * @return the color map
	 */
	public static HashMap<String, HashMap<String, String>> getColorMap(String imagePath) {
		HashMap<String, HashMap<String, String>> colorMap = new HashMap<String, HashMap<String, String>>();
		MongoDatabase database = mongo.getDatabase("POKER_CHARTS");
		MongoCollection<Document> collection = database.getCollection("charts");
		BasicDBObject query = new BasicDBObject();
		query.put("imagepath", imagePath);
		List<Document> charts = (List<Document>) collection.find(query).into(new ArrayList<Document>());
		Iterator<Document> it = charts.iterator();
		while (it.hasNext()) {
			Document document = (Document) it.next();
			@SuppressWarnings("unchecked")
			Map<String, Document> map = (Map<String, Document>) document.get("colors");
			for (Map.Entry<String, Document> entry : map.entrySet()) {
				Map<String, String> colorsMap = new HashMap<String, String>();
				colorsMap.put("BlueRGB", entry.getValue().getString("BlueRGB"));
				colorsMap.put("RedRGB", entry.getValue().getString("RedRGB"));
				colorsMap.put("AlphaRGB", entry.getValue().getString("AlphaRGB"));
				colorsMap.put("GreenRGB", entry.getValue().getString("GreenRGB"));
				colorMap.put(entry.getKey(), new HashMap<String, String>(colorsMap));
			}
		}
		return colorMap;
	}

	/**
	 * Close mongo connection.
	 */
	public static void closeMongoConnection() {
		mongo.close();
	}
}
