package mortgagecalculator;

/**
 * A class that represents the input to the mortgage calculator
 * propertyPrice
 * downPayment
 * annual interest rate
 * amortization period (5 year increments between 5 and 30 years)
 * payment schedule (accelerated bi-weekly, bi-weekly, monthly)
 */
public class MortgageCalculatorInput {
    private Long propertyPrice;
    private Long downPayment;
    private Double annualInterestRate;
    private Integer amortizationPeriod;
    private PaymentSchedule paymentSchedule;

    public Long getPropertyPrice() {
        return propertyPrice;
    }

    public Long getDownPayment() {
        return downPayment;
    }

    public Double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public Integer getAmortizationPeriod() {
        return amortizationPeriod;
    }

    public PaymentSchedule getPaymentSchedule() {
        return paymentSchedule;
    }

    public static boolean validate(MortgageCalculatorInput mortgageCalculatorInput) {
        // All inputs are mandatory
        if (mortgageCalculatorInput.getPropertyPrice() == null ||
                mortgageCalculatorInput.getDownPayment() == null ||
                mortgageCalculatorInput.getAnnualInterestRate() == null ||
                mortgageCalculatorInput.getAmortizationPeriod() == null ||
                mortgageCalculatorInput.getPaymentSchedule() == null) {
            return false;
        }
        // values can not be negative
        if (mortgageCalculatorInput.getPropertyPrice() < 0 ||
                mortgageCalculatorInput.getDownPayment() < 0 ||
                mortgageCalculatorInput.getAnnualInterestRate() < 0 ||
                mortgageCalculatorInput.getAmortizationPeriod() < 0) {
            return false;
        }
        // down payment can not be more than property price
        if (mortgageCalculatorInput.getDownPayment() > mortgageCalculatorInput.getPropertyPrice()) {
            return false;
        }
        // down payment can not be less than 5% of the property price
        if (mortgageCalculatorInput.getDownPayment() < 0.05 * mortgageCalculatorInput.getPropertyPrice()) {
            return false;
        }
        // amortization period has to be 5 year increments between 5 and 30 years
        if (mortgageCalculatorInput.getAmortizationPeriod() % 5 != 0 ||
                mortgageCalculatorInput.getAmortizationPeriod() < 5 ||
                mortgageCalculatorInput.getAmortizationPeriod() > 30) {
            return false;
        }
        // annual interest rate can not be more that 100
        if (mortgageCalculatorInput.getAnnualInterestRate() > 100) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Input{" +
                "propertyPrice=" + propertyPrice +
                ", downPayment=" + downPayment +
                ", annualInterestRate=" + annualInterestRate +
                ", amortizationPeriod=" + amortizationPeriod +
                ", paymentSchedule=" + paymentSchedule +
                '}';
    }

    public enum PaymentSchedule {
        BIWEEKLY,
        MONTHLY,
        ACCELERATED_BIWEEKLY;
    }
}