package CompilerResources;

import java.util.ArrayList;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public class HashTable {
	private String name;
	private int location;
	private ArrayList<Integer> lineList = new ArrayList<>();
	private float value;
	private String type;
	private int hash;

	public HashTable() {
		this.hash = 0;
		this.name = "";
		this.location = 0;
		this.value = 0;
		this.type = "";
	}
	
	public HashTable( int hash, String name, int location, int line, float value, String type ) {
		this.hash = hash;
		this.name = name;
		this.location = location;
		this.lineList.add( line );
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation( int location ) {
		this.location = location;
	}

	public float getValue() {
		return value;
	}

	public void setValue( float value ) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public int getHash() {
		return hash;
	}

	public void setHash( int hash ) {
		this.hash = hash;
	}
	
	public void addLine( int line ) {
		lineList.add( line );
	}
	
	public String getLineList() {
		String temp = "";
		for( Integer line : lineList ) {
			temp += line + " ";
		}
		return temp;
	}
}
