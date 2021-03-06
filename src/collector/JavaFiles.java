
package collector;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
/**
 * get all java files' names and paths in a project
 * get all the source directories's paths
 * get all the binary files' paths
 * @author xiaoq_zhu
 *
 */
public class JavaFiles {
	/*[store the java files' absolute paths under a project ]*/
	private List<String> filepaths=new ArrayList<String>();
	private List<String> names=new ArrayList<String>();
	
	private List<String> sources=new ArrayList<String>();
	private List<String> targets=new ArrayList<String>();
	/**
	 * 读取文件夹下所有子目录下的txt文件，并将其保存到list集合中。
	 * 
	 * @param url
	 *            目录路径
	 * @return List 文件集合
	 * @throws URISyntaxException 
	 */
	public void readFolder(String url)  {
		// 通过将给定路径名字符串转换为抽象路径名来创建一个新 File 实例。
		File file = new File(url);
		
		// 测试此抽象路径名表示的文件或目录是否存在
		if (file.exists()) {
			// 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件。
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 判断是否是文件夹，如果是文件夹则继续读取文件夹下的子目录或文件
				if (files[i].isDirectory()) {
					if(files[i].getName().trim().equals("bin"))
						this.targets.add(files[i].getAbsolutePath());
					else{
						if(files[i].getName().startsWith("src"))
							this.sources.add(files[i].getAbsolutePath());
						this.readFolder(files[i].getPath());
					}
				} else {
					if (files[i].getName().endsWith(".java")) {
						filepaths.add(files[i].getAbsolutePath());
						this.names.add(files[i].getName());
					}
					else if(files[i].getName().endsWith(".jar")){
						this.targets.add(files[i].getAbsolutePath());
					}
				}
			}
		}
		else{
			System.out.println("the file doesn't exist!!");
		}
	}
	
	public List<String> getfilePaths(){
		return this.filepaths;
	}
	
	public List<String> getNames(){
		return this.names;
	}
	public static void main(String[] args){
		JavaFiles files=new JavaFiles();
		files.readFolder("/home/xiaoq_zhu/Documents/experiment/apache-ivy");
		for(int i=0;i<files.filepaths.size();i++){
			System.out.println(files.filepaths.get(i));
			System.out.println(files.names.get(i));
		}
		for(int i=0;i<files.sources.size();i++){
			System.out.println(files.sources.get(i));
		}
		for(int i=0;i<files.targets.size();i++){
			System.out.println(files.targets.get(i));
		}
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(ArrayList<String> sources) {
		this.sources = sources;
	}

	public List<String> getTargets() {
		return targets;
	}

	public void setTargets(ArrayList<String> targets) {
		this.targets = targets;
	}
}
