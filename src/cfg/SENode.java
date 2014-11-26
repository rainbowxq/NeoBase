package cfg;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * the start and end node of cfg 
 * @author xiaoq_zhu
 *
 */
public class SENode {
	private String m_key;
	private ASTNode start_to=null;
	private List<ASTNode> end_from=new ArrayList<ASTNode>();
	
	public SENode(String key){
		this.m_key=key;
	}
	
	public String getM_key() {
		return m_key;
	}
	public void setM_key(String m_key) {
		this.m_key = m_key;
	}

	public ASTNode getStart_to() {
		return start_to;
	}

	public void setStart_to(ASTNode start_to) {
		this.start_to = start_to;
	}

	public List<ASTNode> getEnd_from() {
		return end_from;
	}

//	public void setEnd_from(List<ASTNode> end_from) {
//		this.end_from = end_from;
//	}
	
	public void addEnd_from(ASTNode end){
		this.end_from.add(end);
	}
	public void addEnds_from(List<ASTNode> ends){
		if(ends!=null)
			this.end_from.addAll(ends);
	}
	
}
