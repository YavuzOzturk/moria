import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Interface extends JPanel implements ActionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JButton searchButton, addButton;
	protected JFrame frame;
	protected JFileChooser jfc, jfs;
	protected FileNameExtensionFilter fneFilter;
	protected URL url, Srl;
	protected InputStream inputStream, SInputStream;
	protected AudioInputStream audioInputStream, SAudioInputStream;
	protected PanelContainer container;
	protected SearchEngine newSearch;
	
	protected int returnVal, retSVal;
	
	protected Deployment deploy;
	
	public Interface() throws IOException, UnsupportedAudioFileException{
		
		setButtons();

		
	}
		

		


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if("add".equals(e.getActionCommand())){
			
			try{
			
			this.frame = new JFrame("Audio Analysis Display Simulator"); 
			this.frame.setBounds(200,200, 500, 350);
			
			this.jfc = new JFileChooser();
			
			this.fneFilter = new FileNameExtensionFilter("Only wav files" , "wav" , "wave");
			jfc.setFileFilter(fneFilter);
			
		    int returnVal = jfc.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		        System.out.println("You choose to open this file: " +
		             jfc.getSelectedFile().getPath());
		     }
		    
		    if(System.getProperty("os.name").toLowerCase().startsWith("win")){
		    	this.url = new URL("file:/" + this.jfc.getSelectedFile().getPath());}
		        else{
		        this.url = new URL("file://" + this.jfc.getSelectedFile().getPath());
		        }
			this.inputStream = url.openStream();
			this.audioInputStream = AudioSystem.getAudioInputStream(this.inputStream);
			this.container = new PanelContainer(); 
			this.container.setAudioToDisplay(this.audioInputStream);
			
			this.deploy = new Deployment(this.container.getAudioInfo());
			
			this.frame.getContentPane().setLayout(new BorderLayout());		
			this.frame.getContentPane().add(container, BorderLayout.CENTER);
			//this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.frame.setVisible(true);
			this.frame.validate();
			this.frame.repaint();
			}
			
		
		catch (IOException | UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
		else if("search".equals(e.getActionCommand())){
			
			System.out.println("Search initializing");
			//Search
			
			try{
			this.jfs = new JFileChooser();
			this.fneFilter = new FileNameExtensionFilter("Only wav files" , "wav" , "wave");
			this.jfs.setFileFilter(this.fneFilter);
		    this.retSVal = this.jfs.showOpenDialog(null);
		    if(this.retSVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You choose to search this file: " +
		            this.jfs.getSelectedFile().getPath());
		    }
		    this.Srl = null;
		    if(System.getProperty("os.name").toLowerCase().startsWith("win")){
			this.Srl = new URL("file:/" + jfs.getSelectedFile().getPath());}
		    else{
		    this.Srl = new URL("file://" + jfs.getSelectedFile().getPath());
		    }
			this.SInputStream = this.Srl.openStream();
			this.SAudioInputStream = AudioSystem.getAudioInputStream(this.SInputStream);			
			this.newSearch = new SearchEngine(this.SAudioInputStream);
		}
		
		catch(IOException | UnsupportedAudioFileException e1){
			e1.printStackTrace();
			
		}
		
		}
	
	}
	
	private void setButtons(){
		this.addButton = new JButton("Add Music");
		this.addButton.setVerticalTextPosition(AbstractButton.CENTER);
		this.addButton.setHorizontalTextPosition(AbstractButton.LEADING);
		this.addButton.setToolTipText("Click this to add music");
		this.addButton.setMnemonic(KeyEvent.VK_A);
		this.addButton.setActionCommand("add");
		this.addButton.addActionListener(this);
		
		this.searchButton = new JButton("Search Music");
		this.searchButton.setVerticalTextPosition(AbstractButton.CENTER);
		this.searchButton.setHorizontalTextPosition(AbstractButton.LEADING);
		this.searchButton.setToolTipText("Click this to search music");
		this.searchButton.setMnemonic(KeyEvent.VK_S);
		this.searchButton.setActionCommand("search");
		this.searchButton.addActionListener(this);
		
		add(this.addButton);
		add(this.searchButton);
	}
	
	public static void display() throws IOException, UnsupportedAudioFileException{
        JFrame frame = new JFrame("Add or search");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.
        Interface newContentPane = new Interface();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}

}
