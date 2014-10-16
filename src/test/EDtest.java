package test;

import analyse.Parser;

public class EDtest {
	public void eTest(String[] filePaths,String[] classPaths){
		Parser parser=new Parser("","Enum.java","./src/testcases/Enum.java",filePaths,classPaths);
		parser.ececute();
	}
}
