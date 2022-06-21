import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Rational {
    private BigInteger num,den;
    public Rational(long num,long den) {
        if (den == 0) throw new IllegalArgumentException();
        this.num = BigInteger.valueOf(num);
        this.den = BigInteger.valueOf(den);
        simplify();
    }
    public Rational(long num) {
        this.num = BigInteger.valueOf(num);
        this.den = BigInteger.ONE;
    }
    private Rational(BigInteger num, BigInteger den) {
        if (den.signum() == 0) throw new IllegalArgumentException();
        this.num = num;
        this.den = den;
        simplify();
    }

    public BigInteger getNumerator() {
        return num;
    }

    public BigInteger getDenominator() {
        return den;
    }

    private void simplify() {
        if (num.equals(BigInteger.ZERO)) {
            den = BigInteger.ONE;
            return;
        }
        BigInteger gcd = num.gcd(den);
        num = num.divide(gcd);
        den = den.divide(gcd);
        if (den.signum() < 0) {
            num = num.negate();
            den = den.negate();
        }
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Rational)) return false;
        Rational rational = (Rational) o;
        return this.den.equals(rational.den) && this.num.equals(rational.num);
    }
    public Rational add(Rational other) {
        return new Rational((this.num.multiply(other.den)).add(this.den.multiply(other.num)),this.den.multiply(other.den));
    }
    public Rational sub(Rational other) {
        return this.add(new Rational(other.num.negate(),other.den));
    }
    public Rational mul(Rational other) {
        return new Rational(this.num.multiply(other.num),this.den.multiply(other.den));
    }
    public Rational div(Rational other) {
        if (other.equals(new Rational(0))) throw new IllegalArgumentException();
        return new Rational(this.num.multiply(other.den),this.den.multiply(other.num));
    }
    public int signum() {
        return num.signum();
    }
    public int compareTo(Rational other) {
        return this.sub(other).signum();
    }
    public boolean lessThan(Rational other) {
        return this.compareTo(other) < 0;
    }
    public boolean lessThanOrEqualTo(Rational other) {
        return this.compareTo(other) <= 0;
    }
    public boolean greaterThan(Rational other) {
        return this.compareTo(other) > 0;
    }
    public boolean greaterThanOrEqualTo(Rational other) {
        return this.compareTo(other) >= 0;
    }
    public boolean equalTo(Rational other) {
        return this.compareTo(other) == 0;
    }
    public boolean notEqualTo(Rational other) {
        return this.compareTo(other) != 0;
    }
    public Rational add(long other) {
        return this.add(new Rational(other));
    }
    public Rational sub(long other) {
        return this.sub(new Rational(other));
    }
    public Rational mul(long other) {
        return this.mul(new Rational(other));
    }
    public Rational div(long other) {
        return this.div(new Rational(other));
    }
    public int compareTo(long other) {
        return this.sub(other).signum();
    }
    public boolean lessThan(long other) {
        return this.compareTo(other) < 0;
    }
    public boolean lessThanOrEqualTo(long other) {
        return this.compareTo(other) <= 0;
    }
    public boolean greaterThan(long other) {
        return this.compareTo(other) > 0;
    }
    public boolean greaterThanOrEqualTo(long other) {
        return this.compareTo(other) >= 0;
    }
    public boolean equalTo(long other) {
        return this.compareTo(other) == 0;
    }
    public boolean notEqualTo(long other) {
        return this.compareTo(other) != 0;
    }
    public int compareTo(double other) {
        return this.toBigDecimal(1000).compareTo(new BigDecimal(other));
    }
    public boolean lessThan(double other) {
        return this.compareTo(other) < 0;
    }
    public boolean lessThanOrEqualTo(double other) {
        return this.compareTo(other) <= 0;
    }
    public boolean greaterThan(double other) {
        return this.compareTo(other) > 0;
    }
    public boolean greaterThanOrEqualTo(double other) {
        return this.compareTo(other) >= 0;
    }
    public boolean equalTo(double other) {
        return this.compareTo(other) == 0;
    }
    public boolean notEqualTo(double other) {
        return this.compareTo(other) != 0;
    }
    public BigDecimal toBigDecimal(int precision) {
        return new BigDecimal(num).divide(new BigDecimal(den), precision, RoundingMode.HALF_DOWN);
    }
    public boolean isInt() {
        return this.den.equals(BigInteger.ONE);
    }
    public String toIntString() {
        if (!isInt()) throw new UnsupportedOperationException();
        return this.num.toString();
    }
    public String toBinaryString(int precision) {
        StringBuilder output = new StringBuilder();
        Rational r = this;
        if (r.lessThan(0)) {
            output.append('-');
            r = r.mul(-1);
        }
        output.append(Long.toBinaryString(Long.parseLong(r.num.divide(r.den).toString())));
        r = r.sub(r.num.divide(r.den).longValueExact()).mul(2);
        if (precision > 0) {
            output.append('.');
            int gainedPrecision = 0;
            while (gainedPrecision < precision) {
                output.append(Long.toBinaryString(Long.parseLong(r.num.divide(r.den).toString())));
                r = r.sub(r.num.divide(r.den).longValueExact()).mul(2);
                gainedPrecision++;
                if (gainedPrecision % 100 == 0) System.out.println(gainedPrecision);
            }
        }
        return output.toString();
    }
    public String toDoubleString(int precision) {
        StringBuilder output = new StringBuilder();
        Rational r = this;
        if (r.lessThan(0)) {
            output.append('-');
            r = r.mul(-1);
        }
        output.append(r.num.divide(r.den));
        r = r.sub(r.num.divide(r.den).longValueExact()).mul(10);
        if (precision > 0) {
            output.append('.');
            int gainedPrecision = 0;
            while (gainedPrecision < precision) {
                output.append(r.num.divide(r.den));
                r = r.sub(r.num.divide(r.den).longValueExact()).mul(10);
                gainedPrecision++;
            }
        }
        return output.toString();
    }
}