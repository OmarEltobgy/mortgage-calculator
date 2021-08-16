package mortgagecalculator;

public class MortgageCalculatorUtils {
    private final static int MONTHLY_PAYMENTS_PER_YEAR = 12;
    private final static int BIWEEKLY_PER_YEAR = 26;
    public static double calculateMortgagePayment(MortgageCalculatorInput input) {
        long P = input.getPropertyPrice() - input.getDownPayment();
        int n = input.getAmortizationPeriod() * MONTHLY_PAYMENTS_PER_YEAR;
        double r = (input.getAnnualInterestRate() / 100.0) / MONTHLY_PAYMENTS_PER_YEAR;
        double t = Math.pow(1 + r, n);
        double M = P * (r * t) / (t - 1);
        if (input.getPaymentSchedule() == MortgageCalculatorInput.PaymentSchedule.MONTHLY) {
            return M;
        } else if (input.getPaymentSchedule() == MortgageCalculatorInput.PaymentSchedule.BIWEEKLY) {
            return M * MONTHLY_PAYMENTS_PER_YEAR / BIWEEKLY_PER_YEAR;
        } else { // ACCELERATED_BIWEEKLY
            return M / 2;
        }
    }
}
