
package analyse;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import node.NodeInfo;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.osgi.internal.debug.Debug;

import relationship.Relation;
import collector.CUnit;

public class Parser {
	private String projectName;// assume projects don't have the same names 
	private String fileName;
	private String filePath;
//	private String[] filePaths;
//	private String[] classPaths;
	private List<ASTNode> nodes=new ArrayList<ASTNode>();
	private List<NodeInfo> infos=new ArrayList<NodeInfo>();
	private List<Relation> relations=new ArrayList<Relation>();
	
	public Parser(String pName,String fName,String fPath){
		this.setProjectName(pName);
		this.setFileName(fName);
		this.setFilePath(fPath);
	}
	
	public void analyse(String[]classPaths,String[]filePaths){
		CUnit unit=new CUnit();
		unit.setProgram(this.filePath);
		Map options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5); //or newer version
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);

		parser.setUnitName(this.fileName);
		parser.setEnvironment(classPaths,filePaths,new String[] { "UTF-8" }, true);
		parser.setSource(unit.getProgram().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit javaUnit = (CompilationUnit)parser.createAST(null) ;
//		parser.createASTs(null, null, null, null);
		JFileVisitor visitor=new JFileVisitor(this.fileName);
		javaUnit.accept(visitor);
		this.setNodes(visitor.getNodes());
		this.setInfos(visitor.getInfos());
		this.setRelations(visitor.getRelations());
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
	

	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	


	
	public void ececute(String[]classPaths,String[]filePaths){
		this.analyse(classPaths,filePaths);
		for(int i=0;i<this.infos.size();i++){
			String a=Neo4jOp.executeQuery(this.infos.get(i).getCypherSentence());
			String b[]=a.split(" ");
//			for(int j=0;j<b.length;j++){
//				System.out.println(b[j]+" "+j);
//			}
			this.setNodeId(i,Long.parseLong(b[12]));
			System.out.println(this.infos.get(i).getId());
		}
		
		for(int i=0;i<this.relations.size();i++){
			Relation r=this.relations.get(i);
			int fromIndex=this.nodes.indexOf(r.getFrom());
			if(fromIndex==-1){
				Debug.println("hahahah...."+r.getFrom().toString());
			}
			int toIndex=this.nodes.indexOf(r.getTo());
			assert fromIndex!=-1 : "the from node doesn't exist";
			assert toIndex!=-1 : "the to node doesn't exist";
			
			long fromId=this.infos.get(fromIndex).getId();
			
			long toId=this.infos.get(toIndex).getId();
			
			Neo4jOp.addRelation(fromId, toId, r.getRelationType());
		}
		
	}

	private void setNodeId(int i, long parseLong) {
		// TODO Auto-generated method stub
		this.infos.get(i).setId(parseLong);
	}

	public List<NodeInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<NodeInfo> infos) {
		this.infos = infos;
	}
	
	public List<ASTNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<ASTNode> nodes) {
		this.nodes=nodes;
	}
}
