package ast2;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactoryState;

import collector.CUnit;

public class Parser2 {

	// private String projectName;// assume projects don't have the same names
	private String fileName;
	private String filePath;
	CompilationUnit javaUnit;

	private Long cid;
	private final GraphDatabaseBuilder gbuilder;
	private final int pid;

	// private final GraphDatabaseService db;
	// private final ExecutionEngine engine ;

	public Parser2(GraphDatabaseBuilder builder, String fName, String fPath,int pid) {
		this.gbuilder = builder;
		this.setFileName(fName);
		this.setFilePath(fPath);
		this.pid=pid;
	}

	public void analyse(String[] classPaths, String[] filePaths) {
		CUnit unit = new CUnit();
		unit.setProgram(this.filePath);
		Map options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5); // or newer
																		// version
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);

		parser.setUnitName(this.fileName);
		parser.setEnvironment(classPaths, filePaths, null, true);
		parser.setSource(unit.getProgram().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit javaUnit = (CompilationUnit) parser.createAST(null);
		this.javaUnit = javaUnit;

		GraphDatabaseService db = this.gbuilder.newGraphDatabase();
		Transaction tx = db.beginTx();
		try {
			JFileVisitor2 astVisitor = new JFileVisitor2(
					new ExecutionEngine(db), this.fileName,pid);
			javaUnit.accept(astVisitor);
			this.cid = astVisitor.getCuid();
			tx.success();
		} finally {
			tx.finish();
			// System.out.println("Visited CU and commited");
		}
		db.shutdown();

	}

	// public String getProjectName() {
	// return projectName;
	// }
	//
	// public void setProjectName(String projectName) {
	// this.projectName = projectName;
	// }

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

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

}
