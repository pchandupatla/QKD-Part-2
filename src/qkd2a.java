//original encryption scheme

//Alice and Bob code provided by Avery Crawley and Adam Spiegel

import java.util.ArrayList;
import java.util.Random;

public class qkd2a {

    public static void main(String[] args) {

        int n = 2000; // number of photons
        Random rand = new Random();

	// Alice --------------------------------------------

        // Alice generates the raw key.
        String keyAlice = new String(); // Declare a String object.
        for (int i=0; i<n; i++) {       // Iterate over the number of photons.
	    // Append a random character ('0' or '1') to the end.
            if (rand.nextInt(2)==0) {   // Flip a coin (0 or 1).
	        keyAlice += '0';
	    }
            else {
                keyAlice += '1';
	    }
        }

        // Alice chooses the encoding basis for each key bit.
	// This should be a string of '+'s and 'x's with '+'=H/V, 'x'=D/A, '*' = R/L
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			int randInt = rand.nextInt(3);
			if(randInt == 0)
			{
				builder.append('+');//.5 chance of either H/V or D/A
			}
			else if(randInt == 1)
			{
				builder.append('x');
			}
			else
			{
				builder.append('*');
			}
		}
		String basisAlice =  builder.toString();

		//System.out.println("basisAlice  = " + basisAlice);

        // Alice selects a photon state according to the key and basis.
	// This should be a string of the characters 'H', 'V', 'D', 'A'.
		builder.setLength(0);
		for (int i = 0; i < n; i++)
		{
			char keyBit = keyAlice.charAt(i);
			char basisBit = basisAlice.charAt(i);
			if(keyBit == '0')
			{
				if(basisBit == '+')
				{
					builder.append('H');
				}
				else if(basisBit == 'x')
				{
					builder.append('D');
				}
				else
				{
					builder.append('R');
				}
			}
			else
			{
				if(basisBit == '+')
				{
					builder.append('V');
				}
				else if(basisBit == 'x')
				{
					builder.append('A');
				}
				else
				{
					builder.append('L');
				}
			}
		}
		String photonAlice = builder.toString();
		//System.out.println(photonAlice);

		// Alice prepares and sends each photon.
	// Use the methods of the Photon class to prepare each photon.
        Photon[] photonArray = new Photon[n];
        for (int i=0; i<n; i++) {
            photonArray[i] = new Photon();
	}
		//higher means increased likelihood of incorrect measurement
		//lower means decreased likelihood of receiving photon
		double averagePhotonNumber = 3.5; //determines A of sent photon (must find the Goldilocks amp)
		for (int i = 0; i < n; i++)
		{
			Photon p = photonArray[i];
			char polarization = photonAlice.charAt(i);
			switch(polarization)
			{
				case('H'): p.prepareH(averagePhotonNumber);break;
				case('V'): p.prepareV(averagePhotonNumber);break;
				case('D'): p.prepareD(averagePhotonNumber);break;
				case('A'): p.prepareA(averagePhotonNumber);break;
				case('R'): p.prepareR(averagePhotonNumber);break;
				case('L'): p.prepareL(averagePhotonNumber);break;
			}
			photonArray[i] = p;
		}

	// Eve   --------------------------------------------

	// You should implement this section after completing Alice and Bob.
	// Eve is allowed to do whatever she wants to the photonAlice array.
	// She cannot, however, have knowledge of Alice's or Bob's choice of bases,
	// nor Bob's measurement outcomes, until they are publicly announced.

	// Eve selects a subsample of photons from Alice to measure.
	// interceptIndex should be a string of n characters.
	// Use the convention '0'=ignored, '1'=intercepted
	String interceptIndex = "";
	// TODO: Put your code here.

	// Eve chooses a basis to measure each intercepted photon.
	// basisEve should be a string of n characters.
	// Use the convention '+'=H/V, 'x'=D/A, ' '=not measured
	String basisEve = new String();
	// TODO: Put your code here.

	// Eve performs a measurement on each photon.
	// Use the methods of the Photon class to measure each photon.
	// outcomeEve should be a string of n characters.
	// Use the convention 'H','V','D','A', ' '=not measured
	String outcomeEve = new String();
	// TODO: Put your code here.

	// Eve resends photons to Bob.
	// Be sure to handle the cases in which Eve gets an invalid measurement.
	// TODO: Put your code here.

        // OPTIONAL: Put any other nasty tricks here.


	// Bob   --------------------------------------------

        // Bob chooses a basis to measure each photon.
	// This should be a string of '+'s and 'x's with '+'=H/V, 'x'=D/A.
		builder.setLength(0);
		for (int i = 0; i < n; i++) {
			int randInt = rand.nextInt(3);
			if(randInt == 0)
			{
				builder.append('+');//.5 chance of either H/V or D/A
			}
			else if(randInt == 1)
			{
				builder.append('x');
			}
			else
			{
				builder.append('*');
			}
		}
		String basisBob =  builder.toString();

