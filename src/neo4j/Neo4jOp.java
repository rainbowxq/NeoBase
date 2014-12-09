package neo4j;

import javax.ws.rs.core.MediaType;

import log.Log;
import net.sf.json.JSONObject;
import ast.Query;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Neo4jOp {
	/*link to the database*/

	private static final String SERVER_ROOT_URI = "http://127.0.0.1:7474/db/data";
	/*[test to see if the Noe4j datase can be accessed]*/
		public static void linkTest(){
			WebResource resource = Client.create()
			        .resource( SERVER_ROOT_URI );
			ClientResponse response = resource.get( ClientResponse.class );

			Log.debugLoger( String.format( "GET on [%s], status code [%d]",
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
			Log.debugLoger( String.format( "[%s],the query is [%s], status code [%d],\n returned data is [%s]",
					txUri,query, response.getStatus(),r_s) ) ;
			
			return r_s;
			
			
		}
		
		public static void addRelation(long id1,long id2,String type,String prop){
			JSONObject query=new JSONObject();
			JSONObject params=new JSONObject();
			String content=null;
			switch(type){
			case "AST":
				content="match (f) match (t) where id(f)={from} and id(t)={to} create (f)-[r:AST {NAME:{prop}}]->(t) ";
				break;
			case "CFG":
				content="match (f) match (t) where id(f)={from} and id(t)={to} create (f)-[r:CFG {M_KEY:{prop}}]->(t) ";
				break;
			default:
				assert false: "relation type error!!!";
			}
			query.put("query", content);
			
			
			params.put("from",id1 );
			params.put("to", id2);
			params.put("prop", prop);
			query.put("params",params);
			
			Log.debugLoger(query.toString());
			Neo4jOp.executeQuery(query.toString());
//			Log.debugLoger(json.toString());
			
		}
		
		public static int getPid(){
			JSONObject query=new JSONObject();
			query.put("query", "match (n:Project) return max(n.P_ID)");
			String a=Neo4jOp.executeQuery(query.toString());
			String b[]=a.split(" ");
//			for(int j=0;j<b.length;j++){
//				System.out.println(b[j]+" "+j);
//			}
			int pid=Integer.parseInt(b[12]);
			return pid;
		}
		
		public static void main(String[]args){
			Neo4jOp.linkTest();
		}
		
		
}
