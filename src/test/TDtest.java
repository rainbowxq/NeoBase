package test;

import ast.Parser;

public class TDtest {
	public void eTest(String[] filePaths,String[] classPaths){
		Parser parser=new Parser("TypeDecla.java","./src/testcases/TypeDecla.java");
		parser.ececute(classPaths,filePaths);
	}
}
