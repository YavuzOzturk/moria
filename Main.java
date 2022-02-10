import java.io.*;

import javax.sound.sampled.*;


public class Main {

	
	public static void main(String[] args) {
		
	/*	try {
			final Interface gui = new Interface();
		} catch (IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					Interface newInterface = new Interface();
					newInterface.display();
				} catch (IOException | UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });


	
}
}