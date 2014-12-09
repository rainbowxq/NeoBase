package ast;

import java.util.List;

import log.Log;
import net.sf.json.JSONObject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WildcardType;

public class Query {
	private static int pid=-1;
	private static final String setPid=",P_ID:{proid} }) RETURN id(n)";
	/* PackageDeclaration */
	public static String pdQuery(PackageDeclaration node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"MERGE (n: PackageDeclaration { NAME : {pkgName},P_KEY:{Key}"+setPid);

		JSONObject params = new JSONObject();
		params.put("pkgName", node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("Key", node.resolveBinding().getKey());
		else
			params.put("Key", "null");
		// params.put("pkgKey", node.resolveBinding().getKey());
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}

	/* ImportDeclaration */
	public static String idQuery(ImportDeclaration node) {
		JSONObject query = new JSONObject();
		query.put(
				"query",
				"CREATE (n: ImportDeclaration { NAME : {ImportName} ,STATIC : {Static}, ON_DEMAND:{onDemand} ,T_KEY:{Key}"+setPid);

		JSONObject params = new JSONObject();
		params.put("ImportName", node.getName().getFullyQualifiedName());
		params.put("Static", node.isStatic());
		params.put("onDemand", node.isOnDemand());
		if(node.resolveBinding()!=null){
			params.put("Key", node.resolveBinding().getKey());
		}
		else
			params.put("Key", "null");
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}

	/*
	 * generate query sentence for TypeDeclaration
	 */
	public static String tdQuery(TypeDeclaration node) {

		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: TypeDeclaration { NAME : {typeName},INTERFACE : {isInterface},"
						+ "T_KEY:{Key} "+setPid);

		JSONObject params = new JSONObject();
		params.put("typeName", node.getName().getFullyQualifiedName());
		params.put("isInterface", node.isInterface());
		if(node.resolveBinding()!=null){
			params.put("Key", node.resolveBinding().getKey());
		}
		else
			params.put("Key", "null");
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();

	}

