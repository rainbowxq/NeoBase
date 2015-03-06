package test;

import java.util.List;

import collector.JavaFiles;
import ast.Parser;

public class TestDriver {
	
	private static List<String> srcPaths;
	private static List<String> targetPaths;
	
	public static void eTest(String fileName,String filePath,String[] classPaths,String[] filePaths){
		Parser parser=new Parser(fileName,filePath,4);
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
		
		
//		System.out.println("beginning.....");
//		JavaFiles files=new JavaFiles();
//		files.readFolder("/home/xiaoq_zhu/zxq/workspace/org.eclipse.swt");
//		srcPaths=files.getSources();
//		targetPaths=files.getTargets();
		
		
		TestDriver.eTest("IvyDeliver.java","/home/xiaoq_zhu/Documents/experiment/apache-ivy/src_java/org/apache/ivy/plugins/signer/bouncycastle/OpenPGPSignatureGenerator.java",
				new String[]{"/home/xiaoq_zhu/Documents/experiment/apache-ivy/ivy-2.4.0.jar",
				"/home/xiaoq_zhu/Documents/experiment/apache-ivy/ant.jar",
				"/home/xiaoq_zhu/Documents/experiment/apache-ivy/bcpg-jdk15-1.45.jar",
				"/home/xiaoq_zhu/Documents/experiment/apache-ivy/bcprov-jdk15-1.45.jar"
				},new String[]{"/home/xiaoq_zhu/Documents/experiment/apache-ivy/src_java"});

		
		//		String filepath="/home/xiaoq_zhu/zxq/workspace/org.eclipse.swt/Eclipse SWT/src_common/org/eclipse/swt/internal/image/PNGFileFormat.java";
//		String filename="PNGFileFormat.java";
//		String[] tarpaths=(String[]) targetPaths.toArray(new String[targetPaths.size()]);
//		String[] srcpaths=(String[]) srcPaths.toArray(new String[srcPaths.size()]);
		
		
//		TestDriver.eTest(filename,filepath,tarpaths,srcpaths);
		
		
		
	
//		TestDriver.eTest("hello.java",
//				"/home/xiaoq_zhu/zxq/workspace/HelloWorld/src/test/HelloWorld.java",
//				new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld/bin"},
//				new String[]{"/home/xiaoq_zhu/zxq/workspace/HelloWorld"});
		System.out.println("finished!!");
		
	}




	public static List<String> getSrcPaths() {
		return srcPaths;
	}




	public static void setSrcPaths(List<String> srcPaths) {
		TestDriver.srcPaths = srcPaths;
	}




	public static List<String> getTargetPaths() {
		return targetPaths;
	}




	public static void setTargetPaths(List<String> targetPaths) {
		TestDriver.targetPaths = targetPaths;
	}
}
