
package testcases;

public class HelloWorld {
	public void get(){
		int a=2;
A:		if(a>2)
			a--;
		else if(a>-1)
			a++;
		else
			a=7;
		
A:		while (a>0)
			break A;
		
C:		while (a<0)
		{
			break C;
		}
		
	}
	
	
}


