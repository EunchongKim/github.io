import java.io.*;
import java.util.*;
import java.nio.file.*;

/* Hold table and make operations including insert, select, delete and update.
 * These operations can support both formats of with and without keys.
 * Besides, printing table, reading table from txt file and exporting it to file
 * are processed seperately when they are needed.
 * Table and database name, fields(columns) names, each field's information about
 * data types, key types, and foreign keys the current table holds
 * can be stored and read.
 * Main units in this class are done some tests inside here. */
class Table {

    private PrintStream out = System.out;
    private Map<String, Record> table;
    private String[] fields;
    private String[] types;
    private String[] keyT;
    private String[] foreign;
    private String[] els;
    private int[] space;
    private String tbname;
    private String database;
    private int colsize;
    private int checkequal;
    private long startTime;
    private long endTime;
    private double totalTime;

    Table(String tbname, String database) {
        this.tbname = tbname;
        this.database = database;
    }

    String getname() {
        return tbname;
    }

    int getcolsize() {
        return colsize;
    }

    int getDatasize() {
        return table.size();
    }

    void getinfo(String[] fields0, String[] types0, String[] keyT0,
        String[] foreign0) {
        for(int i=0;i<colsize;i++) {
            fields0[i] = fields[i];
            types0[i] = types[i];
            keyT0[i] = keyT[i];
            foreign0[i] = foreign[i];
        }
    }

    void getfields(String[] fields0) {
        for(int i=0;i<colsize;i++) {
            fields0[i] = fields[i];
        }
    }

    // Init table just with fields information
    void init(String[] fields) {
        table = new TreeMap<String, Record>();
        initfields(fields);
        initspace();
        voidinit();
    }

    // Init table with fields, data types, key types and foreing keys
    // Make space info to calculate each field's length to print
    void init(String[] fields, String[] types, String[] keyT, String[] foreign) {
        table = new TreeMap<String, Record>();
        initfields(fields);
        inittypes(types);
        initkeys(keyT);
        initforeign(foreign);
        initspace();
    }

    void initfields(String[] fields) {
        this.fields = fields;
        colsize = fields.length;       
    }

    private void initspace() {
        space = new int[colsize];
        for(int i=0;i<colsize;i++) {
            space[i] = fields[i].length();
        }
    }

    private void inittypes(String[] types) {
        this.types = types;
    }

    private void initkeys(String[] keyT) {
        this.keyT = keyT;
    }

    private void initforeign(String[] foreign) {
        this.foreign = foreign;
    }

    private void voidinit() {
        types = new String[colsize];
        keyT = new String[colsize];
        foreign = new String[colsize];
        for(int i=0;i<colsize;i++) {
            types[i] = "";
            keyT[i] = "";
            foreign[i] = "";
        } 
    }

    // Parse record's each field data and store into string array
    private void readlines(Record records) {
        els = new String[colsize];
        for(int i=0; i<colsize; i++) {
            els[i] = records.gets(i);
        }
    }

    private boolean checkinvalid(List<Record> records) {
        for(Record r: records) {
            readlines(r);
            if(els.length!=colsize) return true;          
        }
        return false;
    }

    // Check the specific value is in the field, especially for constraints.
    boolean checkValueIn(String colname, String element) {
        for(int i=0;i<fields.length;i++) {
            if(fields[i].equals(colname)) {
                for(Map.Entry<String,Record> entry: table.entrySet()) {
                    readlines(entry.getValue());
                    if(els[i].equals(element)) return true;
                }
            }
        }
        return false;
    }

    private void EndcheckTime() {
        endTime = System.currentTimeMillis();
        totalTime = (double)(endTime - startTime);
    }

    void select_key(List<String> keys, List<Record> records) {
        if (keys.isEmpty()) invalid();
        try {
            for (int i=0; i<keys.size(); i++) {
                records.add(table.get(keys.get(i)));
            }
        }
        catch (Exception e) { fail(); }
    }

