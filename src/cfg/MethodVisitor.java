package cfg;


import java.util.ArrayList;
import java.util.List;

import node.NodeInfo;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import analyse.Query;
import relationship.Relation;




public class MethodVisitor extends ASTVisitor {
	private List<SENode> seNodes=new ArrayList<SENode>();
	private List<ASTNode> cfgN=new ArrayList<ASTNode>();
	private List<NodeInfo> infos=new ArrayList<NodeInfo>();
	private List<Relation> cfgR=new ArrayList<Relation>();
	private static final String rtype="CFG";
	
	public boolean visit(MethodDeclaration node) {
		String key=node.resolveBinding().getKey();
		SENode start_end=new SENode(key);
		Block block=node.getBody();
		SEInfo info=stmtCfg(block,key);
		start_end.setStart_to(info.getStart());
		start_end.setEnd_from(info.getEnds());
		this.seNodes.add(start_end);
		return false;
	}
	
	public SEInfo stmtCfg(Statement stmt,String key){
		
		List<SEInfo> tmpInfos;
		SEInfo tmpinfo;
		List<Expression> exps;
		switch(stmt.getNodeType()){
		
		case ASTNode.ASSERT_STATEMENT:
//			exps=new ArrayList<Expression>();
//			Expression exp=((AssertStatement)stmt).getExpression();
			Expression msg=((AssertStatement)stmt).getMessage();
//			if(exp!=null)
//				exps.add(exp);
//			if(msg!=null)
//				exps.add(msg);
//			tmpInfos=this.computeInfos(exps, key);
			tmpinfo=this.expCfg(msg, key);
			if(tmpinfo!=null)
				return tmpinfo;
			else
				return this.addStmtNode(stmt);
			
		case ASTNode.BLOCK:
			List<Statement> stmts=((Block)stmt).statements();
			tmpInfos=new ArrayList<SEInfo>();
//			System.out.println("aaaaaaaaaaaaaaaaaaaaa  "+stmts.size());
			for(int i=0;i<stmts.size();i++){
				tmpinfo=stmtCfg(stmts.get(i),key);
				assert(tmpinfo!=null);
				tmpInfos.add(tmpinfo);
			}
			SEInfo tmpinfo2=this.tInfo(tmpInfos, key);
			if(tmpinfo2!=null)
				return tmpinfo2;
			else
				return this.addStmtNode(stmt);

		case ASTNode.BREAK_STATEMENT:
			break;
		case ASTNode.CONTINUE_STATEMENT:
			break;
		case ASTNode.DO_STATEMENT:
			SEInfo info=new SEInfo();
			info.setStart(stmt);
			this.cfgN.add(stmt);
			this.infos.add(new NodeInfo(Query.statementQuery(stmt)));
			
			Statement dostmt=((DoStatement)stmt).getBody();
			SEInfo dostmtinfo=stmtCfg(dostmt,key);
			this.cfgR.add(new Relation(stmt,dostmtinfo.getStart(),rtype,key));
			
			
			Expression doexp=((DoStatement)stmt).getExpression();
			this.concatSE(dostmtinfo.getEnds(), doexp, key);
			info.addEnd(doexp);
			this.cfgN.add(doexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(doexp)));
			this.cfgR.add(new Relation(doexp,stmt,rtype,key));
			
			
			
//			tmpinfo=expCfg(doexp,key);
//			if(tmpinfo!=null){
//				this.concatSE(dostmtinfo.getEnds(), tmpinfo.getStart(), key);
//				info.addEnds(tmpinfo.getEnds());
//				this.concatSE(tmpinfo.getEnds(), stmt, key);
//			}
//			else{
//				this.concatSE(dostmtinfo.getEnds(), doexp, key);
//				info.addEnd(doexp);
//				this.cfgN.add(doexp);
//				this.infos.add(new NodeInfo(Query.expressionQuery(doexp)));
//				this.cfgR.add(new Relation(doexp,stmt,rtype,key));
//			}
			return info;
			
		case ASTNode.ENHANCED_FOR_STATEMENT:
		break;
		case ASTNode.EXPRESSION_STATEMENT:
			Expression esexp=((ExpressionStatement)stmt).getExpression();
			tmpinfo=this.expCfg(esexp, key);
			if(tmpinfo!=null)
				return tmpinfo;
			else
				return this.addStmtNode(stmt);

		case ASTNode.FOR_STATEMENT:
		break;
		case ASTNode.IF_STATEMENT:
			SEInfo ifinfo=new SEInfo();
			Expression ifexp=((IfStatement)stmt).getExpression();
			ifinfo.setStart(ifexp);
			this.cfgN.add(ifexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(ifexp)));
			
