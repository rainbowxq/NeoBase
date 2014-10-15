package test;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;

import analyse.Parser;

public class ATDtest {
	public void eTest(){
		Parser parser=new Parser("","ClassPreamble.java","./src/testcases/ClassPreamble.java");
		parser.ececute();
	}
	
	public static void main(String[] args){
//		ATDtest atd=new ATDtest();
//		atd.eTest();
		EDtest ed=new EDtest();
		ed.eTest();
//		TDtest td=new TDtest();
//		td.eTest();
	}
}
