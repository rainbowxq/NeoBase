package test;

import collector.JavaFiles;
import analyse.Parser;

public class TestDriver {
	public static void eTest(String fileName,String filePath,String[] classPaths,String[] filePaths){
		Parser parser=new Parser("",fileName,filePath);
		parser.ececute(classPaths,filePaths);
	}
	
	public static void main(String[] args){
//		TestDriver.eTest("TestAnnotation.java","./src/testcases/TestAnnotation.java");//pass
//		TestDriver.eTest("TestTypeParameter.java","./src/testcases/TestTypeParameter.java");//pass
//		JavaFiles files=new JavaFiles();
//		files.readFolder("/home/xiaoq_zhu/workspace/NeoBase");
//		TestDriver.eTest("TestArray.java","./src/testcases/TestArray.java",
//				new String[]{"./target/classes"},new String[]{"."});
		TestDriver.eTest("hello.java",
				"/home/xiaoq_zhu/zxq/workspace/HelloWorld/src/test/HelloWorld.java",
				new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld/bin"},
				new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld"});
	}
}