    void select(List<Record> provided, List<Record> records) {
        if (provided.isEmpty()) invalid();
        try {
            for (int i=0; i<provided.size(); i++) {
                for(Map.Entry<String,Record> entry : table.entrySet()) {
                    readlines(provided.get(i));
                    comparelines(entry.getValue());
                    if(checkequal==colsize) records.add(entry.getValue());
                }
            }
        } catch (Exception e) { fail(); }
    }

    // Return new table which contains selected records
    // It supports to select all fields where conditions meet,
    // only to choose selected fields according to conditions
    // by using different functions to tables with keys or without keys
    Table selectcol_where(List<String> colselect, String where, String whereVal) {
        startTime = System.currentTimeMillis();
        Table newselect = new Table("select", database);
        List<Integer> newpos = new ArrayList<Integer>();
        List<String> forkeys = new ArrayList<String>();
        String[] newfields;
        String newwhere = "";
        int pos = 0;
        if(colselect.get(0).equals("*")) {
            colselect = new ArrayList<String>();
            for(int i=0;i<fields.length;i++) {
                colselect.add(fields[i]);
            }
        }
        newfields = new String[colselect.size()];
        for(int i=0;i<colselect.size();i++) {
            for(int j=0;j<fields.length;j++) {
                if(colselect.get(i).equals(fields[j])) {
                    newfields[i] = colselect.get(i);
                    newpos.add(j);
                }
                if(where.equals(fields[j])) {
                    newwhere = where;
                    pos = j;
                }
            }
        }
        if(newfields.length!=colselect.size()||newwhere.equals("")) {
            out.println("ERROR: Can't select fields");
            return newselect;
        }
        newselect.init(newfields);
        if(keyT[pos].equals("PRIKEY")) {
            List<Record> newrecord = new ArrayList<Record>();
            forkeys.add(whereVal);
            select_key(forkeys, newrecord);
            newselect.insert(forkeys, newrecord);
        }
        else {
            selectcol_where_nokeys(newselect, pos, whereVal, newpos);
        }
        EndcheckTime();
        out.println("Select time ("+totalTime/1000+" sec)");
        return newselect;
    }

    private void selectcol_where_nokeys(Table newselect, int pos,
        String whereVal, List<Integer> newpos) {
        for (Map.Entry<String,Record> entry : table.entrySet()) {
            readlines(entry.getValue());
            if(els[pos].equals(whereVal)) {
                List<Record> newrecord = new ArrayList<Record>();
                List<String> findcolVal = new ArrayList<String>();
                String combined = "";
                for(int i=0;i<newpos.size();i++) {
                    findcolVal.add(els[newpos.get(i)]);
                }
                for(String s: findcolVal) {
                    combined += s + ", ";
                }
                Record newtemp = new Record("select");
                newtemp.add(combined);
                newrecord.add(newtemp);
                newselect.insert(newrecord);
            }
        }
    }

    // Check record's each value of columns is same as provided one
    private void comparelines(Record record) {
        List<String> temp = new ArrayList<String>();
        for(int i=0;i<els.length;i++) {
            temp.add(els[i]);
        }
        readlines(record);
        checkequal = 0;
        for(int j=0;j<colsize;j++) {
            if(els[j].replace(",","").equals(temp.get(j).replace(",",""))) {
                checkequal++;
            }
        }        
    }

    private void findmax(String[] data) {
        for(int i=0; i<colsize; i++) {
            if(data[i].length() > space[i]) {
                space[i] = data[i].length();
            }
        }
    }

    void insert(List<Record> records) {
        if (records.isEmpty()||checkinvalid(records)) invalid();
        for (int i=0; i<records.size(); i++) {
            readlines(records.get(i));
            findmax(els);
            if(! checkInttype()) { return; }
            table.put(Integer.toString(table.size()),records.get(i));
        }
    }

    void insert(List<String> keys, List<Record> records) {
        if (keys.isEmpty()||keys.size()!=records.size()||
            checkinvalid(records)) invalid();
        for (int i=0; i<keys.size(); i++) {
            readlines(records.get(i));
            findmax(els);
            if(! checkInttype()) { return; }
            table.put(keys.get(i), records.get(i));
        }
    }

