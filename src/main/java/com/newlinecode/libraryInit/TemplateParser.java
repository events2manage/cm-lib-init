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
		
		StringBuilder path = new StringBuilder();
		
		JAXBContext jaxbContext = JAXBContext.newInstance(RootType.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		model = (RootType) jaxbUnmarshaller.unmarshal(new File(inputModel));
		
		path.append(model.getProjectPath());
		model.setProjectPath(normalizePath(path).toString());
		determineLibraryPath();
		buildDirectoryStructure();
		
		try {
			fillTemplates();
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return false;
		}
		
		// model.print();
		return true;
	}

	public static StringBuilder normalizePath(StringBuilder path) {
		if(!path.substring(path.length() - 2, path.length()).equals("\\")) {
			path.append("\\");
		}
		
		if (!path.toString().contains("libraries")) {
			path.append("libraries\\");
		}
		
		return path;
	}
	
	private void buildDirectoryStructure() {
		createDir(model.getProjectPath() + libraryPath + "\\");
		
		// create helper dir
		if (model.isHasHelper()) {
			createDir(model.getProjectPath() + libraryPath + "\\" + "helper\\");
		}
		
		// create helper dir
		if (model.isHasRules()) {
			createDir(model.getProjectPath() + libraryPath + "\\" + "rules\\");
		}
		
		// create helper dir
		if (model.getModels().getModel().size() > 0) {
			createDir(model.getProjectPath() + libraryPath + "\\" + "model\\");
			createDir(model.getProjectPath() + libraryPath + "\\" + "collection\\");
		}
	}
	
	private static boolean createDir(String dir) {
		File files = new File(dir);
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
		
		fillTemplate(TemplateType.CONTROLLER);
		
		// create helper dir
		if (model.isHasHelper()) {
			fillTemplate(TemplateType.HELPER);
		}

		// create helper dir
		if (model.isHasRules()) {
			fillTemplate(TemplateType.RULES);
		}

		// create helper dir
		if (model.getModels().getModel().size() > 0) {
			for(ModelType m: model.getModels().getModel()) {
				fillTemplate(TemplateType.MODEL, m);
				fillTemplate(TemplateType.COLLECTION, m);
			}
		}
		
		return ok != 0;
	}
	
	private boolean fillTemplate(TemplateType templ, ModelType... modelType) throws IOException {
		ClassLoader classLoader = TemplateParser.class.getClassLoader();
		File file = null;
		
		switch (templ) {
			case RULES:
				file = new File(classLoader.getResource(LIBRARY_TEMPLATE_LOCATION + TemplateType.RULES.getName()).getFile());
				break;
			case HELPER:
				file = new File(classLoader.getResource(LIBRARY_TEMPLATE_LOCATION + TemplateType.HELPER.getName()).getFile());
				break;
			case MODEL:
				file = new File(classLoader.getResource(LIBRARY_TEMPLATE_LOCATION + TemplateType.MODEL.getName()).getFile());
				break;
			case COLLECTION:
				file = new File(classLoader.getResource(LIBRARY_TEMPLATE_LOCATION + TemplateType.COLLECTION.getName()).getFile());
				break;
			case CONTROLLER:
			default:
				file = new File(classLoader.getResource(LIBRARY_TEMPLATE_LOCATION + TemplateType.CONTROLLER.getName()).getFile());
				break;
		}
		
		String content = readFile(file);
		content = content.replace(LIBRARY_PATH_TOKEN, StringUtils.replace(libraryPrefix, "\\", "_") + StringUtils.replace(libraryPath, "\\", "_"));
		if (modelType.length > 0) {
			// TODO: change into loop
			content = content.replace(LIBRARY_MODEL_KEY_TOKEN, modelType[0].getModelKey());
			content = content.replace(LIBRARY_MODEL_TBL_TOKEN, modelType[0].getModelDbName());
			// TODO: First Upper
			content = content.replace(LIBRARY_MODEL_TOKEN, modelType[0].getModelName().replace("_", "").toLowerCase());
		}
		
		return saveContent(templ, content, modelType);
	}
	
	private boolean saveContent(TemplateType templ, String content, ModelType...modelType) {
		File outputFile = null;
		
		switch (templ) {
			case RULES: case HELPER: case CONTROLLER: default:
				outputFile = new File(model.getProjectPath() + libraryPath + "\\" + templ.getName().replace(".tmpl", ""));
				break;
			case MODEL:
				outputFile = new File(model.getProjectPath() + libraryPath + "\\model\\" + modelType[0].getModelName() + ".php");
				break;
			case COLLECTION:
				outputFile = new File(model.getProjectPath() + libraryPath + "\\collection\\" + modelType[0].getModelName() + ".php");
				break;
		}
		
        try {
          BufferedWriter bufferOut = new BufferedWriter(new FileWriter(outputFile));
          bufferOut.write(content);
          bufferOut.close();
        } catch ( IOException e ) {
           e.printStackTrace();
           return false;
        }
        
        return true;
	}

	private static String readFile(File file) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(file.getAbsolutePath()));
	    try {
	        long len = file.length();
	        if (len > Integer.MAX_VALUE) throw new IOException("File "+file.getAbsolutePath()+" too large, was "+len+" bytes.");
	        byte[] bytes = new byte[(int) len];
	        dis.readFully(bytes);
	        return new String(bytes, "UTF-8");
	    } finally {
	        dis.close();
	    }
	}
	
	private void determineLibraryPath() {		
		if(model.getProjectPath().contains("libraries")) {
			String[]pathParts = model.getProjectPath().split("(\\\\|/)");
			int libIndex = 0;
			
			for (String part : pathParts) {
				libIndex++;
				if ("libraries".equals(part)) {
					break;
				}
			}
			
			if( libIndex > 0) {
				libraryPrefix = StringUtils.join(Arrays.copyOfRange(pathParts.clone(), libIndex++, pathParts.length), "\\");
				if(!"".equals(libraryPrefix)) {
					libraryPrefix += "_";
				}
			}
		}
		
		libraryPath = model.getControllerName();
	}
}
