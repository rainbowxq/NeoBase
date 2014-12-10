package cfg;


import java.util.ArrayList;
import java.util.List;

import node.NodeInfo;
import node.SENode;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;

import ast.Query;
import relationship.Relation;




public class MethodVisitor extends ASTVisitor {
	private List<SENode> seNodes=new ArrayList<SENode>();
	private List<ASTNode> cfgN=new ArrayList<ASTNode>();
	private List<NodeInfo> infos=new ArrayList<NodeInfo>();
	private List<Relation> cfgR=new ArrayList<Relation>();
	private static final String rtype="CFG";
	
	private List<ASTNode> bcNodes=new ArrayList<ASTNode>();
	private List<ASTNode> pNodes=new ArrayList<ASTNode>();
	
	public boolean visit(MethodDeclaration node) {
		Block block=node.getBody();
		if(block!=null){
			String key=node.resolveBinding().getKey();
			SENode start_end=new SENode(key);
			SEInfo info=stmtCfg(block,start_end);
			start_end.setStart_to(info.getStart());
			start_end.addEnds_from(info.getEnds());
			this.seNodes.add(start_end);
		}
		return false;
	}
	
	public SEInfo stmtCfg(Statement stmt,SENode senode){
		String key=senode.getM_key();
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
//			if(msg!=null){
//				tmpinfo=this.expCfg(msg, key);
//				if(tmpinfo!=null)
//					return tmpinfo;
//			}
			return this.addStmtNode(stmt);
			
		case ASTNode.BLOCK:
			List<Statement> stmts=((Block)stmt).statements();
			tmpInfos=new ArrayList<SEInfo>();
//			System.out.println("aaaaaaaaaaaaaaaaaaaaa  "+stmts.size());
			for(int i=0;i<stmts.size();i++){
				tmpinfo=stmtCfg(stmts.get(i),senode);
				assert(tmpinfo!=null);
				tmpInfos.add(tmpinfo);
			}
			SEInfo tmpinfo2=this.tInfo(tmpInfos, key);
			if(tmpinfo2!=null){
				this.checkBC(stmt, tmpinfo2, null, null);
				return tmpinfo2;
			}
			else
				return this.addStmtNode(stmt);

//		case ASTNode.BREAK_STATEMENT:
//		case ASTNode.CONTINUE_STATEMENT:
//		case ASTNode.SWITCH_CASE:
//		case ASTNode.SYNCHRONIZED_STATEMENT:
//		case ASTNode.THROW_STATEMENT:
//		case ASTNode.TYPE_DECLARATION_STATEMENT:
//			return this.addStmtNode(stmt);
			
		case ASTNode.DO_STATEMENT:
			SEInfo info=new SEInfo();
			info.setStart(stmt);
			this.cfgN.add(stmt);
			this.infos.add(new NodeInfo(Query.statementQuery(stmt)));
			
			Statement dostmt=((DoStatement)stmt).getBody();
			SEInfo dostmtinfo=stmtCfg(dostmt,senode);
			this.cfgR.add(new Relation(stmt,dostmtinfo.getStart(),rtype,key));
			
			
			Expression doexp=((DoStatement)stmt).getExpression();
			this.concatSE(dostmtinfo.getEnds(), doexp, key);
			info.addEnd(doexp);
			this.cfgN.add(doexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(doexp)));
			this.cfgR.add(new Relation(doexp,stmt,rtype,key));
			
			//check if the break or continue infomation is needed to add
			this.checkBC(stmt, info, doexp, key);
			
			
			return info;
			
		case ASTNode.ENHANCED_FOR_STATEMENT:
			SEInfo efinfo=new SEInfo();
			Expression efexp=((EnhancedForStatement)stmt).getExpression();
			this.cfgN.add(efexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(efexp)));
			efinfo.setStart(efexp);
			
			Statement efbody=((EnhancedForStatement)stmt).getBody();
			SEInfo efbodyinfo=this.addStmtNode(efbody);
			this.cfgR.add(new Relation(efexp,efbodyinfo.getStart(),rtype,key));
			this.concatSE(efbodyinfo.getEnds(), efexp, key);
			
			efinfo.addEnd(efexp);
			this.checkBC(stmt, efbodyinfo, efexp, key);
			return efinfo;
			
		
		case ASTNode.EXPRESSION_STATEMENT:
