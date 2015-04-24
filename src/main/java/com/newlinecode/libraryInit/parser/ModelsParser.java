package com.newlinecode.libraryInit.parser;

import com.newlinecode.libraryInit.model.ModelListType;
import com.newlinecode.libraryInit.model.ModelType;

public class ModelsParser {
	
	private ModelListType models;
	
	public ModelsParser() {
		
	}
	
	public ModelsParser(ModelListType models) {
		setModels(models);
	}
	
	public void fillModels() {
		
		// insert the first model in the root library
		// create sublibraries for the others
		
		for(ModelType model : models.getModel()) {
			// parse model
		}
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
}
