package relationship;

import org.eclipse.jdt.core.dom.ASTNode;


/**
 * the parent class of all relationships in Neo4j
 * @author xiaoq_zhu
 *
 */
public class Relation {
	private  ASTNode from;
	private ASTNode to;
	private String relationType;
	
	public Relation(ASTNode from,ASTNode to,String type){
		this.from=from;
		this.to=to;
		this.setRelationType(type);
	}
	
	public ASTNode getFrom() {
		return from;
	}
	public void setFrom(ASTNode from) {
		this.from = from;
	}
	public ASTNode getTo() {
		return to;
	}
	public void setTo(ASTNode to) {
		this.to = to;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	
	
	
	
}
