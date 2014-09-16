package test;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class HWVisitor extends ASTVisitor{
	private PackageDeclaration pDecNode;
	private TypeDeclaration tDecNode;
	
	/**
	 * 
	 */
	public boolean visit(PackageDeclaration node) { 
		this.pDecNode=node;
		return false;
	}
	
	public boolean visit(TypeDeclaration node) {
		this.tDecNode=node;
		return false;
	}
	
	public PackageDeclaration getpDecNode() {
		return pDecNode;
	}
	public void setpDecNode(PackageDeclaration pDecNode) {
		this.pDecNode = pDecNode;
	}
	public TypeDeclaration gettDecNode() {
		return tDecNode;
	}
	public void settDecNode(TypeDeclaration tDecNode) {
		this.tDecNode = tDecNode;
	}
	
	public static void main(){
		String r="[[30000000000]]";
		String r_2="";
		for(int i=0;i<r.length();i++){
		}
	}
}
