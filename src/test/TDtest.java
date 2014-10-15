package test;

import analyse.Parser;

public class TDtest {
	public void eTest(){
		Parser parser=new Parser("","TypeDecla.java","./src/testcases/TypeDecla.java");
		parser.ececute();
	}
}
