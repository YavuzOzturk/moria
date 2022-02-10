import javax.swing.*; 
import java.awt.*;


public class SingleWaveformPanel extends JPanel {
 
	private static final long serialVersionUID = -8678116112387064708L;
	protected static final Color BACKGROUND_COLOR = Color.white;
    protected static final Color REFERENCE_LINE_COLOR = Color.black;
    protected static final Color WAVEFORM_COLOR = Color.red;


    private AudioInfo helper;
    private int channelIndex;
    private double[][] sampleArray;	//2D for channels

    public SingleWaveformPanel(AudioInfo helper, int channelIndex, int second) {
        this.helper = helper;
        this.channelIndex = channelIndex;
        this.sampleArray = new double[2][32768];
        setBackground(BACKGROUND_COLOR);
        for(int i=0 ; i < 32768 ; i++){
        	this.sampleArray[0][i] = this.helper.getAudio(0)[i + second*32768];
        	if(this.helper.getNumberOfChannels() == 2 ){
        		this.sampleArray[1][i] = this.helper.getAudio(1)[i + second*32768];
        	}
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lineHeight = getHeight() / 2;
        g.setColor(REFERENCE_LINE_COLOR);
        g.drawLine(0, lineHeight, (int)getWidth(), lineHeight);

        drawWaveform(g, this.sampleArray[channelIndex]);


    }

    protected void drawWaveform(Graphics g, double[] samples) {
        if (samples == null) {
            return;
        }

        int oldX = 0;
        int oldY = (int) (getHeight() / 2);
        int xIndex = 0;

        int increment = this.getIncrement(this.getXScaleFactor(getWidth()));
        g.setColor(WAVEFORM_COLOR);
        int t = 0;

        for (t = 0; t < increment; t += increment) {
        	g.drawLine(oldX, oldY, xIndex, oldY);
            xIndex++;
            oldX = xIndex;
        }
        for (t = 0; t < samples.length; t += increment) {
            double scaleFactor = this.getYScaleFactor(getHeight());
            double scaledSample = samples[t] * scaleFactor;
            int y = (int) ((getHeight() / 2) - (scaledSample));
            g.drawLine(oldX, oldY, xIndex, y);
            xIndex++;
            oldX = xIndex;
            oldY = y;
        }
    }
    
    public double getXScaleFactor(int panelWidth){
        return ((double)panelWidth / (double)32768);
    }
    
    public double getYScaleFactor(int panelHeight){
        return ((double)panelHeight / (double)(this.helper.biggestSample * 2 * 1.2));
    }
    
    public int getIncrement(double xScale) {
        try {
            int increment = (int)  (1 / xScale);
            return increment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
}