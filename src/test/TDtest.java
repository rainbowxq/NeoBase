package test;

import analyse.Parser;

public class TDtest {
	public void eTest(String[] filePaths,String[] classPaths){
		Parser parser=new Parser("","TypeDecla.java","./src/testcases/TypeDecla.java",filePaths,classPaths);
		parser.ececute();
	}
}
