
package testcases;

public class HelloWorld {
	public int  get( int a ){
		int b=0;
		switch(a){
		case 1:
			b=1;
		case 2:
			b=2;
			break;
		case 3:
			return b;
		case 4:
			b=4;
		default:
			b=5;	
		}
		return b;
		
//		int a=2;
//A:		if(a>2)
//			a--;
//		else if(a>-1)
//			a++;
//		else
//			a=7;
//		
//A:		while (a>0)
//			break A;
//		
//C:		while (a<0)
//D:		while(a>-5){
//			continue C;
//		}
//		
		
	}
	
	
}


