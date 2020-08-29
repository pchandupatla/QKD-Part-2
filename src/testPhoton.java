//testPhoton.java provided by Dr. Brian la Cour and Dr. Noah Davis

import java.util.Random;

public class testPhoton{

    public static void main(String[] args) {

        Random rand = new Random();

	System.out.println("\nPhoton class test");

	int N = 10000;
	int numN, numH, numV, numR, numL, numM;
	Photon photon = new Photon();

	photon.prepareH(1);

	System.out.println("\nVacuum");
	numN = 0; numH = 0; numV = 0; numM = 0;
	for (int n=0; n<N; n++) {
	    photon.prepareVacuum();
	    switch ( photon.measureHV(0.3) ) {
	    case "N": numN += 1; break;
	    case "H": numH += 1; break;
	    case "V": numV += 1; break;
	    case "M": numM += 1; break;
	    }
	}
	System.out.println("N: " + Double.toString((double)numN/N));
	System.out.println("H: " + Double.toString((double)numH/N));
	System.out.println("V: " + Double.toString((double)numV/N));
	System.out.println("M: " + Double.toString((double)numM/N));

	System.out.println("\nPhoton");
	numN = 0; numH = 0; numV = 0; numM = 0;
	for (int n=0; n<N; n++) {
	    photon.prepareH(1);
	    switch ( photon.measureHV(0.3) ) {
	    case "N": numN += 1; break;
	    case "H": numH += 1; break;
	    case "V": numV += 1; break;
	    case "M": numM += 1; break;
	    }
	}
	System.out.println("N: " + Double.toString((double)numN/N));
	System.out.println("H: " + Double.toString((double)numH/N));
	System.out.println("V: " + Double.toString((double)numV/N));
	System.out.println("M: " + Double.toString((double)numM/N));

	System.out.println("\nVacuum w/ Polarizer");
	numN = 0; numH = 0; numV = 0; numM = 0;
	for (int n=0; n<N; n++) {
	    photon.prepareVacuum();
	    photon.applyPolarizer(rand.nextDouble()*Math.PI, rand.nextDouble()*2*Math.PI);
	    switch ( photon.measureHV(0.3) ) {
	    case "N": numN += 1; break;
	    case "H": numH += 1; break;
	    case "V": numV += 1; break;
	    case "M": numM += 1; break;
	    }
	}
	System.out.println("N: " + Double.toString((double)numN/N));
	System.out.println("H: " + Double.toString((double)numH/N));
	System.out.println("V: " + Double.toString((double)numV/N));
	System.out.println("M: " + Double.toString((double)numM/N));

	System.out.println("\nPhoton w/ Polarizer");
	numN = 0; numR = 0; numL = 0; numM = 0;
	for (int n=0; n<N; n++) {
	    photon.prepareL(2);
	    photon.applyPolarizer(Math.PI/4,-Math.PI/2);
	    switch ( photon.measureRL(0.3) ) {
	    case "N": numN += 1; break;
	    case "R": numR += 1; break;
	    case "L": numL += 1; break;
	    case "M": numM += 1; break;
	    }
	}
	System.out.println("N: " + Double.toString((double)numN/N));
	System.out.println("R: " + Double.toString((double)numR/N));
	System.out.println("L: " + Double.toString((double)numL/N));
	System.out.println("M: " + Double.toString((double)numM/N));

	System.out.println("\nPhoton w/ NoisyGate");
	numN = 0; numH = 0; numV = 0; numM = 0;
	for (int n=0; n<N; n++) {
	    photon.prepareH(1);
	    photon.applyNoisyGate(1);
	    switch ( photon.measureHV(0.3) ) {
	    case "N": numN += 1; break;
	    case "H": numH += 1; break;
	    case "V": numV += 1; break;
	    case "M": numM += 1; break;
	    }
	}
	System.out.println("N: " + Double.toString((double)numN/N));
	System.out.println("H: " + Double.toString((double)numH/N));
	System.out.println("V: " + Double.toString((double)numV/N));
	System.out.println("M: " + Double.toString((double)numM/N));

	System.out.println("\nPhoton w/ Attenuation");
	numN = 0; numH = 0; numV = 0; numM = 0;
	for (int n=0; n<N; n++) {
	    photon.prepareH(1);
	    photon.applyAttenuation(1);
	    switch ( photon.measureHV(0.3) ) {
	    case "N": numN += 1; break;
	    case "H": numH += 1; break;
	    case "V": numV += 1; break;
	    case "M": numM += 1; break;
	    }
	}
	System.out.println("N: " + Double.toString((double)numN/N));
	System.out.println("H: " + Double.toString((double)numH/N));
	System.out.println("V: " + Double.toString((double)numV/N));
	System.out.println("M: " + Double.toString((double)numM/N));

	System.out.println("\nBorn Rule");
        
	numH = 0; numV = 0;
	double theta = Math.acos(1 - 2*rand.nextDouble());
	double phi   = rand.nextDouble()*2*Math.PI;
	for (int n=0; n<N; n++) {
	    photon.prepareH(1);
	    photon.applyUnitaryGate(theta, phi, 0);
	    switch ( photon.measureHV(0.1) ) {
	    case "H": numH += 1; break;
	    case "V": numV += 1; break;
	    }
	}
	System.out.println(Double.toString((double)numH/(numH+numV)) + " (modeled) vs " + Double.toString(Math.cos(theta/2)*Math.cos(theta/2)) + " (predicted)");

    }

}
