package com.newlinecode.libraryInit;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.newlinecode.libraryInit.model.ModelType;
import com.newlinecode.libraryInit.model.RootType;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws JAXBException 
     * @throws FileNotFoundException 
     */
    public void testApp() throws JAXBException, FileNotFoundException
    {
        assertTrue( true );
        
        // parse document
		RootType r = new RootType("TestLibrary", "c:\\tmp\\test-project");
		ModelType m = new ModelType();
		
		r.getModels().getModel().add(m);
		m.setForceLeftJoin(true);
		m.setModelKey("model_db_key");
		m.setModelName("TestModel");
		m.setModelDbName("model_db_name");
		m.getTableRelations().add("user");
		
		m = new ModelType();
		
		r.getModels().getModel().add(m);
		m.setForceLeftJoin(true);
		m.setModelKey("model_db_key1");
		m.setModelName("TestModel1");
		m.setModelDbName("model_db_name");
		m.getTableRelations().add("user");
		
		JAXBContext context = JAXBContext.newInstance(RootType.class);
		String xml = asString(context, r);
		
		System.out.print(xml);
		System.out.println();
		
		PrintWriter out = new PrintWriter("c:\\tmp\\xmlIn.xml");
		out.println(xml);
		out.close();
		
		new TemplateParser().parse("c:\\tmp\\xmlIn.xml");
    }
    
    /**
     * Rigourous Test :-)
     * @throws JAXBException 
     * @throws FileNotFoundException 
     */
    public void testFromFileTemplate() throws JAXBException
    {
        assertTrue( true );
		
		new TemplateParser().parse("c:\\tmp\\testUserIn.xml");
    }
    
    public static String asString(JAXBContext pContext, Object pObject) throws JAXBException {

		java.io.StringWriter sw = new StringWriter();

		Marshaller marshaller = pContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.marshal(pObject, sw);

		return sw.toString();
	}
}
