//Let's display audio data visually

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class PanelContainer extends JPanel {    

	private static final long serialVersionUID = -5015273246175285762L;
	private ArrayList<SingleWaveformPanel> singleChannelWaveformPanels;
	private ArrayList<SpectrumPanel> spectrumPanels;
	private AudioInfo audioInfo;
	public int second = 0;
   
	public PanelContainer() {
		setLayout(new GridLayout(0,1));
	}

	public void setAudioToDisplay(AudioInputStream audioInputStream) throws FileNotFoundException, UnsupportedEncodingException{
		this.singleChannelWaveformPanels = new ArrayList<SingleWaveformPanel>();
		this.spectrumPanels = new ArrayList<SpectrumPanel>();
		this.audioInfo = new AudioInfo(audioInputStream, this.second);
		for (int t=0; t<audioInfo.getNumberOfChannels(); t++){
			SingleWaveformPanel waveformPanel
				= new SingleWaveformPanel(audioInfo, t, this.second);
			this.singleChannelWaveformPanels.add(waveformPanel);
			add(createChannelDisplay(waveformPanel, t));
			
			SpectrumPanel spectrumPanel = new SpectrumPanel(audioInfo, t, this.second);
			this.spectrumPanels.add(spectrumPanel);
			add(createSpectrumGraphic(spectrumPanel, t));
		}
	}
	private JComponent createChannelDisplay(
			SingleWaveformPanel waveformPanel,
			int index) {

       JPanel panel = new JPanel(new BorderLayout());
	   panel.add(waveformPanel, BorderLayout.CENTER);

	   JLabel label = new JLabel("Channel " + ++index);
	   panel.add(label, BorderLayout.NORTH);   

	   return panel; 
	} 
	
	private JComponent createSpectrumGraphic(
			SpectrumPanel spectrumpanel, int index){
		JPanel spanel = new JPanel(new BorderLayout());
		spanel.add(spectrumpanel, BorderLayout.CENTER);
		
		JLabel slabel = new JLabel("Spectrum Analysis " + ++index);
		spanel.add(slabel, BorderLayout.NORTH);
		
		return spanel;
	}
	
	public AudioInfo getAudioInfo(){
		return this.audioInfo;
	}
}