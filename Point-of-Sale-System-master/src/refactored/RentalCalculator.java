import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Refactored rental calculation logic.
 * Extracts complex date and late fee calculations from Management class.
 * 
 * REFACTORING TYPE: Extract Class + Extract Method
 * SMELL ADDRESSED: Long Method, God Class (Management.java)
 */
public class RentalCalculator {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
    
    /**
     * Calculates the number of days between a given date and today.
     * 
     * @param returnDate The expected return date
     * @return Number of days (positive if late, negative if early)
     */
    public static int calculateDaysLate(Calendar returnDate) {
        Calendar today = Calendar.getInstance();
        long diffInMillis = today.getTimeInMillis() - returnDate.getTimeInMillis();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }
    
    /**
     * Calculates late fee for a returned item.
     * 
     * @param itemPrice Original item price
     * @param daysLate Number of days late
     * @return Late fee amount
     */
    public static double calculateLateFee(double itemPrice, int daysLate) {
        if (daysLate <= 0) {
            return 0.0;
        }
        return itemPrice * BusinessConstants.LATE_FEE_DAILY_RATE * daysLate;
    }
    
    /**
     * Calculates the expected return date for a rental.
     * 
     * @param rentalDate The date the item was rented
     * @return Calendar object with return date
     */
    public static Calendar calculateReturnDate(Calendar rentalDate) {
        Calendar returnDate = (Calendar) rentalDate.clone();
        returnDate.add(Calendar.DATE, BusinessConstants.RENTAL_PERIOD_DAYS);
        return returnDate;
    }
    
    /**
     * Parses a date string in MM/dd/yy format.
     * 
     * @param dateString Date string to parse
     * @return Calendar object, or null if parsing fails
     */
    public static Calendar parseDate(String dateString) {
        try {
            Date date = DATE_FORMAT.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            System.err.println("Invalid date format: " + dateString);
            return null;
        }
    }
    
    /**
     * Formats a Calendar object as a date string.
     * 
     * @param calendar Calendar to format
     * @return Formatted date string (MM/dd/yy)
     */
    public static String formatDate(Calendar calendar) {
        return DATE_FORMAT.format(calendar.getTime());
    }
}
