import java.io.*;
import java.util.*;
import java.nio.file.*;

/* Process user queries per the command line.
 * Parse the queries, make proper operations to generate results. */
class Query {

    private PrintStream out = System.out;
    private String tablename = "table0";
    private String database;
    private String firstq;
    private String secondq;
    private String where = "";
    private String whereVal = "";
    private String selecttbname = "";
    private boolean constraints = false;
    private boolean checktbname = false;
    private DirectoryStream<Path> tables;
    private List<Record> tablelists;
    private List<String> command;
    private List<String> cols;
    private List<Table> newtables = new ArrayList<Table>();
    private long startTime;
    private long endTime;
    private double totalTime;

    void parse(List<String> command, String database) {
        startTime = System.currentTimeMillis();

        this.command = command;
        this.database = database;
        if(command.size()==0||command.get(0).contains(";")) {
            invalid();
            return;     
        }
        if(command.get(0).equals("quit")) quitdb();
        if(command.size() >= 1) {
            firstq = command.get(0);
            secondq = command.get(1);
            if(firstq.equals("SHOW")&&secondq.equals("TABLES;")){
                showtables(findtables(database),database);
            }
            else if(firstq.equals("DROP")&&secondq.equals("TABLE")){
                droptables();
            }
            else if(firstq.equals("CREATE")&&secondq.equals("TABLE")&&
                command.size()>3) {
                try {
                    newtables.add(createtables());
                } catch (Exception e) {
                    out.println("ERROR: Can't create tables");
                    return;
                }
                if(constraints&&checktbname) EndcheckTime();
                else if(! checktbname) return;
                else if(! constraints) {
                    out.println("ERROR: Can't find Foreign keys data");
                    return;
                }
            }
            else if(firstq.equals("INSERT")&&secondq.equals("INTO")) {
                insertinto();
            }
            else if(command.size()>2&&firstq.equals("SELECT")&&
                command.contains("FROM")) {
                if(secondq.equals("*")&&(!command.contains("WHERE"))) {
                    showwholetb();
                }
                else if(command.contains("WHERE")) {
                    showselectedtb();
                }
            }
            else {
                invalid();
                return;
            }
        }
        else {
            invalid();
            return;
        }
    }

    private void EndcheckTime() {
        endTime = System.currentTimeMillis();
        totalTime = (double)(endTime - startTime);
        out.println("Query OK ("+totalTime/1000+" sec)"+"\n");
    }

    // To show selected table, check table has data
    // which meet conditions of queries
    // and select right columns and record
    private void showselectedtb() {
        cols = new ArrayList<String>();
        showselectedtb_parse();
        findtables(database);
        for(Record r: tablelists) {
            if(selecttbname.contains(r.gets(0))) {
                try {
                    Table originaltb = new Table(r.gets(0), database);
                    originaltb.filein();
                    Table selecttb = new Table("select", database);
                    selecttb = originaltb.selectcol_where(cols,where,whereVal);
                    selecttb.printtable();
                    EndcheckTime();
                } catch (Exception e) {
                    out.println("ERROR: Can't select data");
                    return;
                }
            }
        }
    }

    private void showselectedtb_parse() {
        int i=1;
        while(!command.get(i).equals("FROM")) {
            cols.add(command.get(i).replace(",",""));
            i++;
        }
        selecttbname = command.get(i+1);
        i = i+2;
        if(command.get(i).equals("WHERE")) {
            if(command.get(i+1).contains(";")) {
                String[] temp = command.get(i+1).split("\\=");
                where = temp[0];
                whereVal = temp[1].replace(";","");
            }
            else {
                where = command.get(i+1).replace("=","");
                if(command.get(i+2).contains("=")) {
                    whereVal = command.get(i+2).
                    replace(";","").replace("=","");
                }
                else {
                    whereVal = command.get(i+3).
                    replace("=","").replace(";","");
                }
            }
        }        
    }

    private void showwholetb() {
        findtables(database);
        for(Record r: tablelists) {
            if(command.get(3).contains(r.gets(0))) {
                try {
                    Table selecttb = new Table(r.gets(0), database);
                    selecttb.filein();
                    selecttb.printtable();
                    EndcheckTime();
                } catch (Exception e) {
                    out.println("ERROR: Can't find tables");
                    return;
                }
            }
        }
    }

    private void insertinto() {
        String tbname = command.get(2);
        findtables(database);
        for(Record r:tablelists) {
            try {
                if(r.gets(0).equals(tbname)) inserttotable(tbname);
            }
            catch (Exception e) {
                out.println("ERROR: Insert failed");
                return;
            }
        }
    }