	// Bob performs a measurement on each photon.
	// Use the methods of the Photon class to measure each photon.
	// outcomeBob should be a string of n characters.
	// Use the convention 'H','V','D','A', ' '=not measured
	// TODO: Adjust value
		builder.setLength(0);
		double darkCount = 0.05;
		for (int i = 0; i < n; i++)
		{
			Photon p = photonArray[i];
			char measurementBasis = basisBob.charAt(i);
			if(measurementBasis == '+')
			{
				builder.append(p.measureHV(darkCount));
			}
			else if(measurementBasis == 'x')
			{
				builder.append(p.measureDA(darkCount));
			}
			else
			{
				builder.append(p.measureRL(darkCount));
			}
		}

		String outcomeBob = builder.toString();
		//System.out.println(outcomeBob);

	// Bob infers the raw key.
	// keyBob should be a string of n characters.
	// Use the convention '0', '1', '-'=invalid measurement
		builder.setLength(0);
		for (int i = 0; i < n; i++)
		{
			char bitBob = outcomeBob.charAt(i);
			if(bitBob == 'M' || bitBob == 'N')
			{
				builder.append('-');
			}
			else if(bitBob == 'H' || bitBob =='D' || bitBob == 'R')
			{
				builder.append(0);
			}
			else
			{
				builder.append(1);
			}
		}
		String keyBob = builder.toString();

	// -----------------------------------------------------------
	// Alice and Bob now publicly announce which bases they chose.
	// Bob also announces which of his measurements were invalid.
	// -----------------------------------------------------------


	// Alice and Bob extract their sifted keys.
	// siftedAlice and siftedBob should be strings of length n.
	// Use the convention '0', '1', ' '=removed
		builder.setLength(0); // Alice
		StringBuilder bobBuilder = new StringBuilder(); // Bob

		for (int i = 0; i < n; i++)
		{
			char basisA = basisAlice.charAt(i);
			char basisB = basisBob.charAt(i);
			if(basisA == basisB && keyBob.charAt(i) != '-') //keep the bit
			{
				char aliceBit = keyAlice.charAt(i);
				char bobBit = keyBob.charAt(i);
				builder.append(aliceBit);
				bobBuilder.append(bobBit);
			}
			else
			{
				builder.append(' ');
				bobBuilder.append(' ');
			}
		}
		String siftedAlice = builder.toString();
		String siftedBob   = bobBuilder.toString();

		// Alice and Bob use a portion of their sifted keys to estimate the quantum bit error rate (QBER).
	// sampleIndex should be a string of n characters.
	// Use the convention '0'=ignored, '1'=sampled
	// The QBER is the fraction of mismatches within the sampled portion.
	// For large samples, it should be close to the actual QBER,
	// which Alice and Bob, of course, do not know.
	String sampleIndex = "";
	double sampledBobQBER = 0;
		double pineapple = .999;
		double boot = Math.log(-1*pineapple + 1) / Math.log(26.0 / 27.0) * 2;
		int ultraInstinct = (int) Math.round(boot);
		ArrayList<Integer> indexArray = new ArrayList<>();
		while(indexArray.size() != ultraInstinct)
		{
			int boo = (int) (Math.abs(rand.nextLong()) % siftedAlice.length());
			if(!indexArray.contains(boo) && siftedAlice.charAt(boo) != ' ')
			{
				indexArray.add(boo);
			}
		}

		StringBuilder sampleIndexBuilder = new StringBuilder();
		for (int i = 0; i < siftedAlice.length(); i++)
		{
			if(indexArray.contains(i))
			{
				sampleIndexBuilder.append(1);
			}
			else
			{
				sampleIndexBuilder.append(0);
			}
		}

		sampleIndex = sampleIndexBuilder.toString();

		// Alice and Bob remove the portion of their sifted keys that was sampled.
	// Since a portion of the sifted key was publicly revealed, it cannot be used.
	// secureAlice and secureBob should be strings of length n.
	// Use the convention '0', '1', ' '=removed
	String secureAlice;
	String secureBob;
	builder.setLength(0);
	bobBuilder.setLength(0);
	StringBuilder builderOne = new StringBuilder();
	StringBuilder builderTwo = new StringBuilder();
		for (int i = 0; i < siftedAlice.length(); i++)
		{
			if(sampleIndex.charAt(i) == '0')
			{
				builder.append(siftedAlice.charAt(i));
				bobBuilder.append(siftedBob.charAt(i));
			}
			else
			{
				builder.append(' ');
				bobBuilder.append(' ');
				builderOne.append(siftedAlice.charAt(i));
				builderTwo.append(siftedBob.charAt(i));
			}
		}

		String AliceQBER = builderOne.toString();
		String BobQBER = builderTwo.toString();
		int count = 0;
		for (int i = 0; i < AliceQBER.length(); i++) {
			char aliceChar = AliceQBER.charAt(i);
			char bobChar = BobQBER.charAt(i);
			if(aliceChar != bobChar)
			{
				count++;
			}
		}
		sampledBobQBER = ((double) count / AliceQBER.length());

