package Interfaz;

import Compilador.Semantic;
import static Compilador.Semantic.SHIFT;
import static Compilador.Semantic.SIZE;
import RecursosCompi.HashTable;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.text.StyledDocument;

public class VirtualMachine {
    
    int index, offset;
    Stack<Item> stack;
    String token;
    StringBuffer code;
    ArrayList<HashTable> hashTable;
    ArrayList<Item> memory;
    StyledDocument output;
    volatile boolean ready;
    
    public VirtualMachine(StringBuffer c, ArrayList<HashTable> t, StyledDocument txt){
        index = offset = 0;
        code = c;
        hashTable = t;
        stack = new Stack<>();
        memory = new ArrayList<>();
        output = txt;
        ready = false;
    }
    
    private void startMemory(){         //inicializa la memoria virtual
        for (int i = 0; i < hashTable.size(); i++) {
            memory.add( new Item());       //aqui podria mandar el mensaje de error de la variable no fue inicializada
        } 
        for (int i = 0; i < hashTable.size(); i++) {
            HashTable s = hashTable.get(i);
            System.out.println(s.getLocation()+" : "+s.getType());
            memory.set( s.getLocation(), new Item(s.getType()));
        } 
        //por default todas las variables se inicializan en 0 o false
    }
    
    private boolean getBoolean(String b){
        return b.equals("true");
    }
    