//			Expression esexp=((ExpressionStatement)stmt).getExpression();
//			tmpinfo=this.expCfg(esexp, key);
//			if(tmpinfo!=null)
//				return tmpinfo;
//			else
				return this.addStmtNode(stmt);

		case ASTNode.FOR_STATEMENT:
			SEInfo forinfo=new SEInfo();
			List<ASTNode> startparts=new ArrayList<ASTNode>();
			List<Expression> iniExps=((ForStatement)stmt).initializers();

			
			Expression forexp=((ForStatement)stmt).getExpression();
			startparts.addAll(iniExps);
			if(forexp!=null)
				startparts.add(forexp);
			else
				startparts.add(stmt);
			
			forinfo.setStart(startparts.get(0));
			int spsize=startparts.size();
			for(int i=0;i<spsize;i++){
				ASTNode tmpNode=startparts.get(i);
				this.cfgN.add(tmpNode);
				if(tmpNode instanceof Statement)
					this.infos.add(new NodeInfo(Query.statementQuery((Statement) tmpNode)));
				else if(tmpNode instanceof Expression)
					this.infos.add(new NodeInfo(Query.expressionQuery((Expression) tmpNode)));
				else
					assert false :"the node type can only be Statement or Expression";
				if(i>0)
					this.cfgR.add(new Relation(startparts.get(i-1),startparts.get(i),rtype,key));
			}
			
				
			/**/
			Statement forbody=((ForStatement)stmt).getBody();
			tmpinfo=this.stmtCfg(forbody, senode);
			this.cfgR.add(new Relation(startparts.get(spsize-1),tmpinfo.getStart(),rtype,key));
			
			
			
			List<Expression> upExps=((ForStatement)stmt).updaters();
			int usize=upExps.size();
			for(int i=0;i<usize;i++){
				this.cfgN.add(upExps.get(i));
				this.infos.add(new NodeInfo(Query.expressionQuery(upExps.get(i))));
				if(i>0)
					this.cfgR.add(new Relation(upExps.get(i-1),upExps.get(i),rtype,key));
			}
			List<ASTNode> tmpends=new ArrayList<ASTNode>();
			if(usize>0){
				this.concatSE(tmpinfo.getEnds(), upExps.get(0), key);
				tmpends.add(upExps.get(usize-1));
			}
			else
				tmpends.addAll(tmpinfo.getEnds());
			
			if(forexp!=null){
				this.concatSE(tmpends, forexp, key);
				forinfo.addEnd(forexp);
				if(usize>0)
					this.checkBC(stmt, forinfo, upExps.get(0), key);
				else
					this.checkBC(stmt, forinfo, forexp, key);
			}
			else{
				this.concatSE(tmpends, stmt, key);
				this.checkBC(stmt, forinfo,stmt, key);
//				forinfo.addEnd(stmt);
			}
			return forinfo;
			
			
		case ASTNode.IF_STATEMENT:
			SEInfo ifinfo=new SEInfo();
			Expression ifexp=((IfStatement)stmt).getExpression();
			ifinfo.setStart(ifexp);
			this.cfgN.add(ifexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(ifexp)));
			
			Statement tstmt=((IfStatement)stmt).getThenStatement();
			tmpinfo=stmtCfg(tstmt,senode);
			this.cfgR.add(new Relation(ifexp,tmpinfo.getStart(),rtype,key));
			ifinfo.addEnds(tmpinfo.getEnds());
			
			Statement estmt=((IfStatement)stmt).getElseStatement();
			if(estmt!=null){
				tmpinfo=stmtCfg(estmt,senode);
				this.cfgR.add(new Relation(ifexp,tmpinfo.getStart(),rtype,key));
				ifinfo.addEnds(tmpinfo.getEnds());
			}
			else{
				ifinfo.addEnd(ifexp);
			}
			this.checkBC(stmt, ifinfo, null, null);
			return ifinfo;
			
		case ASTNode.LABELED_STATEMENT:
			Statement lbody=((LabeledStatement)stmt).getBody();
			return this.stmtCfg(lbody, senode);
			
			
		case ASTNode.RETURN_STATEMENT:
