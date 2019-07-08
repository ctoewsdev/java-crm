/**
 * Project: bodySoleWellnessCenter
 * File: DAO.java
 * Date: Dec 17, 2018
 * Time: 5:14:49 PM
 */

package com.caseytoews.bodysoleapp.database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caseytoews.bodysoleapp.io.FilesReader;
import com.caseytoews.bodysoleapp.io.IOConstants;
import com.caseytoews.bodysoleapp.utility.exception.ApplicationException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Dao {
	protected final String fileName;

	private static final Logger LOG = LogManager.getLogger();
	private FilesReader filesReader;
	private final ObjectMapper objectMapper = new ObjectMapper();

	protected Dao(String fileName) {
		this.fileName = fileName;
		filesReader = FilesReader.getTheInstance();
	}

	public long getCurrentID(String key) throws ApplicationException {

		long ID;
		JsonNode rootNode;

		try {
			byte[] jsonData = filesReader.readIDs();
			rootNode = objectMapper.readTree(jsonData);
			JsonNode idNode = rootNode.path(key);
			ID = idNode.asLong();
		} catch (IOException e) {
			LOG.error("Dao/getCurrentID() error catch");
			throw new ApplicationException(e);
		}
		return ID;
	}

	public long getNextID(String key) throws ApplicationException {

		long ID;
		JsonNode rootNode;

		try {
			byte[] jsonData = filesReader.readIDs();
			rootNode = objectMapper.readTree(jsonData);
			JsonNode idNode = rootNode.path(key);
			ID = idNode.asLong();
			ID++;

			// update rootNode
			((ObjectNode) rootNode).put(key, ID);
			updateNextIDFile(rootNode);
		} catch (IOException e) {
			LOG.error("ERROR: Dao() getNextID()");
			throw new ApplicationException(e);
		}
		return ID;
	}

	public void updateFile(String file, Object json) throws ApplicationException {
		try {
			objectMapper.writeValue(new File(file), json);
		} catch (Exception e) {
			LOG.error("Dao/updateFile() ERROR");
			throw new ApplicationException(e);
		}
	}

	public void updateNextIDFile(JsonNode rootNode) throws ApplicationException {

		final ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(IOConstants.NEXTID_FILE), rootNode);
		} catch (Exception e) {
			LOG.error("ERROR: Dao() updateNextIDFile()");
			throw new ApplicationException(e);
		}
	}

	public void deleteFile(String file) throws ApplicationException {
		File toDelete = new File(file);
		if (toDelete.exists()) {
			toDelete.delete();
		}
	}

	public byte[] listToJsonArray(List<?> list) {

		final byte[] jsonArray;
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(out, list);
		} catch (JsonGenerationException e) {
			LOG.error("ERROR1: Dao() listToJsonArray()");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			LOG.error("ERROR2: Dao() listToJsonArray()");
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error("ERROR3: Dao() listToJsonArray()");
			e.printStackTrace();
		}

		jsonArray = out.toByteArray();
		return jsonArray;
	}
}