    // Check provided data to insert has proper data type
    private boolean checkInttype() {
        for(int j=0;j<colsize;j++) {
            if(types[j].equals("INT")&&!checkint(els[j])) {
                out.println("ERROR: Insert right data types"+"\n");
                return false;
            }
        }
        return true;
    }

    private boolean checkint(String elem) {
        try {
            Integer.parseInt(elem);
            return true;
        } catch (Exception e) { return false; }
    }

    void delete_key(List<String> keys) {
        if (keys.isEmpty()) invalid();
        try {
            for (int i=0; i<keys.size(); i++) {
                table.remove(keys.get(i));
            }
        }
        catch (Exception e) { fail(); }
    }

    void delete(List<Record> records) {
        if (records.isEmpty()||checkinvalid(records)) invalid();
        try {
            for (int i=0; i<records.size(); i++) {
                String pos=null;
                for(Map.Entry<String,Record> entry : table.entrySet()) {
                    readlines(records.get(i));
                    comparelines(entry.getValue());
                    if(checkequal==colsize) pos = entry.getKey();
                }
                if(pos!=null) {
                    table.remove(pos);
                    reorder(Integer.parseInt(pos));
                }
            } 
        }
        catch (Exception e) { fail(); }
    }

    // If row numbers are used as keys, modify row numbers after delete
    private void reorder(int i) {
        for (int k=i;k<table.size();k++) {
            if(k==i) {
                table.put(Integer.toString(k),
                    table.get(Integer.toString(k+1)));
            }
            else {
                table.replace(Integer.toString(k),
                    table.get(Integer.toString(k+1)));
            }
        }
        table.remove(Integer.toString(table.size()-1));
    }

    void deleteall() {
        table.clear();
    }

    void update_key(List<String> keys, List<Record> records) {
        if (keys.isEmpty()||keys.size()!=records.size()||
            checkinvalid(records)) invalid();
        try {
            for (int i=0; i<keys.size(); i++) {
                table.replace(keys.get(i), records.get(i));
                readlines(records.get(i));
                findmax(els);
            }
        }
        catch (Exception e) { fail(); }
    }

    void update(List<Record> olds, List<Record> records) {
        if (olds.isEmpty()||olds.size()!=records.size()||
            checkinvalid(records)) invalid();
        try {
            for (int i=0; i<olds.size(); i++) {
                String pos = null;
                for(Map.Entry<String,Record> entry : table.entrySet()) {
                    readlines(records.get(i));
                    comparelines(entry.getValue());
                    if(checkequal==colsize) pos = entry.getKey();
                }
                if(pos!=null) {
                    table.replace(pos, records.get(i));
                    readlines(records.get(i));
                    findmax(els);
                }
            }
        } catch (Exception e) { fail(); }
    }

    void printtable() {
        int gap = 0;
        printline();
        for(int i=0; i<colsize; i++) {
            gap = space[i] - fields[i].length() + 1;
            if(types[i].equals("INT")) {
                gap = gap-1;
                out.print("| "+String.join("",Collections.nCopies(gap," "))+
                    fields[i]+" ");
            }
            else {
                out.print("| "+fields[i]+
                    String.join("",Collections.nCopies(gap," ")));
            }
        }
        out.println("|");
        printline();
        for(Map.Entry<String,Record> entry : table.entrySet()){
            readlines(entry.getValue());
            for(int i=0; i<colsize; i++) {
                gap = space[i] - els[i].length() + 1;
                if(types[i].equals("INT")) {
                    gap = gap-1;
                    out.print("| "+String.join("",Collections.nCopies(gap," "))+
                        els[i]+" ");
                }
                else {
                    out.print("| "+els[i]+
                        String.join("",Collections.nCopies(gap," ")));
                }
            }
            out.println("|");
        }
        printline();
    }

