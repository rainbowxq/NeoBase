package collector;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

/**
 * 读取指定文件夹下所有java文件
 * 
 * @author gaoht
 * @date 2010-10-20
 */
public class Projects {
	/**
	 * store the path of projects
	 */
	private List<String> paths=new ArrayList<String>();
	private List<String> names=new ArrayList<String>();

	public static void main(String[] args)  {

		Projects rf = new Projects();
		rf.setProjects("/home/xiaoq_zhu/zxq/workspace");
//		List<String> list = rf.readFolder("/home/xiaoq_zhu/zxq/workspace");
		for (int i = 0; i < rf.paths.size(); i++) {
			System.out.println(rf.paths.get(i));
		}

	}
	
	public void setProjects(String url){
		File file = new File(url);
		
		if(file.exists()){
			File[] files=file.listFiles();
			for(int i=0;i<files.length;i++){
				if(files[i].isDirectory()&& (!files[i].getName().startsWith("."))){
					this.paths.add(files[i].getAbsolutePath());
//					if (files[i].getName().startsWith("."))
//						System.out.print("true: ");
//					else
//						System.out.print("false: ");
					this.names.add(files[i].getName());
				}
			}
		}
	}
	
	public List<String> getPaths(int i){
		return this.paths;
	}
	
	public List<String> getNames(int i){
		return this.names;
	}
	
}