	secureAlice = builder.toString();
	secureBob = bobBuilder.toString();

	// Alice and Bob make a hard determination whether the channel is secure.
	// If it looks like there's something fishy, better hit the kill switch!
	Boolean channelSecure = true; // default value, to be changed to false if Eve suspected
	if(sampledBobQBER > .01)
	{
		channelSecure = false;
	}

	// Eve ------------------------------------------------------------------

	// Eve infers the raw key.
	// keyEve should be a string of n characters.
	// Use the convention '0', '1', '-'=invalid measurement, ' '=not measured
	String keyEve = new String();
	// TODO: Put your code here.

	// Eve extracts her sifted key.
	// Knowing what Alice and Bob have publically revealed, Eve
	// now selects which portion of her sifted key to keep.
	// stolenEve should be strings of length n.
	// Use the '0', '1', ' '=removed
	String stolenEve = "";
	// TODO: Put your code here.


	// ANALYSIS -------------------------------------------------------------

	// Below is a standard set of metrics to evaluate each protocol.
	// You need not change any of what follows.

	// Compare Alice and Bob's sifted keys.
        int numMatchBob = 0;
	double actualBobQBER = 0;
	double secureKeyRateBob = 0;
	int secureKeyLengthBob = 0;
	for (int i=0; i<secureAlice.length(); i++) {
	    if ( secureAlice.charAt(i) != ' ' ) {
		secureKeyLengthBob += 1;
		if ( siftedAlice.charAt(i) == siftedBob.charAt(i) ) {
		    numMatchBob += 1;
		}
	    }
	}

	// Compute the actual quantum bit error rate for Bob.
	if (secureKeyLengthBob > 0) {
	    actualBobQBER = 1 - (double) numMatchBob / secureKeyLengthBob;
	}
	else {
	    actualBobQBER = Double.NaN;
	}
	// Compute the sifted key rate assuming each trial is 1 microsecond.
	if (secureKeyLengthBob > 0) {
	    secureKeyRateBob = (1-actualBobQBER) * (double) secureKeyLengthBob / n * 1e6;
	}
	else {
	    secureKeyRateBob = Double.NaN;
	}

	// Compare Alice and Eve's sifted keys.
        int numMatchEve = 0;
	double actualEveQBER = 0;
	double stolenKeyRateEve = 0;
	int stolenKeyLengthEve = 0;
	for (int i=0; i<stolenEve.length(); i++) {
	    if ( stolenEve.charAt(i) != ' ' ) {
		stolenKeyLengthEve += 1;
		if ( secureAlice.charAt(i) == stolenEve.charAt(i) ) {
		    numMatchEve += 1;
		}
	    }
	}
	// Compute the actual quantum bit error rate for Eve.
	if (stolenKeyLengthEve > 0) {
	    actualEveQBER = 1 - (double) numMatchEve / stolenKeyLengthEve;
	}
	else {
	    actualEveQBER = Double.NaN;
	}
	// Compute the sifted key rate assuming each trial is 1 microsecond.
	stolenKeyRateEve = (1-actualEveQBER) * (double) stolenKeyLengthEve / n * 1e6;


	// DISPLAY RESULTS ------------------------------------------------------

	System.out.println("");
        System.out.println("basisAlice  = " + basisAlice);
	System.out.println("basisBob    = " + basisBob);
	System.out.println("basisEve    = " + basisEve);
	System.out.println("");
        System.out.println("keyAlice    = " + keyAlice);
   	System.out.println("keyBob      = " + keyBob);
 	System.out.println("keyEve      = " + keyEve);
	System.out.println("");
	System.out.println("siftedAlice = " + siftedAlice);
	System.out.println("siftedBob   = " + siftedBob);
	System.out.println("");
 	System.out.println("secureAlice = " + secureAlice);
 	System.out.println("secureBob   = " + secureBob);
 	System.out.println("stolenEve   = " + stolenEve);
	System.out.println("");
	if (!channelSecure) {
	    secureKeyRateBob = 0;
	    stolenKeyRateEve = 0;
	    System.out.println("*********************************************");
	    System.out.println("* ALERT! The quantum channel is not secure. *");
	    System.out.println("*********************************************");
	}
	System.out.println("sampledBobQBER = " + sampledBobQBER);
 	System.out.println("actualBobQBER  = " + actualBobQBER);
 	System.out.println("actualEveQBER  = " + actualEveQBER);
 	System.out.println("");
 	System.out.println("secureKeyRateBob = " + secureKeyRateBob / 1000 + " kbps");
 	System.out.println("stolenKeyRateEve = " + stolenKeyRateEve / 1000 + " kbps");

	// Your goal is to maximize secureKeyRateBob and minimize stolenKeyRateEve.

    }

}