	/*
	 * generate query sentence for AnnotationTypeDeclaration
	 */
	public static String adQuery(AnnotationTypeDeclaration node) {
		JSONObject query = new JSONObject();
		query.put(
				"query",
				"CREATE (n: AnnotationTypeDeclaration { NAME : {atypeName},T_KEY:{Key} "+setPid);

		JSONObject params = new JSONObject();
		params.put("atypeName", node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("Key", node.resolveBinding().getKey());
		else
			params.put("Key", "null");
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}

	/* EnumDeclaration */
	public static String edQuery(EnumDeclaration node) {

		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: EnumDeclaration { NAME : {etypeName},T_KEY:{Key} "+setPid);

		JSONObject params = new JSONObject();
		params.put("etypeName", node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("Key", node.resolveBinding().getKey());
		else
			params.put("Key", "null");
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*EnumConstantDeclaration*/
	public static String enumConstantDeclarationQuery(EnumConstantDeclaration node) {

		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: EnumConstantDeclaration { NAME : {etypeName},M_KEY:{mKey},"
				+ "V_Key:{vkey}"+setPid);

		JSONObject params = new JSONObject();
		params.put("etypeName", node.getName().getFullyQualifiedName());
		if(node.resolveConstructorBinding()!=null)
			params.put("mKey", node.resolveConstructorBinding().getKey());
		else
			params.put("mKey", "null");
		if(node.resolveVariable()!=null)
			params.put("vkey", node.resolveVariable().getKey());
		else
			params.put("vkey", "null");
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/* CompilationUnit */
	public static String cuQuery(CompilationUnit node, String fileName) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: CompilationUnit { NAME : {fileName} "+setPid);

		JSONObject params = new JSONObject();
		params.put("fileName", fileName);
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}

	/* Modifier */
	public static String modifierQuery(Modifier node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: Modifier { KEYWORD : {key}"+setPid);

		JSONObject params = new JSONObject();
		params.put("key", node.getKeyword().toString());
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();

	}

	/* Javadoc */
	public static String javadocQuery(Javadoc node) {
		String content = "";
		@SuppressWarnings("unchecked")
		List<TagElement> tagelements = node.tags();
		for (int i = 0; i < tagelements.size(); i++) {
			TagElement tagelement = tagelements.get(i);
			content += tagelement.toString() + "\n";
		}
		JSONObject query = new JSONObject();
		query.put("query", "CREATE (n: Javadoc {DOC : {doc} "+setPid);

		JSONObject params = new JSONObject();
		params.put("doc", content);
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());

		Log.debugLoger(query.toString());
		return query.toString();
	}

//	public static String tagEleContent(TagElement tagelement) {
//		String content = "";
//
//		String tagName = tagelement.getTagName();
//		if (tagName != null) {
//			content += tagName + " ";
//		}
//
//		@SuppressWarnings("unchecked")
//		List<ASTNode> fragments = tagelement.fragments();
//		for (int j = 0; j < fragments.size(); j++) {
//			ASTNode fragment = fragments.get(j);
//			if (fragment instanceof TextElement) {
//				content += ((TextElement) fragment).getText() + " ";
//
//			} else if (fragment instanceof TagElement) {
//				content += "{" + Query.tagEleContent((TagElement) fragment)
//						+ "} ";
//
//			} else if (fragment instanceof Name) {
//				content += ((Name) fragment).getFullyQualifiedName() + " ";
//
//			} else if (fragment instanceof MemberRef) {
//				String qualifier = ((MemberRef) fragment).getQualifier()
//						.getFullyQualifiedName();
//				if (qualifier != null) {
//					content += qualifier + ".";
//				}
//				content += ((MemberRef) fragment).getName();
//
//			} else if (fragment instanceof MethodRef) {// MethodRef
//				String qualifier = ((MemberRef) fragment).getQualifier()
//						.getFullyQualifiedName();
//				if (qualifier != null) {
//					content += qualifier + ".";
//				}
//				content += ((MemberRef) fragment).getName() + "(";
//				@SuppressWarnings("unchecked")
//				List<MethodRefParameter> paras = ((MethodRef) fragment)
//						.parameters();
//				for (int i = 0; i < paras.size(); i++) {
//					String type = paras.get(i).getType().toString();
//					String para = paras.get(i).getName()
//							.getFullyQualifiedName();
//					content += type + " ";
//					if (para != null) {
//						content += para;
//					}
//					if (i != paras.size() - 1) {
//						content += ",";
//					}
//				}
//				content += ")";
//			}
//		}
//		return content;
//
//	}

	/* TagElement */
	public static String tagElementQuery(TagElement node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: TagElement { TAG_NAME : {tagName} "+setPid);

		JSONObject params = new JSONObject();
		params.put("tagName", node.getTagName());

		query.put("params", params);
		params.put("proid", pid);
		Log.debugLoger(query.toString());
		return query.toString();

	}

	/* TypeParameter */
	public static String typeParameterQuery(TypeParameter node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: TypeParameter { NAME : {name}, T_KEY : {key} "+setPid);

		JSONObject params = new JSONObject();
		params.put("name", node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("key", node.resolveBinding().getKey());
		else
			params.put("key", "null");
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}

	/* Type */
	public static String typeQuery(Type node) {
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();

		if (node instanceof PrimitiveType) {

			query.put("query",
					"CREATE (n: PrimitiveType { PRIMITIVE_TYPE_CODE : {code},"
							+ "T_KEY : {key}"+setPid);
			params.put("code", ((PrimitiveType) node).getPrimitiveTypeCode()
					.toString());
			if(node.resolveBinding()!=null){
				params.put("key", node.resolveBinding().getKey());
				
			}
			else{
				params.put("key", "null");
				
			}
			params.put("proid", pid);
			query.put("params", params);

		} else if (node instanceof SimpleType) {

			query.put("query", "CREATE (n: SimpleType { NAME : {name},"
					+ "T_KEY : {key},IS_FROMSOURCE:{isFS}"+setPid);
			params.put("name", ((SimpleType) node).getName()
					.getFullyQualifiedName());
			if(node.resolveBinding()!=null){
				params.put("key", node.resolveBinding().getKey());
				params.put("isFS", node.resolveBinding().isFromSource());
			}
			else{
				params.put("key", "null");
				params.put("isFS", "null");
			}
			params.put("proid", pid);
			query.put("params", params);

		} else if (node instanceof ArrayType) {

			query.put(
					"query",
					"CREATE (n: ArrayType { ELEMENT_TYPE : {ename},"
							+ "ELEMENT_T_KEY : {key},DIMENTIONS:{dimentions}"+setPid);

			if(node.resolveBinding()!=null){
				params.put("ename", node.resolveBinding().getElementType()
						.getName());
				params.put("key", node.resolveBinding().getElementType().getKey());
			}
			else{
				params.put("ename", "null");
				params.put("key", "null");
			}
			params.put("dimentions", ((ArrayType) node).getDimensions());
			params.put("proid", pid);
			query.put("params", params);

		} else if (node instanceof UnionType) {

			query.put("query", "CREATE (n: UnionType { NAME : {name},"
					+ "T_KEY : {key}"+setPid);
			if(node.resolveBinding()!=null){
				params.put("name", node.resolveBinding().getName());
				params.put("key", node.resolveBinding().getKey());
			}
			else{
				params.put("name", "null");
				params.put("key", "null");
			}
			params.put("proid", pid);
			query.put("params", params);

		} else if (node instanceof QualifiedType) {

			query.put("query", "CREATE (n: QualifiedType { NAME : {name},"
					+ "T_KEY : {key}"+setPid);
			params.put("name", ((QualifiedType) node).getName()
					.getFullyQualifiedName());
			if(node.resolveBinding()!=null)
				params.put("key", node.resolveBinding().getKey());
			else
				params.put("key", "null");
			params.put("proid", pid);
			query.put("params", params);

		} else if (node instanceof ParameterizedType) {

			query.put("query", "CREATE (n: ParameterizedType { NAME : {name},"
					+ "T_KEY : {key}"+setPid);
			params.put("name", node.resolveBinding().getName());
			if(node.resolveBinding()!=null)
				params.put("key", node.resolveBinding().getKey());
			else
				params.put("key", "null");
			params.put("proid", pid);
			query.put("params", params);

		} else if (node instanceof WildcardType) {

			query.put("query", "CREATE (n: WildcardType { NAME : {name},"
					+ "T_KEY : {key},UPPER_BOUND:{ub}"+setPid);
			params.put("name", node.resolveBinding().getName());
			if(node.resolveBinding()!=null)
				params.put("key", node.resolveBinding().getKey());
			else
				params.put("key", "null");
			params.put("ub", ((WildcardType) node).isUpperBound());
			params.put("proid", pid);
			query.put("params", params);
		}
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*Annotation*/
	public static String AnnotationQuery(Annotation node) {
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		if (node instanceof NormalAnnotation) {
			//the key recorded here is type binding 
			query.put("query", "CREATE (n: NormalAnnotation { TYPE_NAME : {tname},"
					+ "T_KEY:{key},A_KEY:{akey}"+setPid);
			
		} else if (node instanceof MarkerAnnotation) {
			query.put("query", "CREATE (n: MarkerAnnotation { TYPE_NAME : {tname},"
					+ "T_KEY:{key},A_KEY:{akey}"+setPid);
			
		} else if (node instanceof SingleMemberAnnotation) {// SingleMemberAnnotation
			query.put("query", "CREATE (n: SingleMemberAnnotation { TYPE_NAME : {tname},VALUE : {value},T_KEY:{key},A_KEY:{akey}"+setPid);
			params.put("value", ((SingleMemberAnnotation) node).getValue().toString());
		}
		params.put("tname", node.getTypeName().getFullyQualifiedName());
		if(node.resolveTypeBinding()!=null)
			params.put("key", node.resolveTypeBinding().getKey());
		else
			params.put("key", "null");
		if(node.resolveAnnotationBinding()!=null)
			params.put("akey", node.resolveAnnotationBinding().getKey());
		else
			params.put("akey", "null");
		if(node.resolveConstantExpressionValue()!=null)
			params.put("cev", node.resolveConstantExpressionValue());
		else
			params.put("cev", "not exist");
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*IExtendedModifier*/
	public static String iExtendedModifier(IExtendedModifier node){
		if (node instanceof Modifier)
			return Query.modifierQuery((Modifier) node);
		if (node instanceof Annotation)
			return Query.AnnotationQuery((Annotation) node);
		return null;
	}

	/*MemberValuePair*/
	public static String MemberValuePairQuery(MemberValuePair node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: MemberValuePair { NAME : {name}, VALUE : {value}"+setPid);

		JSONObject params = new JSONObject();
		params.put("name", node.getName().getFullyQualifiedName());
		params.put("value", node.getValue().toString());
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	/*FieldDeclaration*/
	public static String fieldDeclarationQuery(FieldDeclaration node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: FieldDeclaration {CONTENT : {content} "+setPid);

		JSONObject params = new JSONObject();
		params.put("content", node.toString());
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	/*VariableDeclarationFragment*/
	public static String variableDeclarationFragmentQuery(VariableDeclarationFragment node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: VariableDeclarationFragment "
				+ "{NAME : {name},V_KEY :{key},EXTRA_DIMENSIONS:{ed}"+setPid);

		JSONObject params = new JSONObject();
		params.put("name",node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("key", node.resolveBinding().getKey());
		else
			params.put("key", "null");
		
		params.put("ed", node.getExtraDimensions());
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	/*Expression*/
	public static String expressionQuery(Expression node){
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		String common="T_KEY:{key},CONTENT:{content}"+Query.setPid;
		if(node.resolveTypeBinding()!=null)
			params.put("key", node.resolveTypeBinding().getKey());
		else
			params.put("key", "null");
		params.put("content", node.toString());
//		Log.debugLoger("hahahhahah   "+node.resolveConstantExpressionValue()+"    hahhahahh");
		switch(node.getNodeType()){
			case ASTNode.MARKER_ANNOTATION:
			case ASTNode.NORMAL_ANNOTATION:
			case ASTNode.SINGLE_MEMBER_ANNOTATION:
				return Query.AnnotationQuery((Annotation) node);
			case ASTNode.ARRAY_ACCESS:
				query.put("query",
						"CREATE (n: ArrayAccess {"
						+ common);
				
				break;
			case ASTNode.ARRAY_CREATION:
				query.put("query",
						"CREATE (n: ArrayCreation {"
						+ common);
				break;
			case ASTNode.ARRAY_INITIALIZER:
				query.put("query",
						"CREATE (n: ArrayInitializer {"
						+ common);
				params.put("content", node.toString());
				break;
			case ASTNode.ASSIGNMENT:
				query.put("query",
						"CREATE (n: Assignment {OPERATOR:{operator},"
						+ common);
				params.put("operator", ((Assignment)node).getOperator().toString());
				
				break;
				
			case ASTNode.BOOLEAN_LITERAL:
				query.put("query",
						"CREATE (n: BooleanLiteral {BOOLEAN_VALUE:{value},"
						+ common);
				params.put("value", ((BooleanLiteral)node).booleanValue());

				break;
				
			case ASTNode.CAST_EXPRESSION:
				query.put("query",
						"CREATE (n: CastExpression {"
						+ common);
				break;
				
			case ASTNode.CHARACTER_LITERAL:
				query.put("query",
						"CREATE (n: CharacterLiteral {ESCAPED_VALUE:{value},"
						+ common);
				params.put("value", ((CharacterLiteral)node).getEscapedValue());
				
				break;
				
			case ASTNode.CLASS_INSTANCE_CREATION:
				query.put("query",
						"CREATE (n: ClassInstanceCreation {M_KEY:{mkey},"
						+ common);
				if(((ClassInstanceCreation)node).resolveConstructorBinding()!=null)
					params.put("mkey",((ClassInstanceCreation)node).resolveConstructorBinding().getKey());
				else
					params.put("mkey", "null");
				break;
			case ASTNode.CONDITIONAL_EXPRESSION:
				query.put("query",
						"CREATE (n: ConditionalExpression {"
						+ common);
				break;
			case ASTNode.FIELD_ACCESS:
				query.put("query",
						"CREATE (n: FieldAccess {NAME:{name},V_KEY:{vkey},"
						+ common);
				params.put("name", ((FieldAccess)node).getName().getFullyQualifiedName());
				if(((FieldAccess)node).resolveFieldBinding()!=null){
					params.put("vkey", ((FieldAccess)node).resolveFieldBinding().getKey());
				}
				else{
					params.put("vkey", "null");
				}
				break;
			case ASTNode.INFIX_EXPRESSION:
				query.put("query",
						"CREATE (n: InfixExpression {OPERATOR:{operator},"
						+ common);
				params.put("operator", ((InfixExpression)node).getOperator().toString());
				
				break;
			case ASTNode.INSTANCEOF_EXPRESSION:
				query.put("query",
						"CREATE (n: InstanceofExpression {"
						+ common);
				break;
				
			case ASTNode.METHOD_INVOCATION:
				query.put("query",
						"CREATE (n: MethodInvocation {M_KEY:{mkey},NAME:{name},"
						+ common);
				if(((MethodInvocation)node).resolveMethodBinding()!=null)
					params.put("mkey", ((MethodInvocation)node).resolveMethodBinding().getKey());
				else
					params.put("mkey", "null");
				params.put("name",((MethodInvocation)node).getName().getFullyQualifiedName());
				break;
			case ASTNode.QUALIFIED_NAME:
			case ASTNode.SIMPLE_NAME:
				String extra="";
				params.put("name", ((Name)node).getFullyQualifiedName());
				if(((Name)node).resolveBinding()!=null){
					switch(((Name)node).resolveBinding().getKind()){
					case IBinding.ANNOTATION:
						extra=",A_KEY:{eakey},";
						params.put("eakey", ((Name)node).resolveBinding().getKey());
						break;
					case IBinding.MEMBER_VALUE_PAIR:
						extra=",MVP_KEY:{emvpkey},";
						params.put("emvpkey", ((Name)node).resolveBinding().getKey());
						break;
					case IBinding.VARIABLE:
						extra=",V_KEY:{evkey},";
						params.put("evkey", ((Name)node).resolveBinding().getKey());
						break;
					case IBinding.METHOD:
						extra=",M_KEY:{emkey},";
						params.put("emkey", ((Name)node).resolveBinding().getKey());
						break;
					case IBinding.TYPE:
						extra=",T_KEY:{etkey},";
						params.put("etkey", ((Name)node).resolveBinding().getKey());
						break;
					case IBinding.PACKAGE:
						extra=",P_KEY:{epkey},";
						params.put("epkey", ((Name)node).resolveBinding().getKey());
						break;
					}
				}
				else{
					params.put("epkey", "null");
				}
				query.put("query",
						"CREATE (n: Name {NAME:{name}"
						+ extra+common);
				break;
			case ASTNode.NULL_LITERAL:
				query.put("query",
						"CREATE (n: NullLiteral {"
						+ common);
				break;
			case ASTNode.NUMBER_LITERAL:
				query.put("query",
						"CREATE (n: NumberLiteral {TOKEN:{token},"
						+ common);
				params.put("token", ((NumberLiteral)node).getToken());
				
				break;
			case ASTNode.PARENTHESIZED_EXPRESSION:
				query.put("query",
						"CREATE (n: ParenthesizedExpression {"
						+ common);
				break;
			case ASTNode.POSTFIX_EXPRESSION:
				query.put("query",
						"CREATE (n: PostfixExpression {OPERATOR:{operator},"
						+ common);
				params.put("operator", ((PostfixExpression)node).getOperator().toString());
				
				break;
				
			case ASTNode.PREFIX_EXPRESSION:
				query.put("query",
						"CREATE (n: PrefixExpression {OPERATOR:{operator},"
						+ common);
				params.put("operator", ((PrefixExpression)node).getOperator().toString());
				
				break;
			case ASTNode.STRING_LITERAL:
				query.put("query",
						"CREATE (n: StringLiteral {ESCAPED_VALUE:{value},"
						+ common);
				params.put("value", ((StringLiteral)node).getEscapedValue());
				
				break;
				
			case ASTNode.SUPER_FIELD_ACCESS:
				query.put("query",
						"CREATE (n: SuperFieldAccess {QUALIFIER:{qualifier},NAME:{name},"
						+ common);
				if(((SuperFieldAccess)node).getQualifier()!=null)
					params.put("qualifier", ((SuperFieldAccess)node).getQualifier().getFullyQualifiedName());
				else
					params.put("qualifier", "null");
				params.put("name", ((SuperFieldAccess)node).getName().getFullyQualifiedName());
				
				break;
			case ASTNode.SUPER_METHOD_INVOCATION:
				query.put("query",
						"CREATE (n: SuperMethodInvocation {QUALIFIER:{qualifier},NAME:{name},M_KEY:{mkey},"
						+ common);
				if(((SuperMethodInvocation)node).getQualifier()!=null)
					params.put("qualifier", ((SuperMethodInvocation)node).getQualifier().getFullyQualifiedName());
				else
					params.put("qualifier", "null");
				params.put("name", ((SuperMethodInvocation)node).getName().getFullyQualifiedName());
				if(((SuperMethodInvocation)node).resolveMethodBinding()!=null)
					params.put("mkey", ((SuperMethodInvocation)node).resolveMethodBinding().getKey());
				else
					params.put("mkey", "null");
				
				break;
			case ASTNode.THIS_EXPRESSION:
				query.put("query",
						"CREATE (n: ThisExpression {QUALIFIER:{qualifier},"
						+ common);
				if(((ThisExpression)node).getQualifier()!=null)
					params.put("qualifier", ((ThisExpression)node).getQualifier().getFullyQualifiedName());
				else
					params.put("qualifier", "null");
				
				break;
			case ASTNode.TYPE_LITERAL:
				query.put("query",
						"CREATE (n: TypeLiteral {"
						+ common);
				break;
			case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
				query.put("query",
						"CREATE (n: VariableDeclarationExpression {"
						+ common);
				break;
		}
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*Statement*/
	public static String statementQuery(Statement node){
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		switch(node.getNodeType()){
			case ASTNode.ASSERT_STATEMENT:
				query.put("query",
						"CREATE (n: AssertStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.BLOCK:
				query.put("query",
						"CREATE (n: Block {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.BREAK_STATEMENT:
				query.put("query",
						"CREATE (n: BreakStatement {LABEL:{label}"+setPid);
				if(((BreakStatement)node).getLabel()!=null)
					params.put("label", ((BreakStatement)node).getLabel().getFullyQualifiedName());
				else
					params.put("label", "null");
				break;
			case ASTNode.CONSTRUCTOR_INVOCATION:
				query.put("query",
						"CREATE (n: ConstructorInvocation {M_KEY:{mkey}"+setPid);
				if(((ConstructorInvocation)node).resolveConstructorBinding()!=null)
					params.put("mkey", ((ConstructorInvocation)node).resolveConstructorBinding().getKey());
				else
					params.put("mkey", "null");
				break;
			case ASTNode.CONTINUE_STATEMENT:
				query.put("query",
						"CREATE (n: ContinueStatement {LABEL:{label}"+setPid);
				if(((ContinueStatement)node).getLabel()!=null)
					params.put("label", ((ContinueStatement)node).getLabel().getFullyQualifiedName());
				else
					params.put("label", "null");
				break;
			case ASTNode.DO_STATEMENT:
				query.put("query",
						"CREATE (n: DoStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.EMPTY_STATEMENT:
				query.put("query",
						"CREATE (n: EmptyStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.ENHANCED_FOR_STATEMENT:
				query.put("query",
						"CREATE (n: EnhancedForStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.EXPRESSION_STATEMENT:
				query.put("query",
						"CREATE (n: ExpressionStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.FOR_STATEMENT:
				query.put("query",
						"CREATE (n: ForStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.IF_STATEMENT:
				query.put("query",
						"CREATE (n: IfStatement {CONDITION:{cond}"+setPid);
				params.put("cond", ((IfStatement)node).getExpression().toString() );
				break;
			case ASTNode.LABELED_STATEMENT:
				query.put("query",
						"CREATE (n: LabeledStatement {LABEL:{label}"+setPid);
				params.put("label", ((LabeledStatement)node).getLabel().getFullyQualifiedName());
				break;
			case ASTNode.RETURN_STATEMENT:
				query.put("query",
						"CREATE (n: ReturnStatement) RETURN id(n)");
				break;
			case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
				query.put("query",
						"CREATE (n: SuperConstructorInvocation {M_KEY:{mkey}"+setPid);
				if(((SuperConstructorInvocation)node).resolveConstructorBinding()!=null)
					params.put("mkey", ((SuperConstructorInvocation)node).resolveConstructorBinding().getKey());
				else
					params.put("mkey", "null");
		
				break;
			case ASTNode.SWITCH_CASE:
				query.put("query",
						"CREATE (n: SwitchCase {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.SWITCH_STATEMENT:
				query.put("query",
						"CREATE (n: SwitchStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.SYNCHRONIZED_STATEMENT:
				query.put("query",
						"CREATE (n: SynchronizedStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.THROW_STATEMENT:
				query.put("query",
						"CREATE (n: ThrowStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.TRY_STATEMENT:
				query.put("query",
						"CREATE (n: TryStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.TYPE_DECLARATION_STATEMENT:
				query.put("query",
						"CREATE (n: TypeDeclarationStatement {P_ID:{proid}}) RETURN id(n)");
				break;
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
				query.put("query",
						"CREATE (n: VariableDeclarationStatement {P_ID:{proid}} ) RETURN id(n)");
				break;
			case ASTNode.WHILE_STATEMENT:
				query.put("query",
						"CREATE (n: WhileStatement {P_ID:{proid}}) RETURN id(n)");
				break;
					
		}
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	/*Initializer*/
	public static String initializerQuery(Initializer node){
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		query.put("query",
				"CREATE (n: Initializer {P_ID:{proid}"+setPid);
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*SingleVariableDeclaration*/
	public static String singleVariableDeclarationQuery(SingleVariableDeclaration node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: SingleVariableDeclaration "
				+ "{NAME : {name},V_KEY :{key},VARARGS:{varargs},EXTRA_DIMENSIONS:{ed}"+setPid);

		JSONObject params = new JSONObject();
		params.put("name",node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("key",node.resolveBinding().getKey());
		else
			params.put("key", "null");
		params.put("varargs", node.isVarargs());
		params.put("ed", node.getExtraDimensions());
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	/*MethodDeclaration*/
	public static String methodDeclarationQuery(MethodDeclaration node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: MethodDeclaration "
				+ "{NAME : {name},M_KEY :{key},CONSTRUCTOR:{constructor},EXTRA_DIMENSIONS:{ed}"+setPid);

		JSONObject params = new JSONObject();
		params.put("name",node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("key",node.resolveBinding().getKey());
		else
			params.put("key", "null");
		params.put("constructor", node.isConstructor());
		params.put("ed", node.getExtraDimensions());
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*AnonymousClassDeclaration*/
	public static String anonymousClassDeclarationQuery(AnonymousClassDeclaration node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: AnonymousClassDeclaration {T_KEY :{key}"+setPid);

		JSONObject params = new JSONObject();
		params.put("key",node.resolveBinding().getKey());
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	/*AnnotationTypeMemberDeclaration*/
	public static String annotationTypeMemberDeclarationQuery(AnnotationTypeMemberDeclaration node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: AnnotationTypeMemberDeclaration {NAME:{name},T_KEY :{key}"+setPid);

		JSONObject params = new JSONObject();
		params.put("name", node.getName().getFullyQualifiedName());
		if(node.resolveBinding()!=null)
			params.put("key",node.resolveBinding().getKey());
		else
			params.put("key", "null");
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	/*CatchClause*/
	public static String catchClauseQuery(CatchClause node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: CatchClause{P_ID:{proid}"+setPid);
		JSONObject params = new JSONObject();
		params.put("proid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*start node in cfg*/
	public static String startQuery(String key){
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		query.put("query",
				"CREATE (n: Start {M_KEY:{key}"+setPid);
		params.put("key", key);
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}
	
	/*end node in cfg*/
	public static String endQuery(String key){
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		query.put("query",
				"CREATE (n: End {M_KEY:{key}"+setPid);
		params.put("key", key);
		params.put("proid", pid);
		
		query.put("params", params);
		Log.debugLoger(query.toString());
		return query.toString();
	}

	public static int getPid() {
		return pid;
	}

	public static void setPid(int pid) {
		Query.pid = pid;
	}
	
}