    // Read a table needed, insert data into table.
    // Check foreign key, NOT NULL, data type constraints,
    // and use different functions to tables having keys or not
    // Store data to a file after insert and print to show
    private void inserttotable(String tbname) {
        Table newins = new Table(tbname, database);
        newins.filein();
        int tbsize = newins.getDatasize();
        String[] fields0 = new String[newins.getcolsize()];
        String[] types0 = new String[newins.getcolsize()];;
        String[] keyT0 = new String[newins.getcolsize()];;
        String[] foreign0 = new String[newins.getcolsize()];;
        newins.getinfo(fields0, types0, keyT0, foreign0);

        List<String> newfields0 = new ArrayList<String>();
        List<String> newrecord0 = new ArrayList<String>();
        List<String> prikeys = new ArrayList<String>();
        inserttotable_parse(newfields0, newrecord0);

        String[] newfields = new String[fields0.length];
        String[] newrecord = new String[fields0.length];
        List<Record> recordlists = new ArrayList<Record>();
        Record record = new Record(tbname);
        newfields0.toArray(newfields);
        newrecord0.toArray(newrecord);
        if(! checkRecordValid(newfields, newrecord, fields0, keyT0, prikeys)) {
            return;
        }

        record.add(Arrays.toString(newrecord).replace("[","").replace("]",""));
        recordlists.add(record);
        for(int i=0;i<foreign0.length;i++) {
            if(! foreign0[i].equals("NULL")&&
                ! checkForeignValue(foreign0[i],record.gets(i))) return;
        }
        startTime = System.currentTimeMillis();
        if(prikeys.size()!=0) {
            newins.insert(prikeys, recordlists);
        }
        else {
            newins.insert(recordlists); 
        }
        endTime = System.currentTimeMillis();
        totalTime = (double)(endTime - startTime);
        out.println("Insert time ("+totalTime/1000+" sec)");
        int newtbsize = newins.getDatasize();
        if(tbsize!=newtbsize) {
            newins.fileout();
            newins.printtable();
            EndcheckTime();
        }
    }

    private void inserttotable_parse(List<String> newfields0, List<String> newrecord0) {
        int i=3;
        while(! command.get(i).contains("VALUES")) {
            newfields0.add(command.get(i).replace("(","").
                replace(")","").replace(",",""));
            i++;
        }
        i++;
        while(! command.get(i).contains(");")) {
            newrecord0.add(command.get(i).replace("(","").
                replace(")","").replace(",",""));
            i++;
        }
        newrecord0.add(command.get(i).replace(");",""));
        if(newfields0.size()!=newrecord0.size()) {
            out.println("ERROR: Record size doesn't match to field size");
            return;
        }
    }

    private boolean checkRecordValid(String[] newfields, String[] newrecord,
        String[] fields0, String[] keyT0, List<String> prikeys) {
        if(newfields.length!=fields0.length) {
            for(int i=0;i<fields0.length;i++) {
                if(!newfields[i].equals(fields0[i])) {
                    for(int j=newfields.length;j>i;j--) {
                        newfields[j+1] = newfields[j];
                        newrecord[j+1] = newrecord[j];
                    }
                    newfields[i] = "null";
                    newrecord[i] = "null";
                }
            }
        }
        else {
            for(int i=0;i<newfields.length;i++) {
                if(!newfields[i].equals(fields0[i])) {
                    out.println("ERROR: Failed to find fields");
                    return false;
                }
            }
        }
        for(int i=0;i<newfields.length;i++) {
            if(newfields[i].equals("null")&&!keyT0[i].equals("NULL")) {
                out.println("ERROR: No data found in 'NOT NULL' type");
                return false;
            }
            if(keyT0[i].equals("PRIKEY")) prikeys.add(newrecord[i]);
        }
        return true;
    }

    private boolean checkForeignValue(String foretbname, String forevalue) {
        String[] names = foretbname.split("\\(");
        String newtbname = names[0];
        String colname = names[1].replace(")","");
        Table foretable = new Table(newtbname, database);
        foretable.filein();
        if(foretable.checkValueIn(colname, forevalue)) return true;
        else {
            out.println("ERROR: Can't find the foreign field");
            return false;
        }
    }

