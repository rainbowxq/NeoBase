package test;

import ast.Parser;

public class EDtest {
	public void eTest(String[] classPaths,String[] filePaths){
		Parser parser=new Parser("Enum.java","./src/testcases/Enum.java",1);
		parser.ececute(classPaths,filePaths);
	}
}
