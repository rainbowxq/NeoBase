package test;

import analyse.Parser;

public class TestDriver {
	public static void eTest(String fileName,String filePath){
		Parser parser=new Parser("",fileName,filePath);
		parser.ececute();
	}
	
	public static void main(String[] args){
//		TestDriver.eTest("TestAnnotation.java","./src/testcases/TestAnnotation.java");//pass
		TestDriver.eTest("TestTypeParameter.java","./src/testcases/TestTypeParameter.java");
	}
}
