/**
 * Refactored constants class.
 * Centralizes all business rule constants for easy configuration.
 * 
 * REFACTORING TYPE: Replace Magic Numbers with Named Constants
 * SMELL ADDRESSED: Magic Numbers (20+ occurrences)
 */
public class BusinessConstants {
    
    // Tax Configuration
    public static final double TAX_RATE = 0.06;
    public static final double TAX_MULTIPLIER = 1.06;
    
    // Discount Configuration
    public static final float COUPON_DISCOUNT_RATE = 0.10f;
    public static final float COUPON_MULTIPLIER = 0.90f;
    
    // Rental Configuration
    public static final int RENTAL_PERIOD_DAYS = 14;
    public static final float LATE_FEE_DAILY_RATE = 0.10f;
    
    // Validation Constants
    public static final long MIN_PHONE_NUMBER = 1000000000L;
    public static final long MAX_PHONE_NUMBER = 9999999999L;
    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final int CREDIT_CARD_LENGTH = 16;
    
    // Database Paths
    public static final String EMPLOYEE_DATABASE = "Database/employeeDatabase.txt";
    public static final String ITEM_DATABASE = "Database/itemDatabase.txt";
    public static final String USER_DATABASE = "Database/userDatabase.txt";
    public static final String COUPON_DATABASE = "Database/couponNumber.txt";
    public static final String EMPLOYEE_LOG = "Database/employeeLogfile.txt";
    public static final String SALE_INVOICE = "Database/saleInvoiceRecord.txt";
    public static final String RETURN_RECORD = "Database/returnSale.txt";
    public static final String RENTAL_DATABASE = "Database/rentalDatabase.txt";
    public static final String TEMP_FILE = "Database/temp.txt";
    
    // Prevent instantiation
    private BusinessConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}
