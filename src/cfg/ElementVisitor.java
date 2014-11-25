package cfg;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * visitors for nodes in cfg
 * @author xiaoq_zhu
 *
 */
public class ElementVisitor extends ASTVisitor {
	private ASTNode start=null;
	private List<ASTNode> ends=null;
	public ASTNode getStart() {
		return start;
	}
	public void setStart(ASTNode start) {
		this.start = start;
	}
	public List<ASTNode> getEnds() {
		return ends;
	}
	public void setEnds(List<ASTNode> ends) {
		this.ends = ends;
	}
	
}