			Statement tstmt=((IfStatement)stmt).getThenStatement();
			tmpinfo=stmtCfg(tstmt,key);
			this.cfgR.add(new Relation(ifexp,tmpinfo.getStart(),rtype,key));
			ifinfo.addEnds(tmpinfo.getEnds());
			
			Statement estmt=((IfStatement)stmt).getElseStatement();
			if(estmt!=null){
				tmpinfo=stmtCfg(estmt,key);
				this.cfgR.add(new Relation(ifexp,tmpinfo.getStart(),rtype,key));
				ifinfo.addEnds(tmpinfo.getEnds());
			}
			else{
				ifinfo.addEnd(ifexp);
			}
			return ifinfo;
		case ASTNode.RETURN_STATEMENT:
		break;
		case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
		break;
		case ASTNode.SWITCH_CASE:
		break;
		case ASTNode.SWITCH_STATEMENT:
		break;
		case ASTNode.SYNCHRONIZED_STATEMENT:
		break;
		case ASTNode.THROW_STATEMENT:
		break;
		case ASTNode.TRY_STATEMENT:
		break;
		case ASTNode.TYPE_DECLARATION_STATEMENT:
			return this.addStmtNode(stmt);

		case ASTNode.VARIABLE_DECLARATION_STATEMENT:
			exps=new ArrayList<Expression>();
			List<VariableDeclarationFragment>fragments=((VariableDeclarationStatement)stmt).fragments();
			for(int i=0;i<fragments.size();i++){
				Expression iniexp=fragments.get(i).getInitializer();
				if(iniexp!=null){
					exps.add(iniexp);
				}
			}
			tmpInfos=this.computeInfos(exps, key);
			tmpinfo=this.tInfo(tmpInfos, key);
			if(tmpinfo!=null)
				return tmpinfo;
			else
				return this.addStmtNode(stmt);
			
		case ASTNode.WHILE_STATEMENT:
			SEInfo winfo=new SEInfo();
			Expression wexp=((WhileStatement)stmt).getExpression();
			winfo.setStart(wexp);
			winfo.addEnd(wexp);
			
