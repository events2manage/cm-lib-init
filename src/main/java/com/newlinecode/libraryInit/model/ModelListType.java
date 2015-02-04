package com.newlinecode.libraryInit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelListType implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<ModelType> model;
	
	public ModelListType() {
		model = new ArrayList<ModelType>();
	}

	public List<ModelType> getModel() {
		return model;
	}

	public void setModel(List<ModelType> model) {
		this.model = model;
	}
}
