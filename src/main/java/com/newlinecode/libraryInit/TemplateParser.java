package com.newlinecode.libraryInit;

import com.newlinecode.libraryInit.inc.TemplateNames;

public class TemplateParser {
	
	// path to project
	private String path;
	
	// name of the new library
	private String libraryName;
	
	/**
	 * Parses a given template
	 * 
	 * @param template {@link TemplateNames}
	 * @return operation result
	 */
	public boolean parseTemplate(TemplateNames template) {
		return false;
	}
	
	/**
	 * Parses all the existent templates
	 * 
	 * @return operation result
	 */
	public boolean parseAll() {
		return false;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