			this.cfgN.add(wexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(wexp)));
			
			Statement wbody=((WhileStatement)stmt).getBody();
			SEInfo wbodyinfo=stmtCfg(wbody,key);
			this.cfgR.add(new Relation(wexp,wbodyinfo.getStart(),rtype,key));
			this.concatSE(wbodyinfo.getEnds(), winfo.getStart(), key);
			return winfo;
		}
		return null;
	}
	
	/**
	 * single statement node in cfg
	 */
	public SEInfo addStmtNode(Statement stmt){
		SEInfo info=new SEInfo();
		this.cfgN.add(stmt);
		this.infos.add(new NodeInfo(Query.statementQuery(stmt)));
		
		info.setStart(stmt);
		info.addEnd(stmt);
		return info;
	}
	
	@SuppressWarnings("unchecked")
	public SEInfo expCfg(Expression exp,String key){
		
		List<SEInfo> tmpInfos;
		List<Expression> exps;
		switch(exp.getNodeType()){
		case ASTNode.ARRAY_ACCESS:
			exps=new ArrayList<Expression>();
			exps.add(((ArrayAccess)exp).getArray());
			exps.add(((ArrayAccess)exp).getIndex());
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);

		case ASTNode.ARRAY_INITIALIZER:
			exps=((ArrayInitializer)exp).expressions();
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);
			
		case ASTNode.ARRAY_CREATION:
			exps=((ArrayCreation)exp).dimensions();
			ArrayInitializer ini=((ArrayCreation)exp).getInitializer();
			if (ini!=null)
				exps.add(ini);
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);
		

		case ASTNode.ASSIGNMENT:
			exps=new ArrayList<Expression>();
			exps.add(((Assignment)exp).getLeftHandSide());
			exps.add(((Assignment)exp).getRightHandSide());
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);

			
		case ASTNode.CAST_EXPRESSION:
			Expression cexp=((CastExpression)exp).getExpression();
			return expCfg(cexp,key);
		
		case ASTNode.CLASS_INSTANCE_CREATION:
			exps=new ArrayList<Expression>();
			Expression cicexp=((ClassInstanceCreation)exp).getExpression();
			if(cicexp!=null)
				exps.add(cicexp);
			exps.addAll(((ClassInstanceCreation)exp).arguments());
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);

		case ASTNode.CONDITIONAL_EXPRESSION:
			SEInfo info=new SEInfo();
			Expression conexp=((ConditionalExpression)exp).getExpression();
//			SEInfo coninfo=expCfg(conexp, key);
//			List<ASTNode> endnodes=new ArrayList<ASTNode>();
//			if(coninfo!=null){
//				info.setStart(coninfo.getStart());
//				endnodes.addAll(coninfo.getEnds());
//			}
//			else{
			this.cfgN.add(conexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(conexp)));
				
			info.setStart(conexp);
