//This class is a collection of Atoms which compose a unique signature for an audio file.

import java.util.ArrayList;

public class FingerPrint {
	
	private ArrayList<Atom> fingerPrint;
	
	public FingerPrint(){
		this.fingerPrint = new ArrayList<Atom>();
	}
	
	public void addToFingerPrint(Atom newAtom){
		this.fingerPrint.add(newAtom);		
	}
	
	public Atom getFingerPrint(int index){
		return fingerPrint.get(index);
	}
	
	public ArrayList<Atom> getFingerPrint(){
		return this.fingerPrint;
	}

}