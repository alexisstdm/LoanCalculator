package financial.LIBOR.loancalculator;

/**
 * Created by casa on 9/11/13.
 */
public class Mortgage {
    double notional;
    double rate;
    double spread;
    int paymentFrequency;
    int years;
    double payment;

    private void calculate(){
        double totalRate = (this.rate + this.spread)/this.paymentFrequency;
        this.payment = this.notional*totalRate*Math.pow((1.0+totalRate),
                this.years*this.paymentFrequency) /
                (Math.pow((1.0+totalRate), this.years * this.paymentFrequency)-1);
    }

    public Mortgage(double notional, double rate, double spread, int paymentFrequency,
                     int years){
        this.notional = notional;
        this.rate = rate;
        this.spread = spread;
        this.paymentFrequency = paymentFrequency;
        this.years = years;
        this.calculate();
    }

    public double getPayment(boolean recalculate){
        if (recalculate) this.calculate();
        return this.payment;
    }

    public double getPayment() {
        return this.payment;
    }

    public double getAmortization(int payment_number){
        return (this.getPayment() - this.notional*(this.rate+this.spread)/this.paymentFrequency)*
                Math.pow((1+(this.rate+this.spread)/this.paymentFrequency),(payment_number-1));
    }

    public double getInterest(int payment_number){
        return this.getPayment(true) - this.getAmortization(payment_number);
    }

    public double accumulativeAmortization(int last_payment){
        return (Math.pow(1+(this.rate+this.spread)/this.paymentFrequency, last_payment)-1)/
                (this.rate + this.spread)*
                this.paymentFrequency*
                (this.getPayment(true)-(this.rate+this.spread)/this.paymentFrequency*this.notional);
    }
}