//			endnodes.add(conexp);
//			}
			//
			Expression thenExp=((ConditionalExpression)exp).getThenExpression();
			SEInfo theninfo=expCfg(thenExp,key);
			if(theninfo!=null){
				this.cfgR.add(new Relation(conexp, theninfo.getStart(),rtype, key));
				info.addEnds(theninfo.getEnds());
			}
			else{
				this.cfgR.add(new Relation(conexp, thenExp,rtype, key));
				this.cfgN.add(thenExp);
				this.infos.add(new NodeInfo(Query.expressionQuery(thenExp)));
				info.addEnd(thenExp);
			}
			//
			Expression elseExp=((ConditionalExpression)exp).getElseExpression();
			SEInfo elseinfo=expCfg(elseExp,key);
			if(elseinfo!=null){
				this.cfgR.add(new Relation(conexp, elseinfo.getStart(), rtype,key));
				info.addEnds(elseinfo.getEnds());
			}
			else{
				this.cfgR.add(new Relation(conexp, elseExp,rtype, key));
				this.cfgN.add(elseExp);
				this.infos.add(new NodeInfo(Query.expressionQuery(elseExp)));
				info.addEnd(elseExp);
			}
			return info;
			
		case ASTNode.FIELD_ACCESS:
			Expression faexp=((FieldAccess)exp).getExpression();
			return expCfg(faexp,key);
			
		case ASTNode.INFIX_EXPRESSION:
			exps=new ArrayList<Expression>();
			Expression lop=((InfixExpression)exp).getLeftOperand();
			Expression rop=((InfixExpression)exp).getRightOperand();
			exps.add(lop);
			exps.add(rop);
			exps.addAll(((InfixExpression)exp).extendedOperands());
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);

		case ASTNode.INSTANCEOF_EXPRESSION:
			Expression insofexp=((InstanceofExpression)exp).getLeftOperand();
			return expCfg(insofexp,key);

		case ASTNode.METHOD_INVOCATION:
			exps=new ArrayList<Expression>();
			Expression micexp=((MethodInvocation)exp).getExpression();
			if(micexp!=null)
				exps.add(micexp);
			exps.addAll(((MethodInvocation)exp).arguments());
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);

		case ASTNode.PARENTHESIZED_EXPRESSION:
			Expression pexp=((ParenthesizedExpression)exp).getExpression();
			return expCfg(pexp,key);

		case ASTNode.POSTFIX_EXPRESSION:
			Expression postexp=((PostfixExpression)exp).getOperand();
			return expCfg(postexp,key);
			
		case ASTNode.PREFIX_EXPRESSION:
			Expression preexp=((PrefixExpression)exp).getOperand();
			return expCfg(preexp,key);

		case ASTNode.SUPER_METHOD_INVOCATION:
			exps=((SuperMethodInvocation)exp).arguments();
			tmpInfos=this.computeInfos(exps, key);
			return this.tInfo(tmpInfos, key);
		}
		return null;
	}
	
	public List<SEInfo> computeInfos(List<Expression> exps,String key){
		List<SEInfo> infos=new ArrayList<SEInfo>();
		if(exps.size()>0){
			for(int i=0;i<exps.size();i++){
				SEInfo tmpInfo=expCfg(exps.get(i),key);
				if(tmpInfo!=null)
				infos.add(tmpInfo);
			}
		}
		if(infos.size()>0)
			return infos;
		else
			return null;
	}
	/**
	 * connect the ends of info1 to the start of info2
	 * @param info1
	 * @param info2
	 */
	public void concatSE(List<ASTNode> ends,ASTNode nextnode,String key){
		
		for(int j=0;j<ends.size();j++){
			this.cfgR.add(new Relation(ends.get(j),nextnode,rtype,key));
		}
	}
	
	/**
	 * the total of the linkage of several non-null SEInfos
	 * @param tmpInfos
	 * @param key
	 * @return
	 */
	public SEInfo tInfo(List<SEInfo> tmpInfos,String key){
		if(tmpInfos!=null&&tmpInfos.size()>0){
			SEInfo info=new SEInfo();
			info.setStart(tmpInfos.get(0).getStart());
			info.setEnds(tmpInfos.get(tmpInfos.size()-1).getEnds());
			for(int i=1;i<tmpInfos.size();i++){
				this.concatSE(tmpInfos.get(i-1).getEnds(), tmpInfos.get(i).getStart(), key);
			}
			return info;
		}
		else
			return null;
	}
	
	public SEInfo setInfoSelf(Expression exp,String key){
		SEInfo info=new SEInfo();
		this.cfgN.add(exp);
		this.infos.add(new NodeInfo(Query.expressionQuery(exp)));
		info.setStart(exp);
		info.addEnd(exp);
		return info;
	}
	
	/**
	 * the data struture to record start and end nodes
	 * @author xiaoq_zhu
	 *
	 */
	class SEInfo{
		ASTNode start;
		List<ASTNode> ends=new ArrayList<ASTNode>();
		
		public void setStart(ASTNode start){
			this.start=start;
		}
		
		public void addEnd(ASTNode end){
			this.ends.add(end);
		}
		
		public ASTNode getStart(){
			return this.start;
		}
		
		public List<ASTNode> getEnds(){
			return this.ends;
		}
		public void setEnds(List<ASTNode> ends){
			this.ends=ends;
		}
		public void addEnds(List<ASTNode> ends){
			this.ends.addAll(ends);
		}
	}
	
	public static void main(String[] args){
		int k=3;
		int a=3;
		A:while (k-->0){
		B:	for(int j=1;j<10;j+=3)
		C: {
			System.out.println(j);
				if(a>10)
					break A;
				else if(a>5)
					break B;
				else if (a>2)
					continue B;
				else
					break C;
			}
		}
	}

	public List<NodeInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<NodeInfo> infos) {
		this.infos = infos;
	}
	
	public List<ASTNode> getCfgN(){
		return this.cfgN;
	}
	public List<Relation> getCfgR(){
		return this.cfgR;
	}
	public List<SENode> getSeNodes(){
		return this.seNodes;
	}
	
}
