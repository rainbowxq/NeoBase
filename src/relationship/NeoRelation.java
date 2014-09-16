package relationship;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * the parent class of all relationships in Neo4j
 * @author xiaoq_zhu
 *
 */
public class NeoRelation {
	private  NeoNode from;
	private NeoNode to;
	private String relationType;
	
	public NeoRelation(NeoNode from,NeoNode to,String type){
		this.from=from;
		this.to=to;
		this.setRelationType(type);
	}
	
	public NeoNode getFrom() {
		return from;
	}
	public void setFrom(NeoNode from) {
		this.from = from;
	}
	public NeoNode getTo() {
		return to;
	}
	public void setTo(NeoNode to) {
		this.to = to;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	
	
	
	
}
