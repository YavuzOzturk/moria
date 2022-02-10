//Transformer class is for different transformation methods such as, Discrete Fourier Transformation, Fast Fourier Transformation, magnitude in decibel 
//calculation. This class will hold more methods at further development such as Power Spectral Density conversion, Mel Frequency Spectral Coefficients 
//calculation and/or for to calculate values for simple Wavelet graphics according to needs. 

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Transformer {
	
	public Transformer(){
		
	}
	//for 32768 samples per turn
	public double[][] discreteFourier(double[] samples, int dftCount){
		// temporary declarations
		double[] dftReal = new double[32768];
		double[] dftImag = new double[32768];
		double[][] result = new double[2][32768];

		//method implementation
		for(int i=0; i<32768; i++){
			dftReal[i] = 0;
			dftImag[i] = 0;
			if(i%1000 == 0){
			}
			for(int j=0; j<32768; j++){
				dftReal[i] += samples[j]*(Math.cos((-2) * Math.PI * i * j / (double)32768));
				dftReal[i] += samples[j]*(Math.sin((-2) * Math.PI * i * j / (double)32768));
			}

		}
		result[0] = dftReal;
		result[1] = dftImag;
		
		return result;
	}
	
	public double[][] fastFourier(double[] samples, int dftCount){

		double[][] result = new double[2][32768];
		double[] temp2 = new double[32768];
		for(int t=0 ; t < 32768 ; t++){
			temp2[t] = samples[t + dftCount*32768];
			result[0][t] = temp2[t];
		}
		
        // bit reversal permutation
        int shift = 1 + Integer.numberOfLeadingZeros(result[0].length);
        for (int k = 0; k < 32768; k++) {
            int j = Integer.reverse(k) >>> shift;
            if (j > k) {
            	
            	double[] temp = new double[2]; //real and imag parts
            	temp[0] = result[0][j];
            	temp[1] = 0;
            	result[0][j] = result[0][k];
            	result[1][j] = 0;
            	result[0][k] = temp[0];
            	result[1][k] = temp[1];
            }
        }

        // butterfly updates
        double[] w = new double[2];
        double[] tao = new double[2];
        for (int L = 2; L <= result[0].length; L = L+L) {
            for (int k = 0; k < L/2; k++) {
                double kth = -2 * k * Math.PI / L;
                
                w[0] = Math.cos(kth);
                w[1] = Math.sin(kth);

                for (int j = 0; j < result[0].length/L; j++) {
                	
                	tao[0] = w[0]*result[0][(j*L) + k + (L/2)] - w[1]*result[1][(j*L) + k + (L/2)];
                	tao[1] = w[0]*result[1][(j*L) + k + (L/2)] + w[1]*result[0][(j*L) + k + (L/2)];
                	
                	result[0][j*L + k + (L/2)] = result[0][j*L + k] - tao[0];
                	result[1][j*L + k + (L/2)] = result[1][j*L + k] - tao[1];
                	
                	result[0][j*L + k] = result[0][j*L + k] + tao[0];
                	result[1][j*L + k] = result[1][j*L + k] + tao[1]; 
                }
            }
        }
        
        return result;
	}
	
	//Magnitude in dB
	public double[] magnitude(double[][] array) throws FileNotFoundException, UnsupportedEncodingException{
		double[] magnitude = new double[32768];
		PrintWriter writer = new PrintWriter("tmags.txt", "UTF-8");


		for(int i = 0; i < 32768; i++ ){
			magnitude[i] = 20 * Math.log10((array[0][i]*array[0][i]) + (array[1][i]*array[1][i]));
			
			//To read audio data manually for error checking
			//if(magnitude[i] > 200){
				writer.println("Magnitude: " + magnitude[i] + ", at :" + i);
			//}
		}
		writer.close();
		return magnitude;
	}
	
	public double getBiggestSample(double[] array){
		double temp = 0;
		for(int i=0; i<array.length; i++){
			if(array[i] > temp){
				temp = array[i];
			}
		}

		return temp;
	}

}