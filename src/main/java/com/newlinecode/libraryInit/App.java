package com.newlinecode.libraryInit;

import java.security.InvalidParameterException;

import javax.xml.bind.JAXBException;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) throws JAXBException {
		// parse document
		if(args.length == 0) {
			throw new InvalidParameterException("Missing parameter 0 with the path to the XML model {String}.");
		}
		
		new TemplateParser().parse(args[0]);
	}
}
