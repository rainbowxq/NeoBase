package analyse;

import java.util.List;

import net.sf.json.JSONObject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.osgi.internal.debug.Debug;

public class Query {

	/* PackageDeclaration */
	public static String pdQuery(PackageDeclaration node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: PackageDeclaration { name : {pkgName},KEY:{Key} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("pkgName", node.getName().getFullyQualifiedName());
		params.put("Key", node.resolveBinding().getKey());
		// params.put("pkgKey", node.resolveBinding().getKey());

		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}

	/* ImportDeclaration */
	public static String idQuery(ImportDeclaration node) {
		JSONObject query = new JSONObject();
		query.put(
				"query",
				"CREATE (n: ImportDeclaration { name : {ImportName} ,STATIC : {Static}, ON_DEMAND:{onDemand} ,KEY:{Key}}) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("ImportName", node.getName().getFullyQualifiedName());
		params.put("Static", node.isStatic());
		params.put("onDemand", node.isOnDemand());
		params.put("Key", node.resolveBinding().getKey());

		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}

	/*
	 * generate query sentence for TypeDeclaration
	 */
	public static String tdQuery(TypeDeclaration node) {

		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: TypeDeclaration { name : {typeName},INTERFACE : {isInterface},"
						+ "KEY:{Key} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("typeName", node.getName().getFullyQualifiedName());
		params.put("isInterface", node.isInterface());
		params.put("Key", node.resolveBinding().getKey());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();

	}

	/*
	 * generate query sentence for AnnotationTypeDeclaration
	 */
	public static String adQuery(AnnotationTypeDeclaration node) {
		JSONObject query = new JSONObject();
		query.put(
				"query",
				"CREATE (n: AnnotationTypeDeclaration { name : {atypeName},KEY:{Key} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("atypeName", node.getName().getFullyQualifiedName());
		params.put("Key", node.resolveBinding().getKey());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}

	/* EnumDeclaration */
	public static String edQuery(EnumDeclaration node) {

		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: EnumDeclaration { name : {etypeName},KEY:{Key} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("typeName", node.getName().getFullyQualifiedName());
		params.put("Key", node.resolveBinding().getKey());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}

	/* CompilationUnit */
	public static String cuQuery(CompilationUnit node, String fileName) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: CompilationUnit { name : {fileName} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("fileName", fileName);

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}

	/* Modifier */
	public static String modifierQuery(Modifier node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: Modifier { KEYWORD : {key} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("key", node.getKeyword().toString());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();

	}

	/* Javadoc */
	public static String javadocQuery(Javadoc node) {
		String content = "";
		List<TagElement> tagelements = node.tags();
		for (int i = 0; i < tagelements.size(); i++) {
			TagElement tagelement = tagelements.get(i);
			content += Query.tagEleContent(tagelement) + "\n";
		}
		JSONObject query = new JSONObject();
		query.put("query", "CREATE (n: Javadoc {DOC : {doc} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("doc", content);

		query.put("params", params);
		Debug.println(query.toString());

		Debug.println(query.toString());
		return query.toString();
	}

	public static String tagEleContent(TagElement tagelement) {
		String content = "";

		String tagName = tagelement.getTagName();
		if (tagName != null) {
			content += tagName + " ";
		}

		List<ASTNode> fragments = tagelement.fragments();
		for (int j = 0; j < fragments.size(); j++) {
			ASTNode fragment = fragments.get(j);
			if (fragment instanceof TextElement) {
				content += ((TextElement) fragment).getText() + " ";

			} else if (fragment instanceof TagElement) {
				content += "{" + Query.tagEleContent((TagElement) fragment)
						+ "} ";

			} else if (fragment instanceof Name) {
				content += ((Name) fragment).getFullyQualifiedName() + " ";

			} else if (fragment instanceof MemberRef) {
				String qualifier = ((MemberRef) fragment).getQualifier()
						.getFullyQualifiedName();
				if (qualifier != null) {
					content += qualifier + ".";
				}
				content += ((MemberRef) fragment).getName();

			} else if (fragment instanceof MethodRef) {// MethodRef
				String qualifier = ((MemberRef) fragment).getQualifier()
						.getFullyQualifiedName();
				if (qualifier != null) {
					content += qualifier + ".";
				}
				content += ((MemberRef) fragment).getName() + "(";
				List<MethodRefParameter> paras = ((MethodRef) fragment)
						.parameters();
				for (int i = 0; i < paras.size(); i++) {
					String type = paras.get(i).getType().toString();
					String para = paras.get(i).getName()
							.getFullyQualifiedName();
					content += type + " ";
					if (para != null) {
						content += para;
					}
					if (i != paras.size() - 1) {
						content += ",";
					}
				}
				content += ")";
			}
		}
		return content;

	}

	/* TagElement */
	public static String tagElementQuery(TagElement node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: TagElement { TAG_NAME : {tagName} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("tagName", node.getTagName());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();

	}

	/* TypeParameter */
	public static String typeParameterQuery(TypeParameter node) {
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: TypeParameter { NAME : {name}, KEY : {key} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("name", node.getName().getFullyQualifiedName());
		params.put("key", node.resolveBinding().getKey());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}

	/* Type */
	public static String typeQuery(Type node) {
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();

		if (node instanceof PrimitiveType) {

			query.put("query",
					"CREATE (n: PrimitiveType { PRIMITIVE_TYPE_CODE : {code},"
							+ "KEY : {key}}) RETURN id(n)");
			params.put("code", ((PrimitiveType) node).getPrimitiveTypeCode()
					.toString());
			params.put("key", node.resolveBinding().getKey());
			query.put("params", params);

		} else if (node instanceof SimpleType) {

			query.put("query", "CREATE (n: SimpleType { NAME : {name},"
					+ "KEY : {key}}) RETURN id(n)");
			params.put("name", ((SimpleType) node).getName()
					.getFullyQualifiedName());
			params.put("key", node.resolveBinding().getKey());
			query.put("params", params);

		} else if (node instanceof ArrayType) {

			query.put(
					"query",
					"CREATE (n: ArrayType { ELEMENT_TYPE : {ename},"
							+ "ELEMENT_KEY : {key}, DIMENTIONS:{dimentions}}) RETURN id(n)");

			params.put("ename", node.resolveBinding().getElementType()
					.getName());
			params.put("key", node.resolveBinding().getElementType().getKey());
			params.put("dimentions", node.resolveBinding().getDimensions());
			query.put("params", params);

		} else if (node instanceof UnionType) {

			query.put("query", "CREATE (n: UnionType { NAME : {name},"
					+ "KEY : {key}}) RETURN id(n)");
			params.put("name", node.resolveBinding().getName());
			params.put("key", node.resolveBinding().getKey());
			query.put("params", params);

		} else if (node instanceof QualifiedType) {

			query.put("query", "CREATE (n: QualifiedType { NAME : {name},"
					+ "KEY : {key}}) RETURN id(n)");
			params.put("name", ((QualifiedType) node).getName()
					.getFullyQualifiedName());
			params.put("key", node.resolveBinding().getKey());
			query.put("params", params);

		} else if (node instanceof ParameterizedType) {

			query.put("query", "CREATE (n: ParameterizedType { NAME : {name},"
					+ "KEY : {key}}) RETURN id(n)");
			params.put("name", node.resolveBinding().getName());
			params.put("key", node.resolveBinding().getKey());
			query.put("params", params);

		} else if (node instanceof WildcardType) {

			query.put("query", "CREATE (n: WildcardType { NAME : {name},"
					+ "KEY : {key},UPPER_BOUND:{ub}}) RETURN id(n)");
			params.put("name", node.resolveBinding().getName());
			params.put("key", node.resolveBinding().getKey());
			params.put("ub", ((WildcardType) node).isUpperBound());
			query.put("params", params);
		}
		Debug.println(query.toString());
		return query.toString();
	}
	
	/*Annotation*/
	public static String AnnotationQuery(Annotation node) {
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		if (node instanceof NormalAnnotation) {
			//the key recorded here is type binding 
			query.put("query", "CREATE (n: NormalAnnotation { TYPE_NAME : {tname},"
					+ "KEY:{key},ConstantExpressionValue:{cev} }) RETURN id(n)");
			
		} else if (node instanceof MarkerAnnotation) {
			query.put("query", "CREATE (n: MarkerAnnotation { TYPE_NAME : {tname},"
					+ "KEY:{key},ConstantExpressionValue:{cev} }) RETURN id(n)");
			
		} else if (node instanceof SingleMemberAnnotation) {// SingleMemberAnnotation
			query.put("query", "CREATE (n: SingleMemberAnnotation { TYPE_NAME : {tname},VALUE : {value},KEY:{key},"
					+ "ConstantExpressionValue:{cev} }) RETURN id(n)");
			params.put("value", ((SingleMemberAnnotation) node).getValue().toString());
		}
		params.put("tname", node.getTypeName().getFullyQualifiedName());
		params.put("key", node.resolveTypeBinding().getKey());
		params.put("cev", node.resolveConstantExpressionValue());
		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}

	/*MemberValuePair*/
	public static String MemberValuePairQuery(MemberValuePair node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: MemberValuePair { NAME : {name}, VALUE : {value} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("name", node.getName().getFullyQualifiedName());
		params.put("value", node.getValue().toString());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}
	/*FieldDeclaration*/
	public static String fieldDeclarationQuery(FieldDeclaration node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: FieldDeclaration {CONTENT : {content} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("content", node.toString());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}
	/*VariableDeclarationFragment*/
	public static String variableDeclarationFragmentQuery(VariableDeclarationFragment node){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: VariableDeclarationFragment "
				+ "{NAME : {name},KEY :{key},EXTRA_DIMENSIONS:{ed} }) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("name",node.getName().getFullyQualifiedName());
		params.put("key",node.resolveBinding().getKey());
		params.put("ed", node.getExtraDimensions());

		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}
	/*Expression*/
	public static String expressionQuery(Expression node){
		JSONObject query = new JSONObject();
		JSONObject params = new JSONObject();
		String common="KEY:{key},ConstantExpressionValue:{cev}";
		switch(node.getNodeType()){
			case ASTNode.MARKER_ANNOTATION:
			case ASTNode.NORMAL_ANNOTATION:
			case ASTNode.SINGLE_MEMBER_ANNOTATION:
				return Query.AnnotationQuery((Annotation) node);
			case ASTNode.ARRAY_ACCESS:
				query.put("query",
						"CREATE (n: ArrayAccess {ARRAY:{array},INDEX:{index} }) RETURN id(n)");
				params.put("array", ((ArrayAccess)node).getArray());
				break;
			case ASTNode.ARRAY_CREATION:
				break;
			case ASTNode.ARRAY_INITIALIZER:
				break;
			case ASTNode.ASSIGNMENT:
				break;
			case ASTNode.BOOLEAN_LITERAL:
				break;
			case ASTNode.CAST_EXPRESSION:
				break;
			case ASTNode.CHARACTER_LITERAL:
				break;
			case ASTNode.CLASS_INSTANCE_CREATION:
				break;
			case ASTNode.CONDITIONAL_EXPRESSION:
				break;
			case ASTNode.FIELD_ACCESS:
				break;
			case ASTNode.INFIX_EXPRESSION:
				break;
			case ASTNode.INSTANCEOF_EXPRESSION:
				break;
			case ASTNode.METHOD_INVOCATION:
				break;
			case ASTNode.QUALIFIED_NAME:
			case ASTNode.SIMPLE_NAME:
				break;
			case ASTNode.NULL_LITERAL:
				break;
			case ASTNode.NUMBER_LITERAL:
				break;
			case ASTNode.PARENTHESIZED_EXPRESSION:
				break;
			case ASTNode.POSTFIX_EXPRESSION:
				break;
			case ASTNode.PREFIX_EXPRESSION:
				break;
			case ASTNode.STRING_LITERAL:
				break;
			case ASTNode.SUPER_FIELD_ACCESS:
				break;
			case ASTNode.SUPER_METHOD_INVOCATION:
				break;
			case ASTNode.THIS_EXPRESSION:
				break;
			case ASTNode.TYPE_LITERAL:
				break;
			case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
				break;
		}
		return query.toString();
	}
	
}
