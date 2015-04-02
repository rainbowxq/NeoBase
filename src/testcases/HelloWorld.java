
package testcases;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import ast2.Query2;

/**
 * sdkfheui
 * sdfkeuq
 * @author xiaoq_zhu
 *
 */
public class HelloWorld {
	private static String DPATH = "./data/test";
	private static GraphDatabaseService db=new GraphDatabaseFactory().newEmbeddedDatabase(DPATH);
	private static ExecutionEngine engine = new ExecutionEngine(db);
	
	public static void main(String[] args){
		ExecutionResult resultIterator = null;
		Transaction tx = db.beginTx();
		try {
			for(int i=0;i<100;i++){
				Node a=db.createNode();
				System.out.println(a.getId());
			}
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Query2.setDb(db);
		Query2.getMaxPid();
	}
	
}


