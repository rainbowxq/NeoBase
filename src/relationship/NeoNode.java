package relationship;

import org.eclipse.jdt.core.dom.ASTNode;

public class NeoNode {
	private ASTNode node;
	private long id;
	
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
}
