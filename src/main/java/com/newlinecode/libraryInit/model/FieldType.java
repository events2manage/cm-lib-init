package com.newlinecode.libraryInit.model;

import java.io.Serializable;

public class FieldType implements Serializable {

	private static final long serialVersionUID = 7239131208485710661L;
	private String fieldName;
	private String fieldType;
	private int fieldLength;
	private String fieldProperties;
	
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return the fieldLength
	 */
	public int getFieldLength() {
		return fieldLength;
	}

	/**
	 * @param fieldLength the fieldLength to set
	 */
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	/**
	 * @return the fieldProperties
	 */
	public String getFieldProperties() {
		return fieldProperties;
	}

	/**
	 * @param fieldProperties the fieldProperties to set
	 */
	public void setFieldProperties(String fieldProperties) {
		this.fieldProperties = fieldProperties;
	}
}
