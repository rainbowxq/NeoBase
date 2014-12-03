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
	private String location="null";// the location where the project stores 
	private String[] classPaths;
	private String[] filePaths;
	
	public Driver(String name,String location,String[] classPaths,String[] filePaths){
		this.name=name;
		this.location=location;
		this.classPaths=classPaths;
		this.filePaths=filePaths;
	}
	
	public Driver(String name,String[] classPaths,String[] filePaths){
		this.name=name;
		this.classPaths=classPaths;
		this.filePaths=filePaths;
	}
	/**
	 * store the project node into the database and record its id in the database
	 */
	public void store(){
		JSONObject query = new JSONObject();
		query.put("query",
				"CREATE (n: Project { NAME : {pname},LOCATION:{loc}}) RETURN id(n)");

		JSONObject params = new JSONObject();
		params.put("pname", this.getName());
		params.put("loc", this.getLocation());
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
	public void parseFile(String fileName,String filePath){
		Parser parser=new Parser(fileName,filePath);
		parser.ececute(classPaths,filePaths);
		this.units.add(parser.getCid());
	}
	
	public void parseProject(String proPath){
		this.store();
		
		JavaFiles files=new JavaFiles();
		files.readFolder(proPath);
		ArrayList<String> filepaths=files.getfilePaths();
		ArrayList<String> names=files.getNames();
		assert(filepaths.size()==names.size()):"size doesn't match!!\n";
		for(int i=0;i<names.size();i++){
			System.out.println("parse: "+names.get(i));
			this.parseFile(names.get(i), filepaths.get(i));
			
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
	public static void main(String[] args){
		Stopwatch timer=new Stopwatch();
		timer.start();
		Driver driver=new Driver("LRP",
				new String[]{"/home/xiaoq_zhu/workspace/LRP/bin",
					"/home/xiaoq_zhu/workspace/LRP/lib/jcommon-1.0.22.jar",
					"/home/xiaoq_zhu/workspace/LRP/lib/jfreechart-1.0.18.jar",
					"/home/xiaoq_zhu/workspace/LRP/lib/servlet.jar"},
				new String[]{"/home/xiaoq_zhu/workspace/LRP/src"});
		driver.parseProject("/home/xiaoq_zhu/workspace/LRP/");
		System.out.println("finished!!");
		timer.stop();
		System.out.println("time consumed: "+timer.timeInNanoseconds());
	}
	
	
}
