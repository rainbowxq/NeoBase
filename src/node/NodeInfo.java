package node;


public class NodeInfo {
	
	/*<id> is used to record the id of the node in Neo4j*/
	private long id;
	
	/*<cypherStentance> is used to store the cypher sentence 
	 * by which the node is stored into the Neo4j database*/
	private String cypherSentence="";
	
	public NodeInfo(String sentence){
		this.cypherSentence=sentence;
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
