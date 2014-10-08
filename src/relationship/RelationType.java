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
	public static final String PARAMETER="PARAMETER";
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
	public static final String INITIALIZERS="INITIALIZERS";
	public static final String UPDATES="UPDATES";
	public static final String ARRAY="ARRAY";
	public static final String INDEX="INDEX";
	public static final String THROWN_EXCEPTIONS="THROWN_EXCEPTIONS";
	public static final String ANONYMOUS_CLASS_DECLARATION="ANONYMOUS_CLASS_DECLARATION";
	public static final String ENUM_CONSTANTS="ENUM_CONSTANTS";
	public static final String DEFAULT="DEFAULT";
	public static final String DIMENSIONS="DIMENSIONS";
	public static final String LEFT_HAND_SIDE="LEFT_HAND_SIDE";
	public static final String RIGHT_HAND_SIDE="RIGHT_HAND_SIDE";
	public static final String THEN_EXPRESSION="THEN_EXPRESSION";
	public static final String ELSE_EXPRESSION="ELSE_EXPRESSION";
	public static final String THEN_STATEMENT="THEN_STATEMENT";
	public static final String ELSE_STATEMENT="ELSE_STATEMENT";
	public static final String LEFT_OPERAND="LEFT_OPERAND";
	public static final String RIGHT_OPERAND="RIGHT_OPERAND";
	public static final String EXTENDED_OPERANDS="EXTENDED_OPERANDS";
	public static final String OPERAND="OPERAND";
	public static final String MESSAGE="MESSAGE";
	public static final String RESOURCES="RESOURCES";
	public static final String CATCH_CLAUSES="CATCH_CLAUSES";
	public static final String FINALLY="FINALLY";
	public static final String EXCEPTION="EXCEPTION";
	public static final String DECLARATION="DECLARATION";
}