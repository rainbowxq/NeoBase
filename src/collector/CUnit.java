package collector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CUnit {

	private String program;

	public void setProgram(String path) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			this.program = stringBuilder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("this is an io exception...");
		}

	}
	
	public String getProgram(){
		return this.program;
	}
	
	public static void main (String[] args){
		CUnit unit=new CUnit();
		unit.setProgram("/home/xiaoq_zhu/zxq/workspace/HelloWorld/src/test/HelloWorld.java");
		System.out.println(unit.program);
	}
}
