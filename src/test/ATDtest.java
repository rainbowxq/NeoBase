package test;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;

import collector.JavaFiles;
import ast.Parser;

public class ATDtest {
	public void eTest(String[] classPaths,String[] filePaths){
		Parser parser=new Parser("ClassPreamble.java","./src/testcases/ClassPreamble.java");
		parser.ececute(classPaths,filePaths);
	}
	
	public static void main(String[] args){
//		ATDtest atd=new ATDtest();
//		atd.eTest();
		EDtest ed=new EDtest();
//		TDtest td=new TDtest();
//		td.eTest();
	}
}
