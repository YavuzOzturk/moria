//SearchEngine class makes search operation in any database for a given data. It needs further development for search optimization. 

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SearchEngine {

	private Atom atom;
	private ArrayList<FingerPrint> fingerPrint;
	private AudioInfo audioInfo;
	private int length;
	
	protected JFileChooser repo;
	protected FileNameExtensionFilter txt;
	protected int retRep;
	protected URL Rrl;
	
	public SearchEngine(AudioInputStream newAudioInputStream) throws FileNotFoundException, UnsupportedEncodingException, MalformedURLException{
		this.audioInfo = new AudioInfo(newAudioInputStream, 0);
		this.fingerPrint = new ArrayList<FingerPrint>();
		this.length = this.audioInfo.getSamplesContainer()[0].length;
		searchMusic(this.audioInfo);
	}
	
	public void searchMusic(AudioInfo searchAudio) throws FileNotFoundException, UnsupportedEncodingException, MalformedURLException{
		this.createFingerPrint(searchAudio);
		
		//Normally program should search in database, 
		//however in our case database isn't fully implemented yet 
		//so program let user choose a text file which we filled 
		//in it with previous add music operation 
		this.repo = new JFileChooser();
		this.txt = new FileNameExtensionFilter("Only text files" , "txt");
		this.repo.setFileFilter(this.txt);
	    this.retRep = this.repo.showOpenDialog(null);
	    if(this.retRep == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You choose to search file in this: " +
	            this.repo.getSelectedFile().getPath());
	    }
	    this.Rrl = null;
	    
	    //This option is needed when jar file executed at different operating systems
	    //Windows and Linux systems have slightly different in addressing locations
	    if(System.getProperty("os.name").toLowerCase().startsWith("win")){
		this.Rrl = new URL("file:/" + repo.getSelectedFile().getPath());}
	    else{
	    this.Rrl = new URL("file://" + repo.getSelectedFile().getPath());
	    }
		
		Scanner newScanner = new Scanner(new File(this.Rrl.getPath()));

	}
	
	public void createFingerPrint(AudioInfo audioInfo) throws FileNotFoundException, UnsupportedEncodingException{
		for(int chan=0; chan < this.audioInfo.getNumberOfChannels(); chan++){
			FingerPrint newFingerPrint = new FingerPrint();
			for(int i = 0; ((i+1)*32768) < this.length; i++ ){
				this.audioInfo.createFFTs(i);
				for(int j=0; j<32768; j++){
					if(this.audioInfo.getFFTMag(chan)[j] > 250){
						Atom newAtom = new Atom((32768-j),i,(int)this.audioInfo.getFFTMag(0)[j]);
						newFingerPrint.addToFingerPrint(newAtom);}
				}
			}
			this.fingerPrint.add(newFingerPrint);
		}	
	}

	
}