//			Expression returnexp=((ReturnStatement)stmt).getExpression();
//			if(returnexp!=null){
//				tmpinfo=this.expCfg(returnexp, key);
//				if(tmpinfo!=null){
//					senode.addEnds_from(tmpinfo.getEnds());
//					tmpinfo.setEnds(null);
//					return tmpinfo;
//				}
//			}
//			senode.addEnd_from(stmt);
			return this.addStmtNode(stmt);
			

		case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
//			exps=new ArrayList<Expression>();
//			Expression micexp=((SuperConstructorInvocation)stmt).getExpression();
//			if(micexp!=null)
//				exps.add(micexp);
//			exps.addAll(((SuperConstructorInvocation)stmt).arguments());
//			tmpInfos=this.computeInfos(exps, key);
//			tmpinfo=this.tInfo(tmpInfos, key);
//			if(tmpinfo!=null)
//				return tmpinfo;
//			else
				return this.addStmtNode(stmt);
		
		
		case ASTNode.SWITCH_STATEMENT:
			SEInfo swinfo=new SEInfo();
			this.cfgN.add(stmt);
			this.infos.add(new NodeInfo(Query.statementQuery(stmt)));
			swinfo.setStart(stmt);
			
			List<Statement> swStmts=((SwitchStatement)stmt).statements();
			tmpInfos=new ArrayList<SEInfo>();
			for(int i=0;i<swStmts.size();i++){
				if(swStmts.get(i).getNodeType()==ASTNode.SWITCH_CASE){
					this.cfgR.add(new Relation(stmt,swStmts.get(i),rtype,key));
				}
//				if(swStmts.get(i).getNodeType()==ASTNode.BREAK_STATEMENT){
//					this.cfgN.add(swStmts.get(i));
//					this.infos.add(new NodeInfo(Query.statementQuery(swStmts.get(i))));
//					
//					swinfo.addEnd(swStmts.get(i));
//					tmpinfo=this.tInfo(tmpInfos, key);
//					this.concatSE(tmpinfo.getEnds(), swStmts.get(i), key);
//					tmpInfos=new ArrayList<SEInfo>();
//				}
				tmpInfos.add(this.stmtCfg(swStmts.get(i), senode));
			}
			tmpinfo=this.tInfo(tmpInfos, key);
			if(tmpinfo!=null)
				swinfo.addEnds(tmpinfo.getEnds());
			this.checkBC(stmt, swinfo, null, null);
			return swinfo;
		
		case ASTNode.TRY_STATEMENT:
			SEInfo tryinfo=new SEInfo();
			tryinfo.setStart(stmt);
			this.cfgN.add(stmt);
			this.infos.add(new NodeInfo(Query.statementQuery(stmt)));
			List<Block> bls=new ArrayList<Block>();
			bls.add(((TryStatement)stmt).getBody());
			
			List<CatchClause> ccs=((TryStatement)stmt).catchClauses();
			for(int i=0;i<ccs.size();i++){
				bls.add(ccs.get(i).getBody());
			}
			SEInfo tmpinfo3=null;
			if(((TryStatement)stmt).getFinally()!=null){
				tmpinfo3=this.stmtCfg(((TryStatement)stmt).getFinally(), senode);
			}
			
			for(int j=0;j<bls.size();j++){
				tmpinfo=this.stmtCfg(bls.get(j), senode);
				this.cfgR.add(new Relation(stmt,tmpinfo.getStart(),rtype,key));
				if(tmpinfo3!=null){
					this.concatSE(tmpinfo.getEnds(),tmpinfo3.getStart() , key);
				}
				else{
					tryinfo.addEnds(tmpinfo.getEnds());
				}
			}
			
			if(tmpinfo3!=null){
				tryinfo.addEnds(tmpinfo3.getEnds());
			}
			
			return tryinfo;		

		case ASTNode.VARIABLE_DECLARATION_STATEMENT:
