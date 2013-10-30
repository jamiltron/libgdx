
package com.badlogic.gdx.tests;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;

import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.XmlWriter;

public class XmlWriterTest extends GdxTest {
	private StringWriter stringWriter;
	private XmlWriter xmlWriter;

	@Override
	public void create () {
		try {
			testAllSpecialCharacters();
			testElementAndText();
			testExample();
			testMultipleAttributes();
			testSpecialCharactersInElementAndText();
			testSpecialCharactersInElementAndThenText();
			testSpecialCharactersInEverything();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void prepareWriters() {
		stringWriter = new StringWriter();
		xmlWriter = new XmlWriter(stringWriter);
	}

	private void runTest (String testName, String expected, String actual) throws IOException {
		if (!actual.equals(expected)) {
			throw new IOException(testName + " failed!");
		} else {
			System.out.println(testName + " passed!");
		}
	}

	private void testExample () throws IOException {
		prepareWriters();

		String expected = "<meow moo=\"cow\">\n"
			+ "\t<child moo=\"cow\">\n"
			+ "\t\t<child moo=\"cow\">\n"
			+ "\t\t\tXML is like violence. If it doesn&apos;t solve your problem, you&apos;re not using enough of it.\n"
			+ "\t\t</child>\n\t</child>\n</meow>\n";
		xmlWriter.element("meow").attribute("moo", "cow").element("child").attribute("moo", "cow").element("child")
			.attribute("moo", "cow").text("XML is like violence. If it doesn't solve your problem, you're not using enough of it.")
			.pop().pop().pop();

		runTest("testExample", expected, stringWriter.toString());
	}
	
	private void testElementAndText () throws IOException {
		prepareWriters();
		
		String expected = "<meow>cat cat cat cat</meow>\n";
		xmlWriter.element("meow", "cat cat cat cat");
		
		runTest("testElementAndText", expected, stringWriter.toString());
	}

	private void testAllSpecialCharacters () throws IOException {
		prepareWriters();

		String expected = "<text>\n\tThis &quot;text&quot; contains all yo&apos; &lt;special&gt; chars &amp; symbols\n</text>\n";
		xmlWriter.element("text").text("This \"text\" contains all yo' <special> chars & symbols").pop();

		runTest("testAllSpecialCharacters", expected, stringWriter.toString());
	}
	
	private void testSpecialCharactersInElementAndText () throws IOException {
		prepareWriters();
		
		String expected = "<&lt;&quot;&amp;&apos;&gt;>bark &amp; meow</&lt;&quot;&amp;&apos;&gt;>\n";
		xmlWriter.element("<\"&'>", "bark & meow");
		
		runTest("testSpecialCharactersInElementAndText", expected, stringWriter.toString());
	}
	
	private void testSpecialCharactersInElementAndThenText() throws IOException {
		prepareWriters();
		
		String expected = "<&lt;&quot;&amp;&apos;&gt;>bark &amp; meow</&lt;&quot;&amp;&apos;&gt;>\n";
		xmlWriter.element("<\"&'>")
		.text("bark & meow")
		.pop();
		
		runTest("testSpecialCharactersInElementAndThenText", expected, stringWriter.toString());
	}
	
	private void testMultipleAttributes () throws IOException {
		prepareWriters();
		
		String expected = "<text a=\"1\" b=\"2\">hello</text>\n";
		xmlWriter.element("text").attribute("a", 1).attribute("b", "2").text("hello").pop();
		
		runTest("testMultipleAttributes", expected, stringWriter.toString());
	}
	
	private void testSpecialCharactersInEverything () throws IOException {
		prepareWriters();
		
		String expected = "<&gt;&amp;&lt;>\n"
			+ "\t<&apos;child &quot;b=\"1\">"
			+ "hi &amp; hey &amp; hello"
			+ "</&apos;child>\n"
			+ "</&gt;&amp;&lt;>\n";
		
		xmlWriter.element(">&<")
		  .element("'child").attribute("\"b", 1)
		    .text("hi & hey & hello")
		  .pop()
		.pop();
		
		runTest("testSpecialCharactersInEverything", expected, stringWriter.toString());
	}

}
