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
			a=2;
		}
		else{
			a=1;
//			synchronized (this){}
		}
		
		if(a<5){
			a=1;
		}
		return 0;
	}
}
