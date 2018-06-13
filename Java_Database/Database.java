import java.io.*;
import java.util.*;
import java.nio.file.*;

/* Start the database system, scan user query,
 * make query class per line, and change database if it is needed */
class Database {

    private Scanner scanner;
    private PrintStream out;
    private List<String> command;
    private Query queries = new Query();
    private Table table = new Table("table", "test");
    private String tablename = "table0";
    private String database = "(none)";
    private boolean returns = false;
    private List<Record> record;

    void setup() {
        scanner = new Scanner(System.in);
        out = System.out;
    }

    void read() {
        returns = false;
        out.print("HomeDB "+"["+database+"]> ");
        command = new ArrayList<String>();
        String line = "";
        while(! line.contains(";")) {
            line = scanner.next();
            command.add(line);
            if(command.get(0).equals("quit")) return;
        }
        if(command.size()>1) {
            if(command.get(0).equals("SHOW")&&
                command.get(1).equals("DATABASES;")) {
                showdatabases();
                returns = true;
            }
            if(command.get(0).equals("USE")) {
                changedb();
                returns = true;
            }
        }
    }

    private void changedb() {
        try {
            for(Record r: record) {
                if(command.get(1).replace(";","").equals(r.gets(0))) {
                    database = command.get(1).replace(";","");
                    out.println("Database changed");
                }
            }
        } catch (Exception e) {fail();}   
    }

    private void fail() {
        out.println("ERROR: Access denied for user to database '"
            +command.get(1)+"'");
        return;
    }

    private void showdatabases() {
        Table showdb = new Table("showdb", database);
        Record showdb0;
        record = new ArrayList<Record>();
        File[] databases = new File(".").listFiles(File::isDirectory);
        String[] db = new String[1];
        db[0] = "database";
        showdb.init(db);
        for(File f : databases) {
            showdb0 = new Record("showdb0");
            showdb0.add(f.getName());
            record.add(showdb0);
        }
        showdb.insert(record);
        showdb.printtable();
    }

    void run() {
        setup();
        out.println("Welcome to HomeDB. Commands end with ;."+"\n"+
            "Your HomeDB connection id is 1"+"\n"+
            "Copyright(c) 2018, COMSM0103, Java, HomeDB"+"\n"+"\n"+
            "Please use an upper case for querying, except for 'quit'"+"\n");
        while (true) {
            read();
            if(! returns) queries.parse(command, database);
        }
    }

    public static void main(String[] args) {
        Database test = new Database();
        test.run();
    }
}