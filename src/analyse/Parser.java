package analyse;



import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;
import node.NeoNode;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import relationship.NeoRelation;
import test.testNeo4j;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import collector.CUnit;

public class Parser {
	private String projectName;// assume projects don't have the same names 
	private String fileName;
	private String filePath;
	private List<NeoNode> nodes=new ArrayList<NeoNode>();
	private List<NeoRelation> relations=new ArrayList<NeoRelation>();
	
	public Parser(String pName,String fName,String fPath){
		this.setProjectName(pName);
		this.setFileName(fName);
		this.setFilePath(fPath);
	}
	
	public void analyse(){
		CUnit unit=new CUnit();
		unit.setProgram(this.filePath);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit.getProgram().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit javaUnit = (CompilationUnit)parser.createAST(null) ;
		JFileVisitor visitor=new JFileVisitor(this.fileName);
		javaUnit.accept(visitor);
		this.setNodes(visitor.getNodes());
		this.setRelations(visitor.getRelations());
//		HWVisitor visitor=new HWVisitor();
//		helloworld.accept(visitor);
		
	}
	
	
	


	/*link to the database*/
	
	private static final String SERVER_ROOT_URI = "http://127.0.0.1:7474/db/data";
	/*[test to see if the Noe4j datase can be accessed]*/
		public static void linkTest(){
			WebResource resource = Client.create()
			        .resource( SERVER_ROOT_URI );
			ClientResponse response = resource.get( ClientResponse.class );

			System.out.println( String.format( "GET on [%s], status code [%d]",
			        SERVER_ROOT_URI, response.getStatus() ) );
			response.close();
		}
		
		public static String executeQuery(String query){
			/*[perform cypher query]*/
			final String txUri = SERVER_ROOT_URI + "/cypher" ;
			WebResource resource = Client.create().resource( txUri );
			
			
			ClientResponse response = resource
			        .accept( MediaType.APPLICATION_JSON )
			        .type( MediaType.APPLICATION_JSON )
			        .entity( query)
			        .post( ClientResponse.class );

			String r_s=response.getEntity(String.class);
			System.out.println( String.format( "[%s],the query is [%s], status code [%d],\n returned data is [%s]",
					txUri,query, response.getStatus(),r_s) ) ;
			
			return r_s;
			
			
		}
		
		public static void addRelation(NeoRelation relation){
			NeoNode from=relation.getFrom();
			NeoNode to= relation.getTo();
			JSONObject query=new JSONObject();
			
			String content="match (f) match (t) where id(f)={from} and id(t)={to} create (f)-[r:"+relation.getRelationType()+"]->(t) return r";
			query.put("query", content);
			
			JSONObject params=new JSONObject();
			params.put("from",from.getId() );
			params.put("to", to.getId());
			
			query.put("params",params);
			
			System.out.println(query.toString());
			testNeo4j.executeQuery(query.toString());
//			System.out.println(json.toString());
			
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
	
	public List<NeoNode> getNodes() {
		return nodes;
	}
	
	private void setNodes(List<NeoNode> nodes) {
		// TODO Auto-generated method stub
		this.nodes=nodes;
	}

	public void setNodeId(int index,long id) {
		this.nodes.get(index).setId(id);
	}

	public List<NeoRelation> getRelations() {
		return relations;
	}

	public void setRelations(List<NeoRelation> relations) {
		this.relations = relations;
	}
	
	public static void main(String [] args){
		Parser parser=new Parser("","HelloWorld.java","/home/xiaoq_zhu/zxq/workspace/HelloWorld/src/test/HelloWorld.java");
		parser.analyse();
		for(int i=0;i<parser.nodes.size();i++){
			String a=Parser.executeQuery(parser.nodes.get(i).getCypherSentence());
			String b[]=a.split(" ");
//			for(int j=0;j<b.length;j++){
//				System.out.println(b[j]+" "+j);
//			}
			parser.setNodeId(i,Long.parseLong(b[12]));
			System.out.println(parser.nodes.get(i).getId());
		}
	}
}
