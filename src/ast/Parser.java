
package ast;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import log.Log;
import neo4j.Neo4jOp;
import node.NodeInfo;
import node.SENode;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.osgi.internal.debug.Debug;

import cfg.MethodVisitor;
import relationship.Relation;
import collector.CUnit;

public class Parser {
//	private String projectName;// assume projects don't have the same names 
	private String fileName;
	private String filePath;
	CompilationUnit javaUnit;
	
	private Long cid;
//	private String[] filePaths;
//	private String[] classPaths;
	private List<ASTNode> nodes=new ArrayList<ASTNode>();
	private List<NodeInfo> infos=new ArrayList<NodeInfo>();
	private List<Relation> relations=new ArrayList<Relation>();
	private List<SENode> senodes=new ArrayList<SENode>();
	
	public Parser(String fName,String fPath,int pid){
		this.setFileName(fName);
		this.setFilePath(fPath);
		Query.setPid(pid);
	}
	
	public void analyse(String[]classPaths,String[]filePaths){
		CUnit unit=new CUnit();
		unit.setProgram(this.filePath);
		Map options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5); //or newer version
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);

		parser.setUnitName(this.fileName);
		parser.setEnvironment(classPaths,filePaths,null, true);
		parser.setSource(unit.getProgram().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit javaUnit = (CompilationUnit)parser.createAST(null) ;
		this.javaUnit=javaUnit;
		/**********************************************************/
		//when testing cfg, comment lines in this area
		JFileVisitor astVisitor=new JFileVisitor(this.fileName);
		
		javaUnit.accept(astVisitor);
		this.setNodes(astVisitor.getNodes());
		this.setInfos(astVisitor.getInfos());
		this.addRelations(astVisitor.getRelations());
		
		
		/***********************************************************/
		
		/*MethodVisitor cfgVisitor=new MethodVisitor();
		javaUnit.accept(cfgVisitor);
		assert(cfgVisitor.getPNodeSize()==0);
		this.addRelations(cfgVisitor.getCfgR());
		this.setSenodes(cfgVisitor.getSeNodes());*/
		/***************************************************/
		//when testing cfg, uncomment lines in this area
		//this.setNodes(cfgVisitor.getCfgN());
		//this.setInfos(cfgVisitor.getInfos());
		/***************************************************/
		
		
		
		
		
		//		HWVisitor visitor=new HWVisitor();
//		helloworld.accept(visitor);
		
	}
	
	
	
	

//	public String getProjectName() {
//		return projectName;
//	}
//
//	public void setProjectName(String projectName) {
//		this.projectName = projectName;
//	}

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

	public void addRelations(List<Relation> relations) {
		this.relations.addAll(relations);
	}
	


	
	public void ececute(String[]classPaths,String[]filePaths){
		System.out.println("begin storage process........");
		this.analyse(classPaths,filePaths);
		for(int i=0;i<this.infos.size();i++){
			String a=Neo4jOp.executeQuery(this.infos.get(i).getCypherSentence());
			String b[]=a.split(" ");
//			for(int j=0;j<b.length;j++){
//				System.out.println(b[j]+" "+j);
//			}
			try{
			this.setNodeId(i,Long.parseLong(b[12]));
			}
			catch(java.lang.NumberFormatException e){
				System.out.println(this.fileName);
				System.out.println(this.filePath);
				System.out.println(this.infos.get(i).getCypherSentence());
				System.out.println(a);
				Log.debugLoger(fileName);
				Log.debugLoger(filePath);
				Log.debugLoger(this.infos.get(i).getCypherSentence());
				return;
			}
//			System.out.println(this.infos.get(i).getId());
		}
		
		int cindex=this.nodes.indexOf(javaUnit);
		this.setCid(this.infos.get(cindex).getId());
		System.out.println(this.infos.get(cindex).getId());
		
		for(int i=0;i<this.relations.size();i++){
			Relation r=this.relations.get(i);
			int fromIndex=this.nodes.indexOf(r.getFrom());
			if(fromIndex==-1){
				Debug.println("hahahah...."+r.getFrom().toString()+"  "+r.getFrom().getParent().getClass().toString());
				Debug.println("hahahah...."+r.getFrom().toString()+"  "+r.getFrom().getClass().toString());
				Debug.println("hahahah...."+r.getTo().toString()+"  "+r.getTo().getClass().toString());
			}
			int toIndex=this.nodes.indexOf(r.getTo());
			assert fromIndex!=-1 : "the from node doesn't exist";
			assert toIndex!=-1 : "the to node doesn't exist"+r.getTo().toString();
			
			long fromId=this.infos.get(fromIndex).getId();
			
			long toId=this.infos.get(toIndex).getId();
			
			Neo4jOp.addRelation(fromId, toId, r.getRelationType(),r.getProperty());
		}
		////for cfg, add start and end node
	/*	List<Long> startids=new ArrayList<Long>();
		List<Long> endids=new ArrayList<Long>();
		for(int i=0;i<this.senodes.size();i++){
			String key=senodes.get(i).getM_key();
			String a=Neo4jOp.executeQuery(Query.startQuery(key));
			String b[]=a.split(" ");
//			for(int j=0;j<b.length;j++){
//				System.out.println(b[j]+" "+j);
//			}
			long sid=Long.parseLong(b[12]);
			int stindex=this.nodes.indexOf(senodes.get(i).getStart_to());
			long stid=this.infos.get(stindex).getId();
			Neo4jOp.addRelation(sid, stid, "CFG",key);
			
			String c=Neo4jOp.executeQuery(Query.endQuery(key));
			String d[]=c.split(" ");
//			for(int j=0;j<b.length;j++){
//				System.out.println(b[j]+" "+j);
//			}
			long eid=Long.parseLong(d[12]);
			List<ASTNode> ends=this.senodes.get(i).getEnd_from();
			for(int j=0;j<ends.size();j++){
				int efindex=this.nodes.indexOf(ends.get(j));
				long efid=this.infos.get(efindex).getId();
				Neo4jOp.addRelation(efid, eid, "CFG",key);
			}
		}*/
		System.out.println("finish storage process........");
		
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

	public List<SENode> getSenodes() {
		return senodes;
	}

	public void setSenodes(List<SENode> senodes) {
		this.senodes = senodes;
	}


	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

}