    private void printline() {
        for(int i=0; i<colsize; i++) {
            out.print("+"+String.join("",Collections.nCopies(space[i]+2,"-")));
        }
        out.println("+");
    }

    // Read table from file
    // The first line is fields, the second line is data types,
    // the third line is primary key, null or not null,
    // the fourth line is foreign table and field name.
    // If table has records and it has a primary key, insert using keys.
    // If not, insert record with row numbers
    void filein() {
        try {
            File file = new File(database+"/"+tbname+".txt");
            Scanner in = new Scanner(file);
            List<Record> tests = new ArrayList<Record>();
            List<String> prikeys = new ArrayList<String>();
            String line = in.nextLine();
            String[] fields = line.split(", ");
            line = in.nextLine();
            String[] types = line.split(", ");
            line = in.nextLine();
            String[] keyT = line.split(", ");
            line = in.nextLine();
            String[] foreign = line.split(", ");
            
            init(fields, types, keyT, foreign);
            while (in.hasNextLine()) {
                line = in.nextLine();
                Record temp = new Record("fromtable");
                temp.add(line);
                tests.add(temp);
            }
            if(! line.equals(Arrays.toString(foreign).replace("[","").
                replace("]",""))) {
                for(int i=0;i<colsize;i++) {
                    if(keyT[i].equals("PRIKEY")) {
                        for(int j=0;j<tests.size();j++) {
                            prikeys.add(tests.get(j).gets(i));
                        }
                    }
                }
                if(prikeys.size()!=0) insert(prikeys,tests);
                else insert(tests);
            }
            in.close();
        } catch(Exception e) { fail(); }
    }

    // Make file from the table
    void fileout() {
        try {
            FileWriter fileout = new FileWriter(database+"/"+tbname+".txt");
            PrintWriter printfile = new PrintWriter(fileout);
            printfile.println(Arrays.toString(fields).replace("[","").
                replace("]",""));
            printfile.println(Arrays.toString(types).replace("[","").
                replace("]",""));
            printfile.println(Arrays.toString(keyT).replace("[","").
                replace("]",""));
            printfile.println(Arrays.toString(foreign).replace("[","").
                replace("]",""));
            for(Map.Entry<String,Record> entry : table.entrySet()) {
                readlines(entry.getValue());
                String[] lines = new String[els.length];
                printfile.println(Arrays.toString(els).
                    replace("[","").replace("]",""));        
            }
            printfile.close();
        } catch(Exception e) { fail(); }
    }

    void invalid() {
        out.println("Invalid input");
        return;
    }

    void fail() {
        out.println("Can't find the value");
        return;
    }

    void claim(boolean b) {
        if (!b) throw new Error("Test " + testNumber + " fails");
        testNumber++;
    }
    private int testNumber = 1;

    private void run() {
        out.println(">>Test Start<<");
        test();
        out.println(">>Test End<<");
    }

