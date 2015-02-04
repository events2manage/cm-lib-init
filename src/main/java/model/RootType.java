package model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RootType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String projectPath;
	private String controllerName;	
	private ModelListType models;
	private boolean hasHelper = true;
	private boolean hasRules = true;

	public RootType() {
		controllerName = new String();
		projectPath = new String("c:/projects/test-project");
		models = new ModelListType();
	}
	
	/**
	 * 
	 * @param libraryName {@link String} libraryName
	 */
	public RootType(String libraryName) {
		controllerName = new String(libraryName);
		projectPath = new String("c:/projects/test-project");
		models = new ModelListType();
	}
	
	/**
	 * 
	 * @param libraryName {@link String} name of the new library to add
	 * @param projectPath {@link String} path of the project
	 */
	public RootType(String libraryName, String projectPath) {
		controllerName = new String(libraryName);
		this.projectPath = new String(projectPath);
		models = new ModelListType();
	}
	
	@XmlElement
	public boolean isHasHelper() {
		return hasHelper;
	}

	public void setHasHelper(boolean hasHelper) {
		this.hasHelper = hasHelper;
	}

	@XmlElement
	public boolean isHasRules() {
		return hasRules;
	}

	public void setHasRules(boolean hasRules) {
		this.hasRules = hasRules;
	}
	
	@XmlElement
	public ModelListType getModels() {
		return models;
	}

	public void setModels(ModelListType models) {
		this.models = models;
	}
	
	@XmlElement
	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}
	
	@XmlElement
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public void print() {
		System.out.println("ControllerName: " + this.controllerName);
		System.out.println("ProjectPath: " + this.projectPath);
		System.out.println("HasHelper: " + this.hasHelper);
		System.out.println("HasRules: " + this.hasRules);
		
		for(ModelType m: getModels().getModel()) {
			m.print();
		}
	}
}