//			exps=new ArrayList<Expression>();
//			List<VariableDeclarationFragment>fragments=((VariableDeclarationStatement)stmt).fragments();
//			for(int i=0;i<fragments.size();i++){
//				Expression iniexp=fragments.get(i).getInitializer();
//				if(iniexp!=null){
//					exps.add(iniexp);
//				}
//			}
//			tmpInfos=this.computeInfos(exps, key);
//			tmpinfo=this.tInfo(tmpInfos, key);
//			if(tmpinfo!=null)
//				return tmpinfo;
//			else
				return this.addStmtNode(stmt);
			
		case ASTNode.WHILE_STATEMENT:
			SEInfo winfo=new SEInfo();
			Expression wexp=((WhileStatement)stmt).getExpression();
			winfo.setStart(wexp);
			winfo.addEnd(wexp);
			
			this.cfgN.add(wexp);
			this.infos.add(new NodeInfo(Query.expressionQuery(wexp)));
			
			Statement wbody=((WhileStatement)stmt).getBody();
			SEInfo wbodyinfo=stmtCfg(wbody,senode);
			this.cfgR.add(new Relation(wexp,wbodyinfo.getStart(),rtype,key));
			this.concatSE(wbodyinfo.getEnds(), winfo.getStart(), key);
			
			this.checkBC(stmt, winfo, wexp, key);
			
			return winfo;
		}
		return this.addStmtNode(stmt);
	}
	
	public void checkBC(ASTNode node,SEInfo info,ASTNode cnode,String key){
		while(this.pNodes.contains(node)){
			int index=this.pNodes.indexOf(node);
			ASTNode bc=this.bcNodes.get(index);
			if(bc instanceof BreakStatement){
				info.addEnd(bc);
			}
			else if(bc instanceof ContinueStatement){
				this.cfgR.add(new Relation(bc,cnode,rtype,key));
			}
			this.pNodes.remove(index);
			this.bcNodes.remove(index);
		}
	}
	/**
	 * only for BreakStatement and ContinueStatement
	 * @param node
	 * @return
	 */
	public ASTNode getBCNode(ASTNode node){
		SimpleName label=null;
		if(node instanceof ContinueStatement){
			label=((ContinueStatement) node).getLabel();
		}
		else if(node instanceof BreakStatement){
			label=((BreakStatement) node).getLabel();
		}
		else
			assert false:"the input node is not a of kind BreakStatement or ContinueStatement";
		return this.findBCparent(node,label);
		
	}
	
	public ASTNode findBCparent(ASTNode node,SimpleName label){
		if(label==null){
			ASTNode parent=node.getParent();
			assert parent!=null;
			while(parent!=null){
				switch(parent.getNodeType()){
				case ASTNode.ENHANCED_FOR_STATEMENT:
				case ASTNode.DO_STATEMENT:
				case ASTNode.FOR_STATEMENT:
				case ASTNode.WHILE_STATEMENT:
					return parent;
				case ASTNode.SWITCH_STATEMENT:
					if(node instanceof BreakStatement)
						return parent;
				}
				parent=parent.getParent();
			}
		}
		else{
			ASTNode parent=node.getParent();
			assert parent!=null;
			while (parent!=null){
				if(parent.getNodeType()==ASTNode.LABELED_STATEMENT){
					String name=((LabeledStatement)parent).getLabel().getFullyQualifiedName();
					if(name.equals(label.getFullyQualifiedName()))
						return ((LabeledStatement)parent).getBody();
				}
				parent=parent.getParent();
			}
		}
		return node;
		
	}
	
	/**
	 * single statement node in cfg
	 */
	public SEInfo addStmtNode(Statement stmt){
		SEInfo info=new SEInfo();
		this.cfgN.add(stmt);
		this.infos.add(new NodeInfo(Query.statementQuery(stmt)));
		
		info.setStart(stmt);
		switch(stmt.getNodeType()){
			case ASTNode.BREAK_STATEMENT:
			case ASTNode.CONTINUE_STATEMENT:
				this.bcNodes.add(stmt);
				this.pNodes.add(getBCNode(stmt));
			case ASTNode.RETURN_STATEMENT:
				info.setEnds(null);
				break;
			default:
				info.addEnd(stmt);
		}
		return info;
	}
	
	@SuppressWarnings("unchecked")
