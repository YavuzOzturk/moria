import javax.swing.*; 
import java.awt.*;


public class SpectrumPanel extends JPanel {
 
	private static final long serialVersionUID = 1L;
	protected static final Color BACKGROUND_COLOR = Color.white;
    protected static final Color REFERENCE_LINE_COLOR = Color.black;


    private AudioInfo helper;
    private int channelIndex;

    public SpectrumPanel(AudioInfo helper, int channelIndex, int second) {
        this.helper = helper;
        this.channelIndex = channelIndex;
        setBackground(BACKGROUND_COLOR);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int lineHeight = getHeight()-1;
        g.setColor(REFERENCE_LINE_COLOR);
        g.drawLine(0, lineHeight, (int)getWidth(), lineHeight);
        drawSpectrum(g, this.helper.getFFTMag(channelIndex));

    }

  
    
    protected void drawSpectrum(Graphics g, double[] FFTMagSamples) {

    	if (FFTMagSamples == null) {
            return;
        }
        
        int oldX = 0;
        int oldY = (int) (getHeight() -1);
        int xIndex = 0;
        int increment = this.getIncrement(this.getXScaleFactor(getWidth()));
        g.setColor(Color.blue);
        int t = 0;

        for (t = 0; t < increment; t += increment) {
            g.drawLine(oldX, oldY, xIndex, oldY);
            xIndex++;
            oldX = xIndex;
        }
        for (t = 0; t < FFTMagSamples.length; t += increment) {
        	
            double scaleFactor = this.getYScaleFactor(getHeight());
            double scaledSample = FFTMagSamples[t] * scaleFactor;
            int y = (int) ((getHeight()*2) - (scaledSample));
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
    	return ((double)panelHeight / (double) this.helper.biggestSampleFFT) * (double) 2.4;
    }
    
    public int getIncrement(double xScale) {
        try {
            int increment = (int) (1 / xScale);
            return increment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}