import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
public class ClassWriter {
	static PrintWriter out;
	static ArrayList<String[]> vars = new ArrayList<String[]>(0);
	static String className = "";
	static Scanner stdin = new Scanner(System.in);
	public static void main(String[] args) throws FileNotFoundException{
		
		stdin.useDelimiter("\\n");
		System.out.print("Enter class name: ");
		className = stdin.next().trim();
		out = new PrintWriter(className+".java");
		out.println("public class "+className+" {");
		getVars();
		getConstsGetSet();
		out.close();
		
	}
	private static void getVars(){
		stdin.nextLine();
		System.out.println("Enter instance variables (Type-name): ");
		String allvars = stdin.nextLine();
		String[] varray = allvars.split(" ");
		for(int i = 0; i < varray.length; i++){//read vars from input
			String unparsed = varray[i];
			vars.add(new String[]{unparsed.substring(0, unparsed.indexOf('-')), unparsed.substring(unparsed.indexOf('-')+1)});
		}
		Iterator<String[]> it = vars.iterator();
		while(it.hasNext()){
			String[] temp = it.next();
			out.println("\t"+temp[0]+" "+temp[1]+";");
		}
	}
	private static void getConstsGetSet(){
		System.out.println("Enter constructors, getters, setters (line by line): ");
		String history = "";
		while(true){
			String line = stdin.nextLine().trim();
			if(history.contains("|"+line+"|")){
				System.out.println("You already created that method!");
			}else{
				if(line.equals("end")) break;
				
				if(line.startsWith("const")) printConsts(line);
				if(line.startsWith("get")) printGet(line);
				if(line.startsWith("set")) printSet(line);
				history+="|"+line+"|";
			}
		}
	}
	private static void printConsts(String line){
		out.print("\n\tpublic "+className+"(");
		ArrayList<String> varNames = new ArrayList<String>(0);
		String[] arr = line.split(" ");
		for(int i = 1; i < arr.length; i++){
			Iterator<String[]> it = vars.iterator();
			String type = "";
			while(it.hasNext()){
				String[] temp = it.next();
				if(temp[1].equals(arr[i]))
					out.print(temp[0]+" _"+arr[i]);
			}
			if(i<arr.length-1)
				out.print(", ");
		}
		out.println(") {");
		for(int i = 1; i < arr.length; i++){
			out.println("\t\t"+arr[i]+" = _"+arr[i]+";");
		}
		out.println("\t}");
	}
	private static void printGet(String _line){
		String[] line = _line.split(" ");
		Iterator<String[]> it = vars.iterator();
		String type = "";
		while(it.hasNext()){
			String[] temp = it.next();
			if(temp[1].equals(line[1]))
				out.println("\tpublic "+temp[0]+" get"+capitalizeFirst(line[1])+"() {");
			
		}
		out.println("\t\treturn "+line[1]+";");
		out.println("\t}\n");
	}
	private static void printSet(String _line){
		String[] line = _line.split(" ");
		out.print("\tpublic void set"+capitalizeFirst(line[1])+"(");
		Iterator<String[]> it = vars.iterator();
		while(it.hasNext()){
			String[] temp = it.next();
			if(temp[1].equals(line[1]))
				out.println(temp[0]+" _"+line[1]+") {");
		}
		out.println("\t\t"+line[1]+" = _"+line[1]+";");
		out.println("\t}\n");
	}
	private static String capitalizeFirst(String s){
		if(s.length()>1)
		return (s.charAt(0)+"").toUpperCase()+s.substring(1);
		return s.toUpperCase();
	}
}
