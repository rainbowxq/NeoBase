package run;

import java.util.ArrayList;
import java.util.List;

import log.Log;
import neo4j.Neo4jOp;
import net.sf.json.JSONObject;
import collector.JavaFiles;
import collector.Stopwatch;
import ast.Parser;


public class Driver {
	private List<Long> units=new ArrayList<Long>();
	private Long id;
	private String name=null;
	private String version="";// the location where the project stores 
	private List<String> srcPaths;
	private List<String> targetPaths;
	private String proPath;
	
	
	public Driver(String name,String propath,String version){
		this.name=name;
		this.proPath=propath;
		this.setVersion(version);
	}
	/**
	 * store the project node into the database and record its id in the database
	 */
	public void store(int pid){
		JSONObject query = new JSONObject();
		query.put("query",
				"MERGE (n: Project { NAME : {pname},VERSION:{version},P_ID:{pid}}) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("pname", this.getName());
		params.put("version", this.getVersion());
		params.put("pid", pid);
		query.put("params", params);
		Log.debugLoger(query.toString());
		
		String a=Neo4jOp.executeQuery(query.toString());
		String b[]=a.split(" ");
//		for(int j=0;j<b.length;j++){
//			System.out.println(b[j]+" "+j);
//		}
		this.setId(Long.parseLong(b[12]));
	}
	
	/**
	 * parse a single java file
	 * @param fileName
	 * @param filePath
	 * @param classPaths
	 * @param filePaths
	 */
	public void parseFile(String fileName,String filePath,int pid){
		Parser parser=new Parser(fileName,filePath,pid);
		String[] tarpaths=(String[]) this.targetPaths.toArray(new String[this.targetPaths.size()]);
		String[] srcpaths=(String[]) this.srcPaths.toArray(new String[this.srcPaths.size()]);
		parser.ececute(tarpaths,srcpaths);
		this.units.add(parser.getCid());
	}
	
	public void parseProject(){
		int pid=Neo4jOp.getPid()+1;
		System.out.println("the pid is:"+pid);
		if(pid<0)
			return;
		this.store(pid);
		
		JavaFiles files=new JavaFiles();
		files.readFolder(proPath);
		List<String> javapaths=files.getfilePaths();
		List<String> names=files.getNames();
		this.srcPaths=files.getSources();
		this.targetPaths=files.getTargets();
		assert(javapaths.size()==names.size()):"size doesn't match!!\n";
		for(int i=0;i<names.size();i++){
			System.out.println("parse: "+javapaths.get(i));
			this.parseFile(names.get(i), javapaths.get(i),pid);
			
		}
		
		for(int j=0;j<this.units.size();j++){
			Neo4jOp.addRelation(this.id, this.units.get(j), "AST", "FILES");
		}
		
	}
	

	public List<Long> getUnits() {
		return units;
	}

	public void addunit(Long unit) {
		this.units.add(unit);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public static void main(String[] args){
		Stopwatch timer=new Stopwatch();
		timer.start();
		Driver driver=new Driver("org.eclipse.swt","/home/xiaoq_zhu/zxq/workspace/org.eclipse.swt","2.1");
		driver.parseProject();
		System.out.println("finished!!");
		timer.stop();
		System.out.println("time consumed: "+timer.timeInNanoseconds()/60000000);
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
