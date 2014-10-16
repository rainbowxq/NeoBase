package test;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;

import collector.JavaFiles;
import analyse.Parser;

public class ATDtest {
	public void eTest(String[] filePaths,String[] classPaths){
		Parser parser=new Parser("","ClassPreamble.java","./src/testcases/ClassPreamble.java",
				filePaths,classPaths);
		parser.ececute();
	}
	
	public static void main(String[] args){
//		ATDtest atd=new ATDtest();
//		atd.eTest();
		JavaFiles files=new JavaFiles();
		files.readFolder("/home/xiaoq_zhu/workspace/NeoBase");
		String[]filePaths=(String[]) files.getfilePaths().toArray();
		String[]classPaths=(String[]) files.getclasspaths().toArray();
		EDtest ed=new EDtest();
		ed.eTest(filePaths,classPaths);
//		TDtest td=new TDtest();
//		td.eTest();
	}
}
