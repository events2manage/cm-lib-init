package com.newlinecode.libraryInit;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;

import com.newlinecode.libraryInit.model.ModelType;
import com.newlinecode.libraryInit.model.RootType;
import com.newlinecode.libraryInit.model.TemplateType;
import com.newlinecode.libraryInit.parser.ModelsParser;

public class TemplateParser implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String LIBRARY_TEMPLATE_LOCATION = "template/";
	private static final String LIBRARY_PATH_TOKEN = "#{LibPath}";
	private static final String LIBRARY_MODEL_TOKEN = "#{ModelName}";
	private static final String LIBRARY_MODEL_TBL_TOKEN = "#{ModelTbl}";
	private static final String LIBRARY_MODEL_KEY_TOKEN = "#{ModelKey}";

	private RootType model;
	private String libraryPath = "";
	private String libraryPrefix = "";

	/**
	 * Parses all the existent templates
	 * 
	 * @return operation result
	 * @throws JAXBException
	 */
	public boolean parse(String inputModel) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(RootType.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		parse((RootType) jaxbUnmarshaller.unmarshal(new File(inputModel)));

		// model.print();
		return true;
	}

	public boolean parse(RootType root) {
		setModel(root);
		StringBuilder path = new StringBuilder();
		
		path.append(getModel().getProjectPath());
		getModel().setProjectPath(normalizePath(path).toString());
		determineLibraryPath();
		buildDirectoryStructure();
		ModelsParser.createAndParseTemplate(this);

		try {
			fillTemplates();
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return false;
		}

		return true;
	}

	public static StringBuilder normalizePath(StringBuilder path) {
		if (!path.substring(path.length() - 2, path.length()).equals("\\")) {
			path.append("\\");
		}

		if (!path.toString().contains("libraries")) {
			path.append("libraries\\");
		}

		return path;
	}

	private void buildDirectoryStructure() {
		createDir(getModel().getProjectPath() + getLibraryPath() + "\\");

		// create helper dir
		if (getModel().isHasHelper()) {
			createDir(getModel().getProjectPath() + getLibraryPath() + "\\"
					+ "helper\\");
		}

		// create helper dir
		if (getModel().isHasRules()) {
			createDir(getModel().getProjectPath() + getLibraryPath() + "\\"
					+ "rules\\");
		}
	}

	public static boolean createDir(String dir) {
		File files = new File(dir.toLowerCase());
		if (!files.exists()) {
			if (files.mkdirs()) {
				System.out.println("Multiple directories are created!");
				return true;
			} else {
				System.out.println("Failed to create multiple directories!");
				return false;
			}
		}

		return true;
	}

	private boolean fillTemplates() throws IOException {
		int ok = 1;
		
		ModelType m = null;
		if(model.getModels().getModel().size()>0) {
			m = model.getModels().getModel().get(0);
		}

		fillTemplate(TemplateType.CONTROLLER, m);

		// create helper from template
		if (getModel().isHasHelper()) {
			fillTemplate(TemplateType.HELPER, m);
		}

		// create rules from template
		if (getModel().isHasRules()) {
			fillTemplate(TemplateType.RULES, m);
		}

        // create helper dir
        if (m != null) {
        	fillTemplate(TemplateType.MODEL, m);
            fillTemplate(TemplateType.COLLECTION, m);
        }

		return ok != 0;
	}

	private boolean fillTemplate(TemplateType templ, ModelType... modelType)
			throws IOException {
		ClassLoader classLoader = TemplateParser.class.getClassLoader();
		File file = null;

		switch (templ) {
		case RULES:
			file = new File(classLoader.getResource(
					LIBRARY_TEMPLATE_LOCATION + TemplateType.RULES.getName())
					.getFile());
			break;
		case HELPER:
			file = new File(classLoader.getResource(
					LIBRARY_TEMPLATE_LOCATION + TemplateType.HELPER.getName())
					.getFile());
			break;
		case MODEL:
			file = new File(classLoader.getResource(
					LIBRARY_TEMPLATE_LOCATION + TemplateType.MODEL.getName())
					.getFile());
			break;
		case COLLECTION:
			file = new File(classLoader.getResource(
					LIBRARY_TEMPLATE_LOCATION
							+ TemplateType.COLLECTION.getName()).getFile());
			break;
		case CONTROLLER:
		default:
			file = new File(classLoader.getResource(
					LIBRARY_TEMPLATE_LOCATION
							+ TemplateType.CONTROLLER.getName()).getFile());
			break;
		}

		String content = readFile(file);
		content = content.replace(LIBRARY_PATH_TOKEN,
				StringUtils.replace(getLibraryPrefix(), "\\", "_")
						+ StringUtils.replace(getLibraryPath(), "\\", "_"));
		if (modelType.length > 0) {
			// TODO: change into loop
			content = content.replace(LIBRARY_MODEL_KEY_TOKEN,
					modelType[0].getModelKey());
			content = content.replace(LIBRARY_MODEL_TBL_TOKEN,
					modelType[0].getModelDbName());
			// TODO: First Upper
			content = content.replace(LIBRARY_MODEL_TOKEN, modelType[0]
					.getModelName().replace("_", "").toLowerCase());
		}

		return saveContent(templ, content, modelType);
	}

	private boolean saveContent(TemplateType templ, String content,
			ModelType... modelType) {
		File outputFile = null;

		switch (templ) {
		case RULES:
		case HELPER:
		case CONTROLLER:
		default:
			outputFile = new File(getModel().getProjectPath()
					+ getLibraryPath() + "\\"
					+ templ.getName().replace(".tmpl", ""));
			break;
		case MODEL:
			outputFile = new File(getModel().getProjectPath()
					+ getLibraryPath() + "\\model\\"
					+ modelType[0].getModelName().toLowerCase() + ".php");
			break;
		case COLLECTION:
			outputFile = new File(getModel().getProjectPath()
					+ getLibraryPath() + "\\collection\\"
					+ modelType[0].getModelName().toLowerCase() + ".php");
			break;
		}

		try {
			BufferedWriter bufferOut = new BufferedWriter(new FileWriter(
					outputFile));
			bufferOut.write(content);
			bufferOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static String readFile(File file) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(
				file.getAbsolutePath()));
		try {
			long len = file.length();
			if (len > Integer.MAX_VALUE)
				throw new IOException("File " + file.getAbsolutePath()
						+ " too large, was " + len + " bytes.");
			byte[] bytes = new byte[(int) len];
			dis.readFully(bytes);
			return new String(bytes, "UTF-8");
		} finally {
			dis.close();
		}
	}

	private void determineLibraryPath() {
		if (getModel().getProjectPath().contains("libraries")) {
			String[] pathParts = getModel().getProjectPath().split("(\\\\|/)");
			int libIndex = 0;

			for (String part : pathParts) {
				libIndex++;
				if ("libraries".equals(part)) {
					break;
				}
			}

			if (libIndex > 0) {
				setLibraryPrefix(StringUtils.join(Arrays.copyOfRange(
						pathParts.clone(), libIndex++, pathParts.length), "\\"));
				if (!"".equals(getLibraryPrefix())) {
					setLibraryPrefix(getLibraryPrefix() + "_");
				}
			}
		}

		setLibraryPath(getModel().getControllerName());
	}

	/**
	 * @return the model
	 */
	public RootType getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(RootType model) {
		this.model = model;
	}

	/**
	 * @return the libraryPath
	 */
	public String getLibraryPath() {
		return libraryPath;
	}

	/**
	 * @param libraryPath
	 *            the libraryPath to set
	 */
	public void setLibraryPath(String libraryPath) {
		this.libraryPath = libraryPath;
	}

	/**
	 * @return the libraryPrefix
	 */
	public String getLibraryPrefix() {
		return libraryPrefix;
	}

	/**
	 * @param libraryPrefix
	 *            the libraryPrefix to set
	 */
	public void setLibraryPrefix(String libraryPrefix) {
		this.libraryPrefix = libraryPrefix;
	}
}
