package test;

import analyse.Parser;

public class EDtest {
	public void eTest(String[] classPaths,String[] filePaths){
		Parser parser=new Parser("","Enum.java","./src/testcases/Enum.java");
		parser.ececute(classPaths,filePaths);
	}
}