//	public SEInfo expCfg(Expression exp,String key){
//		
//		List<SEInfo> tmpInfos=null;
//		List<Expression> exps=null;
//		switch(exp.getNodeType()){
//		case ASTNode.ARRAY_ACCESS:
//			exps=new ArrayList<Expression>();
//			exps.add(((ArrayAccess)exp).getArray());
//			exps.add(((ArrayAccess)exp).getIndex());
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//
//		case ASTNode.ARRAY_INITIALIZER:
//			exps=((ArrayInitializer)exp).expressions();
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//			
//		case ASTNode.ARRAY_CREATION:
//			exps=((ArrayCreation)exp).dimensions();
//			ArrayInitializer ini=((ArrayCreation)exp).getInitializer();
//			if (ini!=null){
//				if(exps==null)
//					exps=new ArrayList<Expression>();
//				exps.add(ini);
//			}
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//		
//
//		case ASTNode.ASSIGNMENT:
//			exps=new ArrayList<Expression>();
//			exps.add(((Assignment)exp).getLeftHandSide());
//			exps.add(((Assignment)exp).getRightHandSide());
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//
//			
//		case ASTNode.CAST_EXPRESSION:
//			Expression cexp=((CastExpression)exp).getExpression();
//			return expCfg(cexp,key);
//		
//		case ASTNode.CLASS_INSTANCE_CREATION:
//			exps=new ArrayList<Expression>();
//			Expression cicexp=((ClassInstanceCreation)exp).getExpression();
//			if(cicexp!=null)
//				exps.add(cicexp);
//			exps.addAll(((ClassInstanceCreation)exp).arguments());
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//
//		case ASTNode.CONDITIONAL_EXPRESSION:
//			SEInfo info=new SEInfo();
//			Expression conexp=((ConditionalExpression)exp).getExpression();
////			SEInfo coninfo=expCfg(conexp, key);
////			List<ASTNode> endnodes=new ArrayList<ASTNode>();
////			if(coninfo!=null){
////				info.setStart(coninfo.getStart());
////				endnodes.addAll(coninfo.getEnds());
////			}
////			else{
//			this.cfgN.add(conexp);
//			this.infos.add(new NodeInfo(Query.expressionQuery(conexp)));
//				
//			info.setStart(conexp);
////			endnodes.add(conexp);
////			}
//			//
//			Expression thenExp=((ConditionalExpression)exp).getThenExpression();
//			SEInfo theninfo=expCfg(thenExp,key);
//			if(theninfo!=null){
//				this.cfgR.add(new Relation(conexp, theninfo.getStart(),rtype, key));
//				info.addEnds(theninfo.getEnds());
//			}
//			else{
//				this.cfgR.add(new Relation(conexp, thenExp,rtype, key));
//				this.cfgN.add(thenExp);
//				this.infos.add(new NodeInfo(Query.expressionQuery(thenExp)));
//				info.addEnd(thenExp);
//			}
//			//
//			Expression elseExp=((ConditionalExpression)exp).getElseExpression();
//			SEInfo elseinfo=expCfg(elseExp,key);
//			if(elseinfo!=null){
//				this.cfgR.add(new Relation(conexp, elseinfo.getStart(), rtype,key));
//				info.addEnds(elseinfo.getEnds());
//			}
//			else{
//				this.cfgR.add(new Relation(conexp, elseExp,rtype, key));
//				this.cfgN.add(elseExp);
//				this.infos.add(new NodeInfo(Query.expressionQuery(elseExp)));
//				info.addEnd(elseExp);
//			}
//			return info;
//			
//		case ASTNode.FIELD_ACCESS:
//			Expression faexp=((FieldAccess)exp).getExpression();
//			return expCfg(faexp,key);
//			
//		case ASTNode.INFIX_EXPRESSION:
//			exps=new ArrayList<Expression>();
//			Expression lop=((InfixExpression)exp).getLeftOperand();
//			Expression rop=((InfixExpression)exp).getRightOperand();
//			exps.add(lop);
//			exps.add(rop);
//			exps.addAll(((InfixExpression)exp).extendedOperands());
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//
//		case ASTNode.INSTANCEOF_EXPRESSION:
//			Expression insofexp=((InstanceofExpression)exp).getLeftOperand();
//			return expCfg(insofexp,key);
//
//		case ASTNode.METHOD_INVOCATION:
//			exps=new ArrayList<Expression>();
//			Expression micexp=((MethodInvocation)exp).getExpression();
//			if(micexp!=null)
//				exps.add(micexp);
//			exps.addAll(((MethodInvocation)exp).arguments());
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//
//		case ASTNode.PARENTHESIZED_EXPRESSION:
//			Expression pexp=((ParenthesizedExpression)exp).getExpression();
//			return expCfg(pexp,key);
//
//		case ASTNode.POSTFIX_EXPRESSION:
//			Expression postexp=((PostfixExpression)exp).getOperand();
//			return expCfg(postexp,key);
//			
//		case ASTNode.PREFIX_EXPRESSION:
//			Expression preexp=((PrefixExpression)exp).getOperand();
//			return expCfg(preexp,key);
//
//		case ASTNode.SUPER_METHOD_INVOCATION:
//			exps=((SuperMethodInvocation)exp).arguments();
//			tmpInfos=this.computeInfos(exps, key);
//			return this.tInfo(tmpInfos, key);
//		}
//		return null;
//	}
	
