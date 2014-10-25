package testcases;
/**
 * ThrowStatement, TryStatement
 * @author xiaoq_zhu
 *
 */
public class ThrowTest {
	static void pop() throws NegativeArraySizeException {
		}
		public static void main(String[] args) {
		try { 
			pop(); 
		} 
		catch (NegativeArraySizeException e) {
		}
		finally{
			
		}
	}
}
