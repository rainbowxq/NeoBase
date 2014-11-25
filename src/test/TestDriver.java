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
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestClassInsCrea.java","./src/testcases/TestClassInsCrea.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestExp1.java","./src/testcases/TestExp1.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestExp2.java","./src/testcases/TestExp2.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestExp3.java","./src/testcases/TestExp3.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestExp4.java","./src/testcases/TestExp4.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestExp5.java","./src/testcases/TestExp5.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestExp6.java","./src/testcases/TestExp6.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TestInit.java","./src/testcases/TestInit.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
		
//		TestDriver.eTest("AssertTest.java","./src/testcases/AssertTest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("CItest.java","./src/testcases/CItest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("VDSTest.java","./src/testcases/VDSTest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("DoTest.java","./src/testcases/DoTest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("ForTest.java","./src/testcases/ForTest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("IfTest.java","./src/testcases/IfTest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("SwitchTest.java","./src/testcases/SwitchTest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("TDStest.java","./src/testcases/TDStest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
//		TestDriver.eTest("ThrowTest.java","./src/testcases/ThrowTest.java",
//				new String[]{"./target/classes"},new String[]{"./src"});
		
		
		TestDriver.eTest("HelloWorld.java","/home/xiaoq_zhu/zxq/workspace/HelloWorld/src/test/HelloWorld.java",
				new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld/bin"},new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld/src"});
		
//		TestDriver.eTest("hello.java",
//				"/home/xiaoq_zhu/zxq/workspace/HelloWorld/src/test/HelloWorld.java",
//				new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld/bin"},
//				new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld"});
	}
}
