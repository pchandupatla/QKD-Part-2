import java.util.Random;

public final class Qubit{

    private Complex alpha;  // |H> component
    private Complex beta;   // |V> component

    Random rand = new Random();

    public Qubit() {
        alpha = new Complex();
        beta  = new Complex();
    }

    public Qubit(Complex a, Complex b) {
        alpha = a;
        beta  = b;
    }

    // This is for debugging purposes only!
    public String toString() {
        String str = new String();
	if ( alpha.isReal() ){
	    str = alpha.toString() + "|H> ";
	}
	else {
	    str = "(" + alpha.toString() + ")|H> ";
	}
	if ( beta.isReal() ) {
	    if ( beta.getRe() >= 0 ){
		str += "+ " + Double.toString(beta.getRe()) + "|V>";
	    }
	    else {
		str += "- " + Double.toString(-beta.getRe()) + "|V>";
	    }
	}
	else {
	    str += "+ (" + beta.toString() + ")|V>";
	}
	return str;
    }

    public static String toString(Qubit q) {
	return q.toString();
    }

    public void prepare(Complex a, Complex b) {
	alpha = a;
        beta  = b;
    }

    public void prepareH() {
        alpha = new Complex(1,0);
        beta  = new Complex(0,0);
        this.prepare(alpha, beta);
    }

    public void prepareV() {
        alpha = new Complex(0,0);
        beta  = new Complex(1,0);
	this.prepare(alpha, beta);
    }

    public void prepareD() {
        alpha = new Complex(1/Math.sqrt(2),0);
        beta  = new Complex(1/Math.sqrt(2),0);
	this.prepare(alpha, beta);
    }

    public void prepareA() {
        alpha = new Complex(1/Math.sqrt(2),0);
        beta  = new Complex(-1/Math.sqrt(2),0);
	this.prepare(alpha, beta);
    }

    public void prepareR() {
        alpha = new Complex(1/Math.sqrt(2),0);
        beta  = new Complex(0,1/Math.sqrt(2));
	this.prepare(alpha, beta);
    }

    public void prepareL() {
        alpha = new Complex(1/Math.sqrt(2),0);
        beta  = new Complex(0,-1/Math.sqrt(2));
	this.prepare(alpha, beta);
    }

    public String measureHV() {
        double probH = alpha.absSqr();
        if ( rand.nextFloat() <= probH ) {
	    this.prepareH(); // collapse to |H> state
	    return "H";
	}
        else {
	    this.prepareV(); // collapse to |V> state
	    return "V";
	}
    }

    public String measureDA() {
        double probD = alpha.add(beta).mult(1/Math.sqrt(2)).absSqr();
        if ( rand.nextFloat() <= probD ) {
	    this.prepareD(); // collapse to |D> state
	    return "D";
	}
        else {
	    this.prepareA(); // collapse to |A> state
	    return "A";
	}
    }

    public String measureRL() {
        double probR = alpha.sub(beta.mult(new Complex(0,1))).mult(1/Math.sqrt(2)).absSqr();
        if ( rand.nextFloat() <= probR ) {
	    this.prepareR(); // collapse to |R> state
	    return "R";
	}
        else {
	    this.prepareL(); // collapse to |L> state
	    return "L";
	}
    }

}