    private void test() {
        // Insert using row numbers (test 1 to 4)
        String[] test0 = {"Id", "Name", "Kind", "Owner"};
        String[] test1 = {"INT", "STR", "STR", "STR"};
        String[] test2 = {"PRIKEY", "NULL", "NULL", "FORKEY"};
        String[] test3 = {"NULL", "NULL", "NULL", "People"};
        init(test0, test1, test2, test3);
        List<Record> testinsert = new ArrayList<Record>();
        Record temp0 = new Record("testtable");
        temp0.add("1, Fido, dog, ab123");
        testinsert.add(temp0);
        Record temp1 = new Record("testtable");
        temp1.add("2, Wanda, fish, ef789");
        testinsert.add(temp1);
        Record temp2 = new Record("testtable");
        temp2.add("3, Garfield, cat, ab123");
        testinsert.add(temp2);        
        insert(testinsert);
        claim(table.size()==3);
        claim(table.get(Integer.toString(0)).equals(temp0));
        claim(table.get(Integer.toString(1)).equals(temp1));        
        claim(table.get(Integer.toString(2)).equals(temp2));

        // Insert with keys (test 5 to 7)
        List<Record> insertkey = new ArrayList<Record>();
        Record temp3 = new Record("testtable");
        temp3.add("4, Bolt, dog, cd456");
        insertkey.add(temp3);
        Record temp4 = new Record("testtable");
        temp4.add("5, Nimo, fish, cd456");
        insertkey.add(temp4);
        List<String> testkeys = new ArrayList<String>();
        testkeys.add("Bolt");
        testkeys.add("Nimo");
        insert(testkeys, insertkey);
        claim(table.size()==5);
        claim(table.get("Bolt").equals(temp3));        
        claim(table.get("Nimo").equals(temp4));

        // Select with keys (test 8 to 10)
        List<Record> testselect0 = new ArrayList<Record>();
        select_key(testkeys, testselect0);
        claim(testselect0.size()==2);
        claim(testselect0.get(0).equals(temp3));
        claim(testselect0.get(1).equals(temp4));

        // Select using records (test 11 to 14)
        List<Record> testselect1 = new ArrayList<Record>();
        select(testinsert, testselect1);
        claim(testselect1.size()==3);
        claim(testselect1.get(0).equals(temp0));
        claim(testselect1.get(1).equals(temp1));
        claim(testselect1.get(2).equals(temp2));        

        // Update with keys (test 15 to 16)
        List<Record> testupdate0 = new ArrayList<Record>();
        Record temp5 = new Record("testtable");
        Record temp6 = new Record("testtable");
        temp5.add("4, Bolt, ultradog, qz595");
        temp6.add("5, Nimo, missingfish, cd456");
        testupdate0.add(temp5);
        testupdate0.add(temp6);
        update_key(testkeys, testupdate0);
        claim(table.get("Bolt").equals(temp5));
        claim(table.get("Nimo").equals(temp6));

        // Update using records (test 17 to 19)
        List<Record> testupdate1 = new ArrayList<Record>();
        temp0.add("1, Waldo, cutedog, ab123");
        testupdate1.add(temp0);
        temp1.add("2, Nimo, fish, ef789");
        testupdate1.add(temp1);
        temp2.add("3, Miyao, cat, ab123");
        testupdate1.add(temp2);
        update(testinsert, testupdate1);
        claim(table.get(Integer.toString(0)).equals(temp0));
        claim(table.get(Integer.toString(1)).equals(temp1));
        claim(table.get(Integer.toString(2)).equals(temp2));

        // Delete with keys (test 20 to 23)
        delete_key(testkeys);
        claim(table.size()==3);
        claim(table.get(Integer.toString(0)).equals(temp0));
        claim(table.get(Integer.toString(1)).equals(temp1));
        claim(table.get(Integer.toString(2)).equals(temp2));

        // Delete using records (test 24)
        delete(testinsert);
        claim(table.size()==0);

        // Read file (test 25 to 28)
        tbname = "readtext";
        filein();
        claim(table.size()==3);
        claim(table.get(Integer.toString(1)).gets(0).equals(temp0.gets(0)));
        claim(table.get(Integer.toString(2)).gets(1).equals(temp1.gets(1)));
        claim(table.get(Integer.toString(3)).gets(2).equals(temp2.gets(2)));

        // Store table to file (test 29 to 36)
        insert(testkeys, testupdate0);
        tbname = "maketext";
        fileout();
        delete_key(testkeys);
        delete(testupdate1);
        claim(table.size()==0);
        filein();
        claim(table.size()==5);
        claim(table.get(Integer.toString(1)).gets(0).equals(temp0.gets(0)));
        claim(table.get(Integer.toString(2)).gets(1).equals(temp1.gets(1)));
        claim(table.get(Integer.toString(3)).gets(2).equals(temp2.gets(2)));
        claim(table.get(Integer.toString(4)).gets(3).equals(temp5.gets(3)));
        claim(table.get(Integer.toString(5)).gets(0).equals(temp6.gets(0)));
        deleteall();
        claim(table.size()==0);
    }

    public static void main(String[] args) {
        Table test = new Table("table", "test");
        test.run();
    }
}
