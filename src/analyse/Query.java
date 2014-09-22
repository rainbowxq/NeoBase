package analyse;

import net.sf.json.JSONObject;

import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.osgi.internal.debug.Debug;

public class Query {
	/*generate query sentence for TypeDeclaration*/
	public static String tdQuery(TypeDeclaration node){
		
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: TypeDeclaration { name : {typeName},INTERFACE : {isInterface},KEY:{Key} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("typeName", node.getName().getFullyQualifiedName());
		params.put("isInterface", node.isInterface());
		params.put("Key",node.resolveBinding().getKey() );
		
		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
		
	}
	/*generate query sentence for AnnotationTypeDeclaration*/
	public static String adQuery(AnnotationTypeDeclaration node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: AnnotationTypeDeclaration { name : {atypeName},KEY:{Key} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("atypeName", node.getName().getFullyQualifiedName());
		params.put("Key",node.resolveBinding().getKey() );
		
		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}
	
	/*EnumDeclaration*/
	public static String edQuery(EnumDeclaration node){
		
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: EnumDeclaration { name : {etypeName},KEY:{Key} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("typeName", node.getName().getFullyQualifiedName());
		params.put("Key",node.resolveBinding().getKey() );
		
		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
	}
	/*CompilationUnit*/
	public static String cuQuery(CompilationUnit node,String fileName){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: CompilationUnit { name : {fileName} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("fileName", fileName);
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	/*PackageDeclaration*/
	public static String pdQuery(PackageDeclaration node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: PackageDeclaration { name : {pkgName} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("pkgName", node.getName().getFullyQualifiedName());
		//params.put("pkgKey", node.resolveBinding().getKey());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	
	/*ImportDeclaration*/
	public static String idQuery(ImportDeclaration node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: ImportDeclaration { name : {ImportName} ,STATIC : {Static}, ON_DEMAND:{onDemand} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("ImportName", node.getName().getFullyQualifiedName());
		params.put("Static", node.isStatic());
		params.put("onDemand", node.isOnDemand());
		
		query.put("params", params);
		System.out.println(query.toString());
		return query.toString();
	}
	/*Modifier*/
	public static String modifierQuery(Modifier node){
		JSONObject query=new JSONObject();
		query.put("query", "CREATE (n: Modifier { KEY : {key} }) RETURN id(n)");
		
		JSONObject params=new JSONObject();
		params.put("key", node.getKeyword().toString());
		
		query.put("params", params);
		Debug.println(query.toString());
		return query.toString();
		
	}
}
