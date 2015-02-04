package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ModelType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String modelName;
	private String modelKey;
	private boolean forceLeftJoin;
	private List<String> tableRelations;

	public ModelType() {
		modelName = new String();
		modelKey = new String();
		tableRelations = new ArrayList<String>();
		forceLeftJoin = false;
	}
	
	@XmlElement
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	@XmlElement
	public String getModelKey() {
		return modelKey;
	}
	
	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}
	
	@XmlElement
	public boolean isForceLeftJoin() {
		return forceLeftJoin;
	}
	
	public void setForceLeftJoin(boolean forceLeftJoin) {
		this.forceLeftJoin = forceLeftJoin;
	}
	
	@XmlElement
	public List<String> getTableRelations() {
		return tableRelations;
	}

	public void setTableRelations(List<String> tableRelations) {
		this.tableRelations = tableRelations;
	}
	
	public void print() {
		System.out.println("\t---------------MODEL---------------");
		System.out.println("\tModel: " + this.modelName);
		System.out.println("\tKey: " + this.modelKey);
		System.out.println("\tForceLeftJoin: " + this.forceLeftJoin);
		System.out.println("\t\t---------RELATIONS---------");
		
		for(String rel : getTableRelations()) {
			System.out.println("\t\tRelation: " + rel);
		}
		
		System.out.println("\t---------------END MODEL---------------");
	}
}
