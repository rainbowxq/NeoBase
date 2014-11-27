
package testcases;

public class HelloWorld {
	public void  get( ){
		int k=3;
		int a=11;
		A:	do{
			B:	for(int j=1;j<10;j+=3)
			C: {
				System.out.println(j);
					if(a>10)
						continue A;
					else if(a>5)
						break B;
					else if (a>2)
						continue B;
					else
						break C;
				}
			}while (k-->0);
		
//		int b=0;
//		switch(a){
//		case 1:
//			b=1;
//		case 2:
//			b=2;
//			break;
//		case 3:
//			return b;
//		case 4:
//			b=4;
//		default:
//			b=5;	
//		}
//		return b;
		
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


