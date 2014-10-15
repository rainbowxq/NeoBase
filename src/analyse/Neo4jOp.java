package analyse;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

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
		
		public static void addRelation(long id1,long id2,String type){
			JSONObject query=new JSONObject();
			String content="match (f) match (t) where id(f)={from} and id(t)={to} create (f)-[r:"+type+"]->(t) return r";
			query.put("query", content);
			
			JSONObject params=new JSONObject();
			params.put("from",id1 );
			params.put("to", id2);
			
			query.put("params",params);
			
			System.out.println(query.toString());
			Neo4jOp.executeQuery(query.toString());
//			System.out.println(json.toString());
			
		}
}