//	public List<SEInfo> computeInfos(List<Expression> exps,String key){
//		List<SEInfo> infos=new ArrayList<SEInfo>();
//		if(exps.size()>0){
//			for(int i=0;i<exps.size();i++){
//				SEInfo tmpInfo=expCfg(exps.get(i),key);
//				if(tmpInfo!=null)
//				infos.add(tmpInfo);
//			}
//		}
//		if(infos.size()>0)
//			return infos;
//		else
//			return null;
//	}
	/**
	 * connect the ends of info1 to the start of info2
	 * @param info1
	 * @param info2
	 */
	public void concatSE(List<ASTNode> ends,ASTNode nextnode,String key){
		if(ends!=null){
			for(int j=0;j<ends.size();j++){
				this.cfgR.add(new Relation(ends.get(j),nextnode,rtype,key));
			}
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
//		List<ASTNode> bcs=new ArrayList<ASTNode>();
//		List<ASTNode> bcnode=new ArrayList<ASTNode>();
//		
//		
//		
//		public void addbcs(ASTNode bs){
//			this.bcs.add(bs);
//		}
//		
//		public void addbcss(List<ASTNode> bss){
//			this.bcs.addAll(bss);
//		}
//		
//		public List<ASTNode> getBs(){
//			return this.bcs;
//		}
//		
//		public void addBnode(ASTNode bcnode){
//			this.bcnode.add(bcnode);
//		}
//		
//		public void addBnodes(List<ASTNode> bcnodes){
//			this.bcnode.addAll(bcnodes);
//		}
//		
//		public List<ASTNode> getBCnode(){
//			return this.bcnode;
//		}
		
		
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
			if(ends!=null)
				this.ends.addAll(ends);
		}
	}
	
	public static void main(String[] args){
		int k=3;
		int a=11;
		switch(a){
			case 11:
			{
				k=5;
				break;
			}
			default:
				k=-5;
				break;
		}
		
		System.out.println(k);
		
		
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
	
	public int getPNodeSize(){
		return this.pNodes.size();
	}
	
}
