package com.newlinecode.libraryInit;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.RootType;

public class TemplateParser {
	/**
	 * Parses all the existent templates
	 * 
	 * @return operation result
	 * @throws JAXBException 
	 */
	public static boolean parse(String inputModel) throws JAXBException {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(RootType.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		RootType model = (RootType) jaxbUnmarshaller.unmarshal(new File(inputModel));
		
		model.print();
		
		return false;
	}
}
