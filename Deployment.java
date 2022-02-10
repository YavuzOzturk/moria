//Deployment class makes analyzed audio data being arranged and saved to database (or for now to a text file).

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.io.PrintWriter;

public class Deployment {

	private AudioInfo audioInfo;
	private Atom atom;
	private ArrayList<FingerPrint> fingerPrint;
	private int length;
	
	public Deployment(AudioInfo newAudioInfo) throws FileNotFoundException, UnsupportedEncodingException{
		
		this.audioInfo = newAudioInfo;
		this.length = this.audioInfo.getSamplesContainer()[0].length;
		this.fingerPrint = new ArrayList<FingerPrint>();
		createFingerPrint(this.audioInfo);
		Deploy();
	}
	
	public void createFingerPrint(AudioInfo audioInfo) throws FileNotFoundException, UnsupportedEncodingException{
		for(int chan=0; chan < this.audioInfo.getNumberOfChannels(); chan++){
			FingerPrint newFingerPrint = new FingerPrint();
			for(int i = 0; ((i+1)*32768) < this.length; i++ ){
				this.audioInfo.createFFTs(i);
				for(int j=0; j<32768; j++){
					if(this.audioInfo.getFFTMag(chan)[j] > 250){
						Atom newAtom = new Atom((32768-j),i,(int)this.audioInfo.getFFTMag(0)[j]);
						System.out.println(newAtom.toString());
						newFingerPrint.addToFingerPrint(newAtom);}
				}
			}
			this.fingerPrint.add(newFingerPrint);
		}	
	}
	
	public void Deploy() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("figP.txt", "UTF-8");
		System.out.println("Sup?");
		if(this.fingerPrint.size() == 1){
			for(int i=0; i<this.fingerPrint.get(0).getFingerPrint().size(); i++){
			writer.println(this.fingerPrint.get(0).getFingerPrint(i).toString());
			System.out.println("OK");}
		}
		else if(this.fingerPrint.size() == 2){
			for(int i=0; i<this.fingerPrint.get(0).getFingerPrint().size(); i++){
			writer.println(this.fingerPrint.get(0).getFingerPrint(i).toString());
			}
			for(int i=0; i<this.fingerPrint.get(1).getFingerPrint().size(); i++){
			writer.println(this.fingerPrint.get(1).getFingerPrint(i).toString()); }
		}
		writer.close();
	}
	

}