package testcases;
/**
 * DoStatement test and BreakStatement test
 * @author xiaoq_zhu
 *
 */
public class DoTest {
	void get(){
A:	do{
			break A;
		}while(false);
	}
}
