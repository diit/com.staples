package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import debug.debug;

public class DB {
	/* This helper class holds the Query method which is used as the 
	 * controller element in our MVC structure, it parses the commands
	 * given and returns structured data sets from the model requested.
	 * 
	 * Any internal methods are also contained within this class to 
	 * maintain a simple architecture.
	 */

	// INIT clean commands (removes possibility of syntax error and internalizes command structures)
	public static String find = "find";
	public static String findFirst = "findFirst";
	public static String insert = "insert";
	public static String drop = "drop";

	// MAIN COLUMN INDEX SETUP
	static HashMap<Integer,String> products = new HashMap<Integer, String>(){{put(0,"name");put(1,"price");}};
	static HashMap<Integer,String> users = new HashMap<Integer, String>(){{put(0,"name");put(1,"password");}};
	static HashMap<Integer,String> sales = new HashMap<Integer, String>(){{put(0,"pid");put(1,"uid");}};

	static HashMap<String, HashMap<Integer, String>> columns = new HashMap<String, HashMap<Integer, String>>(){{
		put("products",products);
		put("users",users);
		put("sales",sales);
	}};

	//////////////////////
	// QUERY  TEMPLATES //
	//////////////////////

	public static HashMap<Integer, LinkedHashMap<String, String>> query(String model, String cmd){
		if(cmd.equals(find)){
			return find(model);
		}else{
			debug.notify("SYNTAX ERROR");
		}
		return null;
	};

	public static LinkedHashMap<String, String> query(String model, String cmd, int id){
		if(cmd.equals(drop)){
			drop(model, id);
		}else if(cmd.equals(findFirst)){
			return findFirst(model, id);
		}else{
			debug.notify("SYNTAX ERROR");
		}
		return null;
	};

	public static void query(String model, String cmd, int id, LinkedHashMap<String, String> Content){
		if(cmd.equals(insert)){
			insert(model, id, Content);
		}else{
			debug.notify("SYNTAX ERROR");
		}
	}

	//////////////////////
	//     COMMANDS     //
	//////////////////////

	// FIND
	private static HashMap<Integer, LinkedHashMap<String, String>> find(final String model){
		//debug
		debug.notify(model);

		try{
			RandomAccessFile record = new RandomAccessFile ("db/"+model, "rw"); //Opens DBF
			record.seek(0);
			String response;

			//Create content structure
			LinkedHashMap<Integer, LinkedHashMap<String, String>> content = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
			//Integer[] content = new Integer[(int)(long)record.length()/256];

			for(int i=0;i<((int)(long)record.length()/256);i++){
				//Set filePointer and parse output
				record.seek(i*256);
				response = record.readLine();	
				content.put(i, buildLHM(model, response));
			}

			record.close();
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static LinkedHashMap<String, String> buildLHM(final String model, final String response){
		LinkedHashMap<String,String> t = new LinkedHashMap<String, String>(){
			private static final long serialVersionUID = -8911632370183684063L;
			{
				//TODO: Remove the 2 column limit
				put(columns.get(model).get(0),normalize(response.substring(0, 128)));
				put(columns.get(model).get(1),normalize(response.substring(129, 256)));
			}
		};
		return t;
	}

	// FINDFIRST
	private static LinkedHashMap<String, String> findFirst(String model, int id){
		//debug
		debug.notify(" IN "+model+" at ROW "+id);

		//Create content structure
		LinkedHashMap<String, String> content = new LinkedHashMap<String, String>();
		try{
			RandomAccessFile record = new RandomAccessFile ("db/"+model, "rw"); //Opens DBF

			record.seek(id*256);

			String response = record.readLine();

			content.put(columns.get(model).get(0), normalize(response.substring(0, 128)));
			content.put(columns.get(model).get(1), normalize(response.substring(129, 256)));	

			record.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	// INSERT
	private static void insert(String model, int id, LinkedHashMap<String, String> content){
		//debug
		debug.notify(" INTO "+model+" at ROW "+id+" "+content.toString());

		//Create local cache
		HashMap<String, String> cache = content;

		try{
			RandomAccessFile record = new RandomAccessFile ("db/"+model, "rw"); //Opens DBF

			record.seek(id*256);

			Iterator it = cache.entrySet().iterator();
			while (it.hasNext()) { // iterates through each column and packs and writes to file
				Map.Entry pairs = (Map.Entry)it.next();
				record.writeChars(pack(pairs.getValue().toString()));
				it.remove(); // avoids a ConcurrentModificationException
			}
			record.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String pack(String obj){
		// Replaces ASCII boxes with spaces
		while(obj.length()<64){
			obj+=" ";
		}
		return obj;
	}

	//REMOVE
	private static void drop(String model, int id){
		//debug
	debug.notify(id+" IN "+model);
		
		//Empty name or price fields causes item to be ignored on output
		try{
			RandomAccessFile record = new RandomAccessFile ("db/"+model, "rw"); //Opens DBF
			
			record.seek(id*256);
			
			//Erase current values
			record.writeChars("                                                            "
					+ "                                                                    ");
			
			record.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	//Standard Methods
	private static String normalize(String o){	
		return o.toString().replaceAll("[^a-zA-Z0-9.{}=, ]", "").trim();	
	}
}
