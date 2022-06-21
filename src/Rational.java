import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * This class represents a (precise,) immutable rational number in simplest form.
 */
public final class Rational {

    /**
     * The numerator and denominator of the rational in simplified form. If the rational is an integer,
     * the denominator will be 1. The denominator is never negative.
     */
    private BigInteger num,den;

    /**
     * Constructs a new rational number. Throws IllegalArgumentException if the denominator is 0.
     * @param num The numerator of the rational number.
     * @param den The denominator of the rational number.
     */
    public Rational(long num,long den) {
        if (den == 0) throw new IllegalArgumentException();
        this.num = BigInteger.valueOf(num);
        this.den = BigInteger.valueOf(den);
        simplify();
    }

    /**
     * Equivalent constructor to new Rational(num,1), used for integers.
     * @param num The integer.
     */
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

    /**
     * Ensures the numerator and denominator are as reduced as possible.
     */
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

    /**
     * @return this + other
     */
    public Rational add(Rational other) {
        if (this.equals(new Rational(0)))
            return other;
        if (other.equals(new Rational(0)))
            return this;
        return new Rational((this.num.multiply(other.den)).add(this.den.multiply(other.num)),this.den.multiply(other.den));
    }

    /**
     * @return this - other
     */
    public Rational sub(Rational other) {
        if (this.equals(new Rational(0)))
            return other.mul(-1);
        if (other.equals(new Rational(0)))
            return this;
        return this.add(new Rational(other.num.negate(),other.den));
    }

    /**
     * @return this * other
     */
    public Rational mul(Rational other) {
        if (this.num.equals(BigInteger.ZERO) || other.num.equals(BigInteger.ZERO))
            return new Rational(0);
        Rational result = new Rational(1); // precalculate gcd1 and gcd2 to avoid simplification on result.
        BigInteger gcd1 = this.den.gcd(other.num);
        BigInteger gcd2 = this.num.gcd(other.den);
        result.num = this.num.multiply(other.num.divide(gcd1));
        result.den = this.den.multiply(other.den.divide(gcd2));
        return result;
    }

    /**
     * @return this / other
     */
    public Rational div(Rational other) {
        if (this.num.equals(BigInteger.ZERO))
            return new Rational(0);
        if (other.equals(new Rational(0))) throw new IllegalArgumentException();
        Rational result = new Rational(1); // precalculate gcd1 and gcd2 to avoid simplification on result.
        boolean negate = other.num.signum() == -1;
        BigInteger gcd1 = this.den.gcd(other.den);
        BigInteger gcd2 = this.num.gcd(other.num);
        result.num = this.num.multiply(other.den.divide(gcd1));
        result.den = this.den.multiply(other.num.divide(gcd2));
        if (negate) result = result.mul(-1);
        return result;
    }

    /**
     * @return |this|
     */
    public Rational abs() {
        if (num.signum() >= 0) return this;
        Rational r = new Rational(1);
        r.num = this.num.negate();
        r.den = this.den;
        return r;
    }

    /**
     * @return -1, 0, or 1 if this is negative, zero, or positive.
     */
    public int signum() {
        return num.signum();
    }

    /**
     * @return -1, 0, or 1 if this is this is less than, equal to, or greater than other.
     */
    public int compareTo(Rational other) {
        if (this.equals(other)) return 0; // efficiency
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

    /**
     * @return this + other
     */
    public Rational add(long other) {
        return this.add(new Rational(other));
    }

    /**
     * @return this - other
     */
    public Rational sub(long other) {
        return this.sub(new Rational(other));
    }

    /**
     * @return this * other
     */
    public Rational mul(long other) {
        return this.mul(new Rational(other));
    }

    /**
     * @return this / other
     */
    public Rational div(long other) {
        return this.div(new Rational(other));
    }

    /**
     * Exponent must be non-negative, and may not be zero if the base is zero.
     * @return this ^ exponent
     */
    public Rational pow(int exponent) {
        if (exponent < 0) throw new IllegalArgumentException();
        if (exponent == 0 && this.signum() == 0) throw new IllegalArgumentException();
        if (exponent == 0) return new Rational(1);
        Rational result = new Rational(1);
        result.num = this.num.pow(exponent);
        result.den = this.den.pow(exponent);
        return result;
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

    /**
     * @return this as an approximate BigDecimal
     */
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

    /**
     * Returns this as an approximate binary string (i.e. 1000.01 represents 33/4 or 8.25 in decimal).
     */
    public String toBinaryString(int precision) {
        StringBuilder output = new StringBuilder();
        Rational r = this;
        if (r.signum() < 0) {
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

    /**
     * Returns this as an approximate decimal string (i.e. 8.25 represents 33/4).
     */
    public String toDoubleString(int precision) {
        StringBuilder output = new StringBuilder();
        Rational r = this;
        if (r.signum() < 0) {
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