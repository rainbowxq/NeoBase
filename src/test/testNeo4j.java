package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import relationship.NeoNode;
import relationship.NeoRelation;
import net.sf.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;



public class testNeo4j {
	
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
		
//				String returnS=response.getEntity(String.class);
//				System.out.println(response.getType().toString());
//				System.out.println(returnS);
				
//				JSONObject gJson = JSONObject.fromObject(returnS);
//				return returnS;
//				return gJson;
//				List names=(List) gJson.get("data");
//				List name=(List)names.get(0);
//				
//				response.close();
//				return (int)name.get(0);
//				return 0;
		
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
//		System.out.println(json.toString());
		
	}
	
	public static void main(String[] args){
		//testNeo4j.linkTest();
		NeoHello hello=new NeoHello();
		hello.analyzeHello();
		ArrayList<NeoNode> nodes=hello.getNodes();
		ArrayList<NeoRelation> relations=hello.getRelations();
		for(int i=0;i<nodes.size();i++){
			String a=testNeo4j.executeQuery(hello.executeStore(nodes.get(i).getNode()));
			String b[]=a.split(" ");
//			for(int j=0;j<b.length;j++){
//				System.out.println(b[j]+" "+j);
//			}
			nodes.get(i).setId(Long.parseLong(b[12]));
		}
		for(int j=0;j<relations.size();j++){
			testNeo4j.addRelation(relations.get(j));
		}
//		JSONObject query=new JSONObject();
//		query.put("query", "match (n) return id(n)");
//		String a=testNeo4j.executeQuery(query.toString());
//		System.out.println(a);
//		String b[]=a.split(" ");
//		for(int i=0;i<b.length;i++){
//			System.out.println(b[i]+" "+i);
//		}
//		String c[]=b[b.length-1].split(" ");
//		for(int i=0;i<c.length;i++){
//			System.out.println(c[i]+""+i);
//		}
//		System.out.println(c[3]);
//		long d=Long.parseLong(c[3]);
//		System.out.println(d);
//		String r=testNeo4j.executeQuery("match (n:TypeDeclaration) return id(n)");
//		System.out.println(r);
	}
}
