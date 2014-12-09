package test;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;

import collector.JavaFiles;
import ast.Parser;

public class ATDtest {
	public void eTest(String[] classPaths,String[] filePaths){
		Parser parser=new Parser("ClassPreamble.java","./src/testcases/ClassPreamble.java",3);
		parser.ececute(classPaths,filePaths);
	}
	
	public static void main(String[] args){
		System.out.println("begin....");
		ATDtest atd=new ATDtest();
		atd.eTest(new String[]{"./target/classes"},new String[]{"./src"});
		System.out.println("finished!!");
//		EDtest ed=new EDtest();
//		TDtest td=new TDtest();
//		td.eTest();
	}
}
