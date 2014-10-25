package testcases;
/**
 * IfStatement, SynchronizedStatement and ReturnStatement test
 * @author xiaoq_zhu
 *
 */
public abstract class IfTest implements Runnable{
	/**
	 * this is a method 
	 * @return
	 */
	int get(int a){
		if(a>3){
			
		}
		else{
			synchronized (this){}
		}
		return 0;
	}
}
