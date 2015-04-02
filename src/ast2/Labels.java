package ast2;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;

public interface Labels {
	public static final Label tkLabel = DynamicLabel.label( "Tkey" );
	public static final Label mkLabel = DynamicLabel.label( "Mkey" );
	public static final Label vkLabel = DynamicLabel.label( "Vkey" );
	public static final Label kLabel = DynamicLabel.label( "Key" );
	public static final Label pdLabel = DynamicLabel.label( "PackageDeclaration" );
	public static final Label idLabel = DynamicLabel.label( "ImportDeclaration" );
	public static final Label tDabel = DynamicLabel.label( "TypeDeclaration" );
	
}
