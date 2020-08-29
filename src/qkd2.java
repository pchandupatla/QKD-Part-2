// Name: Avery Crawley
// Name: Adam Spiegel

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class qkd2{

    public static void main(String[] args) {

        int n = 100; // number of photons
        Random rand = new Random();
        final double APN = 2;
        final double PDC = .1;
        final double sampleProp = .50;

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
        // This should be a string of '+'s and 'x's with '+'=H/V, 'x'=D/A.
        String basisAlice = new String();

        for (int i=0; i<n; i++) {       // Iterate over the number of qubits.
            // Append a random character ('+' or 'x') to the end.
            if (rand.nextInt(2)==0) {   // Flip a coin (+ or x).
                basisAlice += '+';
            }
            else {
                basisAlice += 'x';
            }
        }

        // Alice selects a photon state according to the key and basis.
        // This should be a string of the characters 'H', 'V', 'D', 'A'.
        String photonAlice = new String();

        final int ASCII_OFFSET = 48;
        String HVBasis = "HV";
        String DABasis = "DA";
        for(int index = 0; index < basisAlice.length(); index++) {
            char ch;
            int keyBit = keyAlice.charAt(index) - ASCII_OFFSET;
            if(basisAlice.charAt(index) == '+') {
                ch = HVBasis.charAt(keyBit);
            } else {
                ch = DABasis.charAt(keyBit);
            }
            photonAlice += ch;
        }

        // Alice prepares and sends each photon.
        // Use the methods of the Photon class to prepare each photon.
        Photon[] photonArray = new Photon[n];
        for (int i=0; i<n; i++) {
            photonArray[i] = new Photon();
        }

        for(int index = 0; index < photonAlice.length(); index++) {
            char ch = photonAlice.charAt(index);

            if(ch == 'H') {
                photonArray[index].prepareH(APN);
            }else if(ch == 'V') {
                photonArray[index].prepareV(APN);
            }else if(ch == 'D') {
                photonArray[index].prepareD(APN);
            }else {
                photonArray[index].prepareA(APN);
            }
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
        int ultraInstinct = (int)(.2 * n);
        ArrayList<Integer> indexArray = new ArrayList<>();
        while(indexArray.size() != ultraInstinct)
        {
            int boo = (int) (Math.abs(rand.nextLong()) % n);
            if(!indexArray.contains(boo))
            {
                indexArray.add(boo);
            }
        }

        StringBuilder sampleIndexBuilder = new StringBuilder();
        for (int i = 0; i < n; i++)
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

        interceptIndex = sampleIndexBuilder.toString();


        // Eve chooses a basis to measure each intercepted photon.
        // basisEve should be a string of n characters.
        // Use the convention '+'=H/V, 'x'=D/A, ' '=not measured
        String basisEve;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < interceptIndex.length(); i++) {
            int randInt = rand.nextInt(2);
            if(randInt == 0)
            {
                builder.append('+');//.5 chance of either H/V or D/A
            }
            else
            {
                builder.append('x');
            }
        }
        basisEve = builder.toString();

        // Eve performs a measurement on each photon.
        // Use the methods of the Photon class to measure each photon.
        // outcomeEve should be a string of n characters.
        // Use the convention 'H','V','D','A', ' '=not measured
        String outcomeEve;
        // TODO: Put your code here.
        builder.setLength(0);
        double darkCount = 0.2;
        for (int i = 0; i < interceptIndex.length(); i++)
        {
            Photon p = photonArray[i];
            char measurementBasis = basisEve.charAt(i);
            if(measurementBasis == '+' && interceptIndex.charAt(i) == '1')
            {
                builder.append(p.measureHV(darkCount));
            }
            else if(measurementBasis == 'x' && interceptIndex.charAt(i) == '1')
            {
                builder.append(p.measureDA(darkCount));
            }
            else
            {
                builder.append(' ');
            }
        }

        outcomeEve = builder.toString();

        double averagePhotonNumber = 3; //determines amplitude of sent photon (must find the Goldilocks amp)
        for (int i = 0; i < interceptIndex.length(); i++)
        {
            Photon p = photonArray[i];
            char polarization = outcomeEve.charAt(i);
            switch(polarization)
            {
                case('H'): p.prepareH(averagePhotonNumber);break;
                case('V'): p.prepareV(averagePhotonNumber);break;
                case('D'): p.prepareD(averagePhotonNumber);break;
                case('A'): p.prepareA(averagePhotonNumber);break;
                case('N'):
                case ('M'): int a = rand.nextInt(4);
                    switch(a)
                    {
                        case(0): p.prepareH(averagePhotonNumber); break;
                        case(1): p.prepareV(averagePhotonNumber); break;
                        case(2): p.prepareD(averagePhotonNumber); break;
                        case(3): p.prepareA(averagePhotonNumber); break;
                    }
                    break;
            }
            photonArray[i] = p;
        }

        // Eve resends photons to Bob.
        // Be sure to handle the cases in which Eve gets an invalid measurement.



        // OPTIONAL: Put any other nasty tricks here.

        // Bob   --------------------------------------------

        // Bob chooses a basis to measure each photon.
        // This should be a string of '+'s and 'x's with '+'=H/V, 'x'=D/A.
        String basisBob = new String();

        for (int i=0; i<n; i++) {       // Iterate over the number of qubits.
            // Append a random character ('+' or 'x') to the end.
            if (rand.nextInt(2)==0) {   // Flip a coin (+ or x).
                basisBob += '+';
            }
            else {
                basisBob += 'x';
            }
        }

        int numInvalids = 0;

        // Bob performs a measurement on each photon.
        // Use the methods of the Photon class to measure each photon.
        // outcomeBob should be a string of n characters.
        // Use the convention 'H','V','D','A', ' '=not measured
        String outcomeBob = new String();

        for(int index = 0; index < basisBob.length(); index++) {
            String nextOutcome;
            if(basisBob.charAt(index) == '+') {
                nextOutcome = photonArray[index].measureHV(PDC);
            } else if(basisBob.charAt(index) == 'x'){
                nextOutcome = photonArray[index].measureDA(PDC);
            } else {
                nextOutcome = " ";
            }

            outcomeBob += nextOutcome;
        }

        // Bob infers the raw key.
        // keyBob should be a string of n characters.
        // Use the convention '0', '1', '-'=invalid measurement
        String keyBob = new String();

        for(int index = 0; index < outcomeBob.length(); index++) {
            char ch = outcomeBob.charAt(index);
            String nextKeyBit;

            if(ch == 'H' || ch == 'V') {
                nextKeyBit = "" + HVBasis.indexOf(ch);
            }else if (ch == 'D' || ch == 'A'){
                nextKeyBit = "" + DABasis.indexOf(ch);
            }else {
                nextKeyBit = "-";
                numInvalids++;
            }

            keyBob += nextKeyBit;
        }


        // -----------------------------------------------------------
        // Alice and Bob now publicly announce which bases they chose.
        // Bob also announces which of his measurements were invalid.
        // -----------------------------------------------------------


        // Alice and Bob extract their sifted keys.
        // siftedAlice and siftedBob should be strings of length n.
        // Use the convention '0', '1', ' '=removed
        String siftedAlice = new String();
        String siftedBob   = new String();
        int numLost = 0;

        for(int index = 0; index < basisAlice.length(); index++) {
            char chAlice = basisAlice.charAt(index);
            char chBob = basisBob.charAt(index);

            if(keyBob.charAt(index)!='-' && chBob == chAlice) {
                siftedAlice += keyAlice.charAt(index);
                siftedBob += keyBob.charAt(index);
            } else {
                siftedAlice += ' ';
                siftedBob += ' ';
                numLost++;
            }
        }


        // Alice and Bob use a portion of their sifted keys to estimate the quantum bit error rate (QBER).
        // sampleIndex should be a string of n characters.
        // Use the convention '0'=ignored, '1'=sampled
        // The QBER is the fraction of mismatches within the sampled portion.
        // For large samples, it should be close to the actual QBER,
        // which Alice and Bob, of course, do not know.
        String sampleIndex = "";

        boolean shuffleSample[] = new boolean[keyAlice.length() - numLost];

        for(int index = 0; index < shuffleSample.length*sampleProp; index++) {
            shuffleSample[index] = true;
        }

        for(int i=0; i<shuffleSample.length-1; i++) {
            int j = (int)(Math.random()*(shuffleSample.length-1-i) + i);
            boolean t = shuffleSample[i];
            shuffleSample[i] = shuffleSample[j];
            shuffleSample[j] = t;
        }


        int sindex = 0;
        for(int index = 0; index < siftedAlice.length(); index++) {
            char a = siftedAlice.charAt(index);
            if(a!=' ') {
                sampleIndex += shuffleSample[sindex++]?'1':'0';
            }
            else sampleIndex += '0';
        }

        double sampledBobQBER = 0;
        int errors = 0;
        int numSampled = 0;

        for(int index = 0; index < siftedAlice.length(); index++) {

            if (sampleIndex.charAt(index)=='1') {
                char a = siftedAlice.charAt(index);
                char b = siftedBob.charAt(index);
                if (a != b) {
                    errors++;
                }
                numSampled++;
            }
        }

        sampledBobQBER = (double)errors/numSampled;

        // Alice and Bob remove the portion of their sifted keys that was sampled.
        // Since a portion of the sifted key was publicly revealed, it cannot be used.
        // secureAlice and secureBob should be strings of length n.
        // Use the convention '0', '1', ' '=removed
        String secureAlice = "";
        String secureBob   = "";

        for(int index = 0; index < siftedAlice.length(); index++) {
            char a = siftedAlice.charAt(index);

            if(sampleIndex.charAt(index)=='0') {
                secureAlice += a;

                char b = siftedBob.charAt(index);
                secureBob += b;
            }
            else {
                secureAlice += " ";
                secureBob += " ";
            }
        }


        // Alice and Bob make a hard determination whether the channel is secure.
        // If it looks like there's something fishy, better hit the kill switch!
        Boolean channelSecure = true; // default value, to be changed to false if Eve suspected

        if(sampledBobQBER>0.1 || numInvalids>0.5*n) {
            channelSecure = false;
        }


        // Eve ------------------------------------------------------------------

        // Eve infers the raw key.
        // keyEve should be a string of n characters.
        // Use the convention '0', '1', '-'=invalid measurement, ' '=not measured
        String keyEve;
        // TODO: Put your code here.
        StringBuilder rawKeyBuilder = new StringBuilder();
        for (int i = 0; i < outcomeEve.length(); i++)
        {
            char outcome = outcomeEve.charAt(i);
            if(outcome == 'H' || outcome =='D')
            {
                rawKeyBuilder.append(0);
            }
            else if (outcome == 'V' || outcome == 'A')
            {
                rawKeyBuilder.append(1);
            }
            else if (outcome == 'N' || outcome == 'M')
            {
                rawKeyBuilder.append('-');
            }
            else
            {
                rawKeyBuilder.append(' ');
            }
        }
        keyEve = rawKeyBuilder.toString();


        // Eve extracts her sifted key.
        // Knowing what Alice and Bob have publically revealed, Eve
        // now selects which portion of her sifted key to keep.
        // stolenEve should be strings of length n.
        // Use the '0', '1', ' '=removed
        String stolenEve = "";
        builder.setLength(0);
        for (int i = 0; i < n; i++)
        {
            char aliceBasis = basisAlice.charAt(i);
            char eveBasis = basisEve.charAt(i);
            char bobBasis = basisBob.charAt(i);
            char keyEveBit = keyEve.charAt(i);
            if(aliceBasis == eveBasis && bobBasis == eveBasis && keyEveBit != '-' && sampleIndex.charAt(i) == '0'
                && keyBob.charAt(i) != '-')
            {
                builder.append(keyEveBit);
            }
            else
            {
                builder.append(' ');
            }
        }
        stolenEve = builder.toString();


        // ANALYSIS -------------------------------------------------------------

        // Below is a standard set of metrics to evaluate each protocol.
        // You need not change any of what follows.
        int outsideCount = 0;
        double count = 0;
        for (int i = 0; i < n; i++)
        {
            char aliceKey = secureAlice.charAt(i);
            char eveKey = stolenEve.charAt(i);
            boolean bool = aliceKey != ' ' && eveKey != ' ';
            if((aliceKey == eveKey) && bool)
            {
                count++;
            }
            if(aliceKey != ' ')
            {
                outsideCount++;
            }
        }
        System.out.println(count / outsideCount * 100);
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
        System.out.println("sampledBobQBER = " + Double.toString(sampledBobQBER));
        System.out.println("actualBobQBER  = " + Double.toString(actualBobQBER));
        System.out.println("actualEveQBER  = " + Double.toString(actualEveQBER));
        System.out.println("");
        System.out.println("secureKeyRateBob = " + Double.toString(secureKeyRateBob/1000) + " kbps");
        System.out.println("stolenKeyRateEve = " + Double.toString(stolenKeyRateEve/1000) + " kbps");

        // Your goal is to maximize secureKeyRateBob and minimize stolenKeyRateEve.

    }

}