    public void executeCode(){      //funcion que ejecuta el codigo P del generador de codigo intermedio
        long startTime = System.currentTimeMillis();
        HashTable aux = null;
        Item a = null, b = null;
        //Scanner sc = new Scanner(System.in);
        String input;
        startMemory();
        nextToken();
        while(!token.equals("STP")){
            switch(token){
                case "LOD":         //Load: Cargar el valor de una variable
                    nextToken();
                    int loc = Semantic.LocTabla(token);
                    stack.push(memory.get(loc));
                    break;
                case "LDA":         //Load Address: Cargar la direccion de memoria de una variable
                    nextToken();
                    aux = new HashTable(Semantic.LocTabla(token),Semantic.SacaTabla(token));
                    stack.push( new Item(aux.getType(),aux.getLocation()));        //todas las direcciones son de tipo int
                    break;
                case "LDI":         //Load Int: Cargar una constante numerica de tipo int
                    nextToken();
                    stack.push( new Item("int", Integer.parseInt(token)));
                    break;
                case "LDF":         //Load Float: Cargar una constante numerica de tipo float
                    nextToken();
                    stack.push( new Item("float", Float.parseFloat(token)));
                    break;
                case "LDB":         //Load Bool: Cargar valor booleano
                    nextToken();
                    stack.push( new Item("bool", getBoolean(token)));
                    break;
                case "FJP":         //False Jump
                    nextToken();
                    a = stack.pop(); 
                    if(!a.val_b)
                        goToLabel();
                    break;
                case "JMP":         //Jump
                    nextToken();
                    goToLabel();
                    break;
                case "LAB":         //Label: Definicion de un etiqueta (nada que hacer)
                    nextToken();
                    break;  
                case "STO":         //Store
                    a = stack.pop();                //tope
                    b = stack.pop();               //direccion
                    if("int".equals(a.type)){
                        memory.set(b.val_i, new Item(a.type,a.val_i));
                        //System.out.println("------------ "+b.val_i);
                    }else if("float".equals(a.type)){
                        memory.set(b.val_i, new Item(a.type,a.val_f));
                    }else if("bool".equals(a.type)){
                        memory.set(b.val_i, new Item(a.type,a.val_b));
                    }
                    break;
                case "RD":          //Read
                    input = read();
                    //System.out.println("INPUT "+input);
                    //System.out.println(stack.size());
                    a = stack.pop();                    //direccion de memoria
                    b = memory.get(a.val_i);            //valor a sobreescribir en memoria
                    try {
                        //System.out.println("es que tipo = "+b.type);
                        //System.out.println("susrituir = "+a.val_i);
                        if(b.type != null){
                            switch (b.type) {
                                case "int":
                                    memory.set(a.val_i, new Item(b.type,Integer.parseInt(input)));
                                    break;
                                case "float":
                                    memory.set(a.val_i, new Item(b.type,Float.parseFloat(input)));
                                    break;
                                case "bool":
                                    memory.set(a.val_i, new Item(b.type,Boolean.parseBoolean(input)));
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        print("\n    Format Exception: values doesn't match" + "\n");
                        print("\n    BUILD STOPPED" + "\n");
                        return;
                    }
                    break;
                case "WRT":         //Write: Escribe el tope de la pila y extrae
                    a = stack.pop();
                    if(null != a.type){
                        switch (a.type) {
                            case "int":
                                //System.out.println("output: " + a.val_i);
                                print("    " + a.val_i + "\n");
                                break;
                            case "float":
                                //System.out.println("output: " + a.val_f);
                                print("    " + a.val_f + "\n");
                                break;
                            case "bool":
                                //System.out.println("output: " + a.val_b);
                                print("    " + a.val_b + "\n");
                                break;
                        }
                    }
                    break;
                case "ADD":         //Add: Suma los valores de tope y debajo de tope y deja el resultado en tope
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("int", a.val_i + b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("float", a.val_i + b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("float", a.val_f + b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("float", a.val_f + b.val_f));
                    break;
                case "SUB":         //Substract
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("int", a.val_i - b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("float", a.val_i - b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("float", a.val_f - b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("float", a.val_f - b.val_f));
                    break;
                case "MUL":         //Multiplication
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("int", a.val_i * b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("float", a.val_i * b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("float", a.val_f * b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("float", a.val_f * b.val_f));
                    break;
                case "DIV":         //Division
                    b = stack.pop();
                    a = stack.pop();
                    try {
                        if(a.type == "int" && b.type == "int")
                            stack.push( new Item("int", a.val_i / b.val_i));
                        else if(a.type == "int" && b.type == "float")
                            stack.push( new Item("float", a.val_i / b.val_f));
                        else if(a.type == "float" && b.type == "int")
                            stack.push( new Item("float", a.val_f / b.val_i));   
                        else if(a.type == "float" && b.type == "float")
                            stack.push( new Item("float", a.val_f / b.val_f));
                    } catch (ArithmeticException e) {
                        print("\n    Arithmetic Exception: / by zero" + "\n");
                        print("\n    BUILD STOPPED" + "\n");
                        return;
                    }
                    break;
                case "EQU":         //Equals to
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("bool", a.val_i == b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("bool", a.val_i == b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("bool", a.val_f == b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("bool", a.val_f == b.val_f));
                    break;
                case "NEQ":         //Not Equals to
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("bool", a.val_i != b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("bool", a.val_i != b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("bool", a.val_f != b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("bool", a.val_f != b.val_f));
                    break;
                case "GRT":         //Greater than
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("bool", a.val_i > b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("bool", a.val_i > b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("bool", a.val_f > b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("bool", a.val_f > b.val_f));
                    break;
                case "GEQ":         //Greater Equals to
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("bool", a.val_i >= b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("bool", a.val_i >= b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("bool", a.val_f >= b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("bool", a.val_f >= b.val_f));
                    break;
                case "LET":         //Less than
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("bool", a.val_i < b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("bool", a.val_i < b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("bool", a.val_f < b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("bool", a.val_f < b.val_f));
                    break;
                case "LEQ":         //Less Equals to
                    b = stack.pop();
                    a = stack.pop();
                    if(a.type == "int" && b.type == "int")
                        stack.push( new Item("bool", a.val_i <= b.val_i));
                    else if(a.type == "int" && b.type == "float")
                        stack.push( new Item("bool", a.val_i <= b.val_f));
                    else if(a.type == "float" && b.type == "int")
                        stack.push( new Item("bool", a.val_f <= b.val_i));   
                    else if(a.type == "float" && b.type == "float")
                        stack.push( new Item("bool", a.val_f <= b.val_f));
                    break;
                case "AND":         //And
                    b = stack.pop();
                    a = stack.pop();
                    stack.push( new Item("bool", a.val_b && b.val_b));
                    break;
                case "OR":          //Or
                    b = stack.pop();
                    a = stack.pop();
                    stack.push( new Item("bool", a.val_b || b.val_b));
                    break;
                case "NOT":         //Not
                    a = stack.pop();
                    stack.push( new Item("bool", !a.val_b));
                    break;
            }
            nextToken();
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double seg = totalTime / 1000;
        //System.out.println("\nBUILD SUCCESSFUL\n");
        print("\n    TERMINADO " + "(Tiempo total: " + seg + " segundos)" + "\n");
    }
    
    private void nextToken(){
        token = "";
        while(code.charAt(index) != '\n' && code.charAt(index) != ' '){
            token += code.charAt(index);
            index++;
        }
        index++;
        //System.out.println("takeenn "+token);
        //System.out.println("stack: " + stack.toString());
    }
    
    private void goToLabel(){
        int i = 0;
        String tkn = "";
        while(!tkn.equals(token)){
            while(!tkn.equals("LAB")){
                tkn = "";
                while(code.charAt(i) != '\n' && code.charAt(i) != ' '){
                    tkn += code.charAt(i);
                    i++;
                }
                i++;
            }
            tkn = "";
            while(code.charAt(i) != '\n' && code.charAt(i) != ' '){
                tkn += code.charAt(i);
                i++;
            }
            i++;
        }
        index = i;
        token = tkn;
    }
    
    private void print(String s){
        try {
            output.insertString(offset, s, null);
        } catch (Exception e) {}
        offset += s.length();
    }
    
    public String read(){
        String s = "";
        print("    ");
        while(!ready);
        //System.out.println("Ya esta listo");
        int i = offset, tope = output.getLength();
        int word = tope - i;
        //System.out.println("offset: " + offset);
        //System.out.println("tope: " + tope);
        //System.out.println("word: " + word);
        try {
            s = output.getText(offset, word);
        } catch (Exception e) {
            System.err.println("err "+e);
        }
        ready = false;
        offset += s.length() + 1;
        s.replace("\n", "");
        s.replace("\r", "");
        s.replace(" ", "");
        return s;
    }
    
    public int LocTabla( String name) {
	int hash = getHash( name );
	int loc = 0;
	HashTable temp = serchHash( hash );
            for ( HashTable i : hashTable ) {
		if ( i.getHash() == hash && i.getName().equals( name ) ) {
                    loc=i.getLocation();
                    break;
		}
            }
	
	return loc;
    }
    
    public HashTable SacaTabla( String name) {
	int hash = getHash( name );
	HashTable aux=null;
	HashTable temp = serchHash( hash );
            for ( HashTable i : hashTable ) {
		if ( i.getHash() == hash && i.getName().equals( name ) ) {
                    aux=i;
                    break;
		}
            }
	
	return aux;
    }
    
    private HashTable serchHash( int hash ) {
	for ( HashTable i : this.hashTable ) {
            //System.out.println(i.getName());
            //System.out.println(i.getName()+"/"+i.getHash()+"    "+hash);
            if ( i.getHash() == hash ) {
              //  System.out.println(i.getName()+"/"+i.getHash()+"    "+hash);
		return i;
            }
	}
	return null;
    }
    
    public static int getHash( String id ) {
	int temp = 0;
	int i = 0;
	char[] key = id.toCharArray();
	while ( i != id.length() ) {
            temp = ( ( temp << SHIFT ) + key[i] ) % SIZE;
            ++i;
	}
	return temp;
    }
}
