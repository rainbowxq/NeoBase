package testcases;


public class TestExp3 {
	int ass;
	int f=TestExp3.this.ass;//	 *    {@link FieldAccess},QualifiedName????????
    boolean gt=3>ass;//	 *    {@link InfixExpression},
    TestExp3 ai=null;
    boolean at=ai instanceof TestExp3;//	 *    {@link InstanceofExpression},
}
