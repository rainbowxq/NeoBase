package test;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import collector.CUnit;
import relationship.NeoNode;
import relationship.NeoRelation;
import relationship.RelationshipType;

public class NeoHello {
	
	private ArrayList<NeoNode> nodes=new ArrayList<NeoNode>();
	private ArrayList<NeoRelation> relations=new ArrayList<NeoRelation>();
	
//	public  String program;
	
	public void analyzeHello(){
		CUnit unit=new CUnit();
		unit.setProgram("/home/xiaoq_zhu/zxq/workspace/HelloWorld/src/test/HelloWorld.java");
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit.getProgram().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit helloworld = (CompilationUnit)parser.createAST(null) ;
		HWVisitor visitor=new HWVisitor();
		helloworld.accept(visitor);
		NeoNode pDecNode=new NeoNode(visitor.getpDecNode());
		NeoNode tDecNode=new NeoNode(visitor.gettDecNode());
		this.nodes.add(pDecNode);
		this.nodes.add(tDecNode);
		this.analyze(tDecNode,(TypeDeclaration)tDecNode.getNode());
	}
	/**
	 * the node PackageDeclaration
	 * @param node
	 */
	
	
	public String store(PackageDeclaration node){
		
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: PackageDeclaration { name : {pkgName} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("pkgName", node.getName().getFullyQualifiedName());
		//params.put("pkgKey", node.resolveBinding().getKey());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	
	
	/**
	 * TypeDeclaration
	 * @param node
	 */
	public void analyze(NeoNode Node ,TypeDeclaration node){
		/**
		 * get modifiers
		 */
		List<Modifier> modifiers= node.modifiers();
		for(int i=0;i<modifiers.size();i++){
			NeoNode modifier=new NeoNode(modifiers.get(i));
			this.nodes.add(modifier);
			NeoRelation relation=new NeoRelation(Node,
								modifier,
								RelationshipType.MODIFIERS);
		}
		/**
		 * BODY_DECLARATIONS
		 */
		MethodDeclaration[] methods=node.getMethods();
		for(int i=0;i<methods.length;i++){
			NeoNode method=new NeoNode(methods[i]);
			this.nodes.add(method);
			this.relations.add(new NeoRelation(Node,
					method,
					RelationshipType.BODY_DECLARATIONS));
			this.analyze(method,(MethodDeclaration)method.getNode());
		}
//		node.getModifiers()
//		int mFlags=node.getFlags();
//		for(int )
	}
	
	/**
	 * 2 properties:  name , INTERFACE
	 * @param node
	 * @return
	 */
	public String store(TypeDeclaration node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: TypeDeclaration { name : {typeName},INTERFACE : {isInterface} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("typeName", node.getName().getFullyQualifiedName());
		params.put("isInterface", node.isInterface());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	
	
	/**
	 * MethodDeclaration
	 * @param node
	 */
	public void analyze(NeoNode Node,MethodDeclaration node){
		/**
		 * get modifiers
		 */
		List<Modifier> modifiers= node.modifiers();
		for(int i=0;i<modifiers.size();i++){
			NeoNode modifier=new NeoNode(modifiers.get(i));
			this.nodes.add(modifier);
			this.relations.add(new NeoRelation(Node,modifier,RelationshipType.MODIFIERS));
		}
		/**
		 * return type
		 */
		NeoNode rType=new NeoNode((PrimitiveType) node.getReturnType2());
		this.nodes.add(rType);
		this.relations.add(new NeoRelation(Node,rType,RelationshipType.RETURN_TYPE));
		/**
		 * PARAMETERS 
		 */
		NeoNode svd=new NeoNode((SingleVariableDeclaration) node.parameters().get(0));
		this.nodes.add(svd);
		this.relations.add(new NeoRelation(Node,svd,RelationshipType.PARAMETERS));
		this.analyze(svd,(SingleVariableDeclaration)svd.getNode());
		/**
		 * BODY
		 */
		NeoNode block=new NeoNode(node.getBody());
		this.nodes.add(block);
		this.relations.add(new NeoRelation(Node,block,RelationshipType.BODY));
		this.analyze(block,(Block)block.getNode());
	}
	/**
	 * 2 properties: name, CONSTRUCTOR
	 * @param node
	 * @return
	 */
	public String store(MethodDeclaration node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: MethodDeclaration { name : {methodName},CONSTRUCTOR : {isConstructor} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("methodName", node.getName().getFullyQualifiedName());
		params.put("isConstructor", node.isConstructor());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	/**
	 * SingleVariableDeclaration
	 * @param node
	 */
	public void analyze(NeoNode Node,SingleVariableDeclaration node){
		NeoNode aType=new NeoNode((ArrayType) node.getType());
		this.nodes.add(aType);
		this.relations.add(new NeoRelation(Node,aType,RelationshipType.TYPE));
		this.analyze(aType,(ArrayType)aType.getNode());
		
		
	}
	public String store(SingleVariableDeclaration node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: SingleVariableDeclaration { name : {sName} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("sName", node.getName().getFullyQualifiedName());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	/**
	 * Block
	 * @param node
	 */
	public void analyze(NeoNode Node,Block node){
		List<Statement> statements=node.statements();
//		System.out.println(statements.size());
		NeoNode eStatement=new NeoNode((ExpressionStatement) statements.get(0));
		this.nodes.add(eStatement);
		this.relations.add(new NeoRelation(Node,eStatement,RelationshipType.STATEMENTS));
		this.analyze(eStatement,(ExpressionStatement)eStatement.getNode());
	}
	
	public String store(Block node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: Block) RETURN id(n)");

		System.out.println(query.toString());
		return query.toString();
	}
	/**
	 * ExpressionStatement
	 * @param node
	 */
	public void analyze(NeoNode Node,ExpressionStatement node){
		NeoNode mInvoke=new NeoNode((MethodInvocation) node.getExpression());
		this.nodes.add(mInvoke);
		this.relations.add(new NeoRelation(Node,mInvoke,RelationshipType.EXPRESSION));
		this.analyze(mInvoke,(MethodInvocation)mInvoke.getNode());
	}
	public String store(ExpressionStatement node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: ExpressionStatement) RETURN id(n)");

		System.out.println(query.toString());
		return query.toString();
	}
	/**
	 * MethodInvocation
	 * @param node
	 */
	public void analyze(NeoNode Node,MethodInvocation node){
		NeoNode string=new NeoNode((StringLiteral) node.arguments().get(0));
		this.nodes.add(string);
		this.relations.add(new NeoRelation(Node,string,RelationshipType.ARGUMENTS));
	}
	
	public String store(MethodInvocation node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: MethodInvocation { name : {methodName},EXPRESSION : {exp} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("methodName", node.getName().getFullyQualifiedName());
		params.put("exp", ((QualifiedName)node.getExpression()).getFullyQualifiedName());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	/**
	 * Modifier
	 * @param node
	 */
	public String store(Modifier  node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: Modifier { KEY : {key} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("key", node.getKeyword().toString());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	
	/**
	 * PrimitiveType
	 * @param node
	 */
	public String store(PrimitiveType  node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: Type { kind : {tKind},PRIMITIVE_TYPE_CODE : {code} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("code", node.getPrimitiveTypeCode().toString());
		params.put("tKind", "PrimitiveType");
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	/**
	 * ArrayType
	 * @param node
	 * @return
	 */
	
	public void analyze(NeoNode Node,ArrayType node){
		NeoNode sType=new NeoNode((SimpleType) node.getComponentType());
		this.nodes.add(sType);
		this.relations.add(new NeoRelation(Node,sType,RelationshipType.COMPONENT_TYPE));
	}
	
	public String store(ArrayType  node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: Type { kind : {tKind} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("tKind", "ArrayType");
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	/**
	 * SimpleType
	 */
		
	public String store(SimpleType  node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: Type { kind : {tKind}, name : {tName}}) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("tName", node.getName().getFullyQualifiedName());
		params.put("tKind", "SimpleType");
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	
	/**
	 * StringLiteral
	 * @param args
	 */
	public String store(StringLiteral  node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: StringLiteral { ESCAPED_VALUE : {value} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("value", node.getEscapedValue());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	
	public String executeStore(ASTNode node){
		if(node instanceof PackageDeclaration)
			return this.store((PackageDeclaration) node);
		else if (node instanceof TypeDeclaration)
			return this.store((TypeDeclaration) node);
		else if (node instanceof MethodDeclaration)
			return this.store((MethodDeclaration) node);
		else if (node instanceof Modifier)
			return this.store((Modifier)node);
		else if (node instanceof Block)
			return this.store((Block)node);
		else if (node instanceof MethodInvocation)
			return this.store((MethodInvocation)node);
		else if (node instanceof ExpressionStatement)
			return this.store((ExpressionStatement)node);
		else if (node instanceof PrimitiveType)
			return this.store((PrimitiveType)node);
		else if (node instanceof ArrayType)
			return this.store((ArrayType)node);
		else if (node instanceof SimpleType)
			return this.store((SimpleType)node);
		else if (node instanceof StringLiteral)
			return this.store((StringLiteral)node);
		else if (node instanceof SingleVariableDeclaration)
			return this.store((SingleVariableDeclaration)node);
		else 
			return null;
		
	}
	public ArrayList<NeoNode> getNodes(){
		return this.nodes;
	}
	
	public ArrayList<NeoRelation> getRelations(){
		return this.relations;
	}
	
	public static void main(String[] args){
		NeoHello hello=new NeoHello();
		hello.analyzeHello();
		for(int i=0;i<hello.nodes.size();i++){
			hello.executeStore(hello.nodes.get(i).getNode());
		}
		
//		System.out.println(NeoHello.program);
	}	
}
