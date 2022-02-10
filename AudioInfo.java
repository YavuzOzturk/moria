//AudioInfo class is the hearth of program which holds/arranges music data. There's an implemented 16 bit 8 bit conversion yet it will be
//improved with more data mining operations. At this phase, it helps to get audio (only wav for now) data readable and understandable for program.

import javax.sound.sampled.AudioInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class AudioInfo {
    private static final int NUM_BITS_PER_BYTE = 8;

    private AudioInputStream audioInputStream;
    private Transformer transform;
    private double[][] samplesContainer; //frequency values in time domain with channels
    private double[][] frequencyDomain; //fft result with real and imaginary parts
    private double[][] frequencyDomain2; 
    private double[][] magnitude;	//magnitude values with channels

	
    protected int sampleMax = 0;
    protected int sampleMin = 0;
    protected int sampleMaxFFT = 0;
    protected int sampleMinFFT = 0;
    protected double biggestSample;
    protected double biggestSampleFFT;
    public double[] sampleArray;
   


    public AudioInfo(AudioInputStream aiStream, int second) throws FileNotFoundException, UnsupportedEncodingException {

        this.audioInputStream = aiStream;
        this.createSampleArrayCollection();
        this.frequencyDomain = new double[2][32768];
        this.magnitude = new double[2][32768];
        this.transform = new Transformer();
        createFFTs(0);
        
        
    }

    public void createFFTs(int second) throws FileNotFoundException, UnsupportedEncodingException{
        if(this.getNumberOfChannels() == 1){
        this.frequencyDomain = transform.fastFourier(this.getSamplesContainer()[0], second);
        this.magnitude[0] = this.calculateFFTMag(this.frequencyDomain);}
        else if(this.getNumberOfChannels() == 2){
            this.frequencyDomain = transform.fastFourier(this.getSamplesContainer()[0], second);
            this.magnitude[0] = this.calculateFFTMag(this.frequencyDomain);
            this.frequencyDomain2 = transform.fastFourier(this.getSamplesContainer()[1], second);
            this.magnitude[1] = this.calculateFFTMag(this.frequencyDomain2);
        }
    }
    
    public int getNumberOfChannels(){
        int numBytesPerSample = audioInputStream.getFormat().getSampleSizeInBits() / NUM_BITS_PER_BYTE;
        return audioInputStream.getFormat().getFrameSize() / numBytesPerSample;
    }

    private void createSampleArrayCollection() {
        try {
            audioInputStream.mark(Integer.MAX_VALUE);
            audioInputStream.reset();
            byte[] bytes = new byte[(int) (audioInputStream.getFrameLength()) * ((int) audioInputStream.getFormat().getFrameSize())];
            int result;
            try {
                result = audioInputStream.read(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
           

            //convert sample bytes to channel separated 16 bit samples
            this.samplesContainer =  getSampleArray(bytes);

            //find biggest sample. used for interpolating the yScaleFactor
            if (sampleMax > sampleMin) {
                biggestSample = sampleMax;
            } else {
                biggestSample = Math.abs(((double) sampleMin));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected double[][] getSampleArray(byte[] eightBitByteArray) {
        double[][] toReturn = new double[getNumberOfChannels()][eightBitByteArray.length / (2 * getNumberOfChannels())];
        int index = 0;

        //loop through the byte[]
        for (int t = 0; t < eightBitByteArray.length;) {
            //for each iteration, loop through the channels
            for (int a = 0; a < getNumberOfChannels(); a++) {
                //do the byte to sample conversion
                int low = (int) eightBitByteArray[t];
                t++;
                int high = (int) eightBitByteArray[t];
                t++;
                int sample = (high << 8) + (low & 0x00ff);

                if (sample < sampleMin) {
                    sampleMin = sample;
                } else if (sample > sampleMax) {
                    sampleMax = sample;
                }
                //set the value.
                toReturn[a][index] = sample;
            }
            index++;
        }

        return toReturn;
    }

    public double[] getAudio(int channel){
        return this.samplesContainer[channel];
    }
    
    public double[][] getSamplesContainer(){
    	return this.samplesContainer;    	
    }
    
    public void seeSamples(){
    	System.out.println(this.biggestSample + ", " + this.sampleMax + ", " + this.sampleMin);
    }    
    
    public int getSampleSize(){
    	
    	return this.audioInputStream.getFormat().getSampleSizeInBits();
    }
    
    public int getSampleRate(){
    	
    	return (int)this.audioInputStream.getFormat().getSampleRate();
    }
    
    public double[] calculateFFTMag(double[][] sampleArray) throws FileNotFoundException, UnsupportedEncodingException{
    	double[] magnitude =  this.transform.magnitude(sampleArray);
    	this.biggestSampleFFT = this.transform.getBiggestSample(this.magnitude[0]);
    	return magnitude;
    }
    
    public double[] getFFTMag(int channelIndex){
    	return this.magnitude[channelIndex];
    }

    
}