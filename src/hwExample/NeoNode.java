package hwExample;

import org.eclipse.jdt.core.dom.ASTNode;

public class NeoNode {
	
	private ASTNode node;
	
	/*<id> is used to record the id of the node in Neo4j*/
	private long id;
	
	/*<cypherStentance> is used to store the cypher sentence 
	 * by which the node is stored into the Neo4j database*/
	private String cypherSentence="";
	
	
	public NeoNode(ASTNode node){
		this.node=node;
	}
	
	public ASTNode getNode() {
		return node;
	}
	public void setNode(ASTNode node) {
		this.node = node;
	}
	public long getId() {
		return id;
	}
	public void setId(Long long1) {
		this.id = long1;
	}

	public String getCypherSentence() {
		return cypherSentence;
	}

	public void setCypherSentence(String cypherSentence) {
		this.cypherSentence = cypherSentence;
	}

	
}
