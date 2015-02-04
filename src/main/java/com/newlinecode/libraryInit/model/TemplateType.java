package com.newlinecode.libraryInit.model;

public enum TemplateType {
	CONTROLLER ("controller.php.tmpl"), RULES ("/rules/rules.php.tmpl"), MODEL ("model/model.php.tmpl"),
	COLLECTION ("collection/collection.php.tmpl"), HELPER ("helper/helper.php.tmpl"); 
	
	private final String name;
	
	TemplateType(String templateName) {
		this.name = templateName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