    // Create table according to provided information of
    // field names, data types, key types, foreing keys from queries
    // Store new table to file to proper database directory
    private Table createtables() {
        String tbname = command.get(2);
        Table newtb = new Table(tbname, database);
        findtables(database);
        for(Record r:tablelists) {
            if(tbname.equals(r.gets(0))) {
                out.println("ERROR: Table name already exists");
                checktbname=false;
                return newtb;
            }
        }
        List<String> fields0 = new ArrayList<String>();
        List<String> types0 = new ArrayList<String>();
        List<String> keyT0 = new ArrayList<String>();
        List<String> foreign0 = new ArrayList<String>();

        createtables_parse(fields0, types0, keyT0, foreign0);
        String[] fields = new String[fields0.size()];
        String[] types = new String[types0.size()];
        String[] keyT = new String[keyT0.size()];
        String[] foreign = new String[fields.length];

        fields0.toArray(fields);
        types0.toArray(types);
        keyT0.toArray(keyT);
        setForeignkey(foreign0, foreign, fields);
        if(constraints==false) {
            return newtb;
        }
        newtb.init(fields, types, keyT, foreign);
        newtb.fileout();
        checktbname=true;
        return newtb;
    }

    private void createtables_parse(List<String> fields0, List<String> types0,
        List<String> keyT0, List<String> foreign0) {
        int i=3;
        while(! command.get(i).contains(");")) {
            if(command.get(i).equals("INTEGER")) {
                types0.add("INT"); i++;
            }
            else if(command.get(i).contains("VARCHAR")) {
                types0.add("STR"); i++;
            }
            else if(command.get(i).contains("(")&&
                ! command.get(i-1).equals("KEY")) {
                fields0.add(command.get(i).replace("(","")); i++;
            }
            else if(command.get(i).contains("FOREIGN")&&
                command.get(i+1).contains("KEY")) {
                foreign0.add(command.get(i+2).replace("(","").replace(")",""));
                i++;
            }
            else if(command.get(i).contains("REFERENCES")&&
                ! command.get(i+1).equals("")) {
                foreign0.add(command.get(i+1).replace(",","")); i++;
            }
            else if(command.get(i).contains(",")&&
                ! command.get(i+1).equals("FOREIGN")) {
                fields0.add(command.get(i+1)); i++;
            }
            else if(command.get(i).contains("NOT")&&
                command.get(i+1).contains("NULL")) {
                keyT0.add("NOTN"); i++;
            }
            else if(command.get(i).contains("NULL")&&
                ! command.get(i-1).contains("NOT")) {
                keyT0.add("NULL"); i++;
            }
            else if(command.get(i).contains("PRIMARY")) {
                keyT0.add("PRIKEY"); i++;
            }
            else i++;
        }
    }

    private void setForeignkey(List<String> foreign0, String[] foreign,
        String[] fields) {
        for(int i=0;i<foreign.length;i++) {
            foreign[i] = "";
        }
        for(int i=0;i<foreign0.size();i++) {
            for(int j=0;j<fields.length;j++) {
                if(foreign0.get(i).equals(fields[j])) {
                    foreign[j] = foreign0.get(i+1).replace(");","");
                }
            }
        }
        for(int i=0;i<foreign.length;i++) {
            if(foreign[i].equals("")) foreign[i] = "NULL";
        }
        findtables(database);
        for(int i=0;i<foreign.length;i++) {
            if(!foreign[i].equals("NULL")) {
                constraints = false;
                for(Record r: tablelists) {
                    if(foreign[i].contains(r.gets(0))) {
                        constraints = true;
                    }
                }
            }
            else constraints = true;
        }
    }

    // Drop table if exists
    private void droptables() {
        findtables(database);
        for(Record r: tablelists) {
            if(command.contains(r.gets(0)+";")) {
                try {
                    File tables = new File(database+"/"+r.gets(0)+".txt");
                    tables.delete();
                } catch (Exception e) {
                    out.println("ERROR: Can't find tables");
                    return;
                }
            }
        }
        EndcheckTime();
    }

    // Find all table files from the chosen database directory
    private Table findtables(String database) {
        Table showtb = new Table("showtb", database);
        tablelists = new ArrayList<Record>();
        String[] dblist = new String[1];
        dblist[0] = "Table_in_"+database;
        showtb.init(dblist);
        Path dir = Paths.get(database);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir,
            "*.{txt}")) {
            for (Path entry: stream) {
                Record showtb0 = new Record("showtb0");
                showtb0.add(entry.getFileName().toString().replace(".txt",""));
                tablelists.add(showtb0);
            }
            showtb.insert(tablelists);
        } catch (IOException e) {
            out.println("ERROR: Can't find tables");
        }
        return showtb;
    }

    private void showtables(Table showtb, String database) {
        if(database.equals("(none)")) {
            out.println("ERROR: No database selected");
            return;
        }
        showtb.printtable();
    }

    private void invalid() {
        out.println("ERROR: No query specified");
    }

    private void quitdb() {
        out.println("Bye");
        System.exit(0);
    }
}