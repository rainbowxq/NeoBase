package testcases;

public class TestExp2 {
	int ass=2;
	int ass2=ass>>=3;//*    {@link Assignment},
	Object o=(Object)ass;//	 *    {@link CastExpression},
	int max=ass2>ass?1:0;//	 *    {@link ConditionalExpression},
}
