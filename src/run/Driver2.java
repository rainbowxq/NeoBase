package run;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import collector.JavaFiles;
import collector.Stopwatch;
import ast.Query;
import ast2.Parser2;
import ast2.Query2;

public class Driver2 {
	private List<Long> units = new ArrayList<Long>();
	private Long id;
	private String name = null;
	private String version = "";// the location where the project stores
	private List<String> srcPaths;
	private List<String> targetPaths;
	private String proPath;
	private GraphDatabaseBuilder graphDbBuilder;

	private static final String DPATH = "./data/test";

	// private final GraphDatabaseService db;
	// private final ExecutionEngine engine;

	public Driver2(String name, String propath, String version) {
		this.name = name;
		this.proPath = propath;
		this.setVersion(version);
		this.createDb();
	}



	/**
	 * parse a single java file
	 * 
	 * @param fileName
	 * @param filePath
	 * @param classPaths
	 * @param filePaths
	 */
	public void parseFile(Query2 query2,String fileName, String filePath) {
		Parser2 parser = new Parser2(query2, fileName, filePath);
		String[] tarpaths = (String[]) this.targetPaths
				.toArray(new String[this.targetPaths.size()]);
		String[] srcpaths = (String[]) this.srcPaths
				.toArray(new String[this.srcPaths.size()]);
		parser.analyse(tarpaths, srcpaths);
		this.units.add(parser.getCid());
		System.out.println(parser.getCid());
	}

	public void parseProject() {
		Query2 query2=new Query2(this.graphDbBuilder);
		this.setId(query2.projectQuery(this.name,this.version));

		JavaFiles files = new JavaFiles();
		files.readFolder(proPath);
		List<String> javapaths = files.getfilePaths();
		List<String> names = files.getNames();
		this.srcPaths = files.getSources();
		this.targetPaths = files.getTargets();
		assert (javapaths.size() == names.size()) : "size doesn't match!!\n";
		for (int i = 0; i < names.size(); i++) {
			System.out.println("parse: " + javapaths.get(i));
			this.parseFile(query2, names.get(i), javapaths.get(i));

		}
		
		for (int j = 0; j < this.units.size(); j++) {
			query2.addRelation(this.id, this.units.get(j), "FILES");
			System.out.println(this.id + "," + this.units.get(j));
		}
		query2.shutdownDb();
		// this.registerShutdownHook(db);

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

	public static void main(String[] args) {
		Stopwatch timer = new Stopwatch();
		timer.start();
		// Driver driver=new
		// Driver("org.eclipse.swt","/home/xiaoq_zhu/zxq/workspace/org.eclipse.swt","2.1");
		// Driver driver=new
		// Driver("apache-ivy","/home/xiaoq_zhu/Documents/experiment/apache-ivy","2.4.0");
		// Driver driver=new
		// Driver("apache-ant","/home/xiaoq_zhu/Documents/experiment/apache-ant","1.9.4");
		Driver2 driver = new Driver2("ArtOfIllusion",
				"/home/xiaoq_zhu/Documents/experiment/AoIsrc301", "3.0.1");

		driver.parseProject();
		// driver.parseFile("Simple8BitZipEncoding.java",
		// "/home/xiaoq_zhu/Documents/experiment/apache-ant/main/org/apache/tools/zip/Simple8BitZipEncoding.java",
		// -1);

		System.out.println("finished!!");
		timer.stop();
		System.out.println("time consumed: " + timer.timeInNanoseconds());
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

//	public static GraphDatabaseService getDb() {
//		return db;
//	}
//
//	public static void setDb(GraphDatabaseService db) {
//		Driver2.db = db;
//	}

	public void createDb() {
		graphDbBuilder = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(DPATH);

		// registerShutdownHook( graphDb );

	}

	// private void clearDb(String dbPath)
	// {
	// try
	// {
	// FileUtils.deleteRecursively( new File( dbPath ) );
	// }
	// catch ( IOException e )
	// {
	// throw new RuntimeException( e );
	// }
	// }

//	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				graphDb.shutdown();
//			}
//		});
//	}

}
