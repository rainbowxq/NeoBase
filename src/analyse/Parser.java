package analyse;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import test.HWVisitor;
import collector.CUnit;

public class Parser {
	private String projectName;// assume projects don't have the same names 
	private String fileName;
	private String filePath;
	
	public Parser(String pName,String fName,String fPath){
		this.setProjectName(pName);
		this.setFileName(fName);
		this.setFilePath(fPath);
	}
	
	public void initializeParser(){
		CUnit unit=new CUnit();
		unit.setProgram(this.filePath);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit.getProgram().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit javaUnit = (CompilationUnit)parser.createAST(null) ;
//		HWVisitor visitor=new HWVisitor();
//		helloworld.accept(visitor);
	}
	
	

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
