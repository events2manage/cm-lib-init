package com.newlinecode.libraryInit.parser;

import java.util.List;

import com.newlinecode.libraryInit.TemplateParser;
import com.newlinecode.libraryInit.model.ModelListType;
import com.newlinecode.libraryInit.model.ModelType;
import com.newlinecode.libraryInit.model.RootType;

public class ModelsParser {
	
	private ModelListType models;
	
	public ModelsParser() {
		
	}
	
	public ModelsParser(ModelListType models) {
		setModels(models);
	}
	
	public void fillModels() {
	}

	/**
	 * @return the models
	 */
	public ModelListType getModels() {
		return models;
	}

	/**
	 * @param models the models to set
	 */
	public void setModels(ModelListType models) {
		this.models = models;
	}

	public static void createAndParseTemplate(TemplateParser context) {
		// create helper dir
		RootType model = context.getModel();
		List<ModelType> modelList = model.getModels().getModel();
		
		String basePath = model.getProjectPath() + context.getLibraryPath();
		if (modelList.size() > 0) {
			TemplateParser.createDir(basePath + "/" + "model/");
			TemplateParser.createDir(basePath + "/" + "collection/");
		}
		
		// from the second model ahead create a sublibrary structure
		// with helpers controllers rules a model and a collection
		for(int i = 1; i < modelList.size(); i++) {
			RootType newRoot = new RootType(modelList.get(i).getModelName(), basePath);
			newRoot.setHasHelper(true);
			newRoot.setHasRules(true);
			newRoot.setControllerName(modelList.get(i).getModelName());
			newRoot.setProjectPath(basePath);
			
			ModelListType newModels = new ModelListType();
			newModels.getModel().add(modelList.get(i));
			
			newRoot.setModels(newModels);
			TemplateParser tmpl = new TemplateParser();
			tmpl.parse(newRoot);
		}
	}
}
