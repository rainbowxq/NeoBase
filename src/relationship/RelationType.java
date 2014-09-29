package relationship;


public class RelationType {
	public static final String PACKAGE="PACKAGE"; 	// java file --> package
	public static final String ANNOTATIONS="ANNOTATIONS";//__ --> annotation /annotation binding
	public static final String IMPORT="IMPORT";	// java file --> import declaration
	public static final String TYPES="TYPES";		// java file --> types
	public static final String MODIFIERS="MODIFIERS";	
	public static final String SUPER_INTERFACE_TYPES="SUPER_INTERFACE_TYPES"; // type declaration --> super class(simple type)
	public static final String SUPERCLASS_TYPE="SUPERCLASS_TYPE";// type declaration --> super interface(simple type)
	public static final String TYPE_PARAMETERS="TYPE_PARAMETERS";	//
	public static final String BODY_DECLARATIONS="BODY_DECLARATIONS"; 	//
	public static final String JAVA_DOC="JAVA_DOC";	//
	public static final String TAGS="TAGS";
	public static final String COMMENTS="COMMENTS";
	
	public static final String NAME="NAME";	//
	
	public static final String RETURN_TYPE="RETURN_TYPE";	// method declaration --> type
	public static final String PARAMETERS="PARAMETERS";	// method declearation --> SingleVariableDeclaration
	public static final String BODY="BODY";	// __ --> block
	public static final String STATEMENTS="STATEMENTS";	// block --> statement
	public static final String EXPRESSION="EXPRESSION";	//__ --> expression
	public static final String EXPRESSIONS="EXPRESSIONS"; //__ --> expression //can be more than one
	public static final String ARGUMENTS="ARGUMENTS"; // method invocation --> expression
	public static final String TYPE_ARGUMENTS="TYPE_ARGUMENTS";	// __ --> type  //can be more than one
	public static final String COMPONENT_TYPE="COMPONENT_TYPE";
	public static final String TYPE="TYPE";
	public static final String TYPE_BOUNDS="TYPE_BOUNDS";
	public static final String BOUND="BOUND";
	public static final String QUALIFIER="QUALIFIER";
	public static final String VALUES="VALUES";
	public static final String FRAGMENTS="FRAGMENTS";
	public static final String INITIALIZER="INITIALIZER";
	public static final String ARRAY="ARRAY";
	public static final String INDEX="INDEX";
}