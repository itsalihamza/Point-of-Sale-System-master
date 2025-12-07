/**
 * Refactored validation logic.
 * Centralizes input validation to prevent scattered validation code.
 * 
 * REFACTORING TYPE: Extract Method + Replace Magic Numbers
 * SMELL ADDRESSED: No Input Validation, Magic Numbers
 */
public class ValidationHelper {
    
    /**
     * Validates a phone number.
     * 
     * @param phoneNumber Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhoneNumber(long phoneNumber) {
        return phoneNumber >= BusinessConstants.MIN_PHONE_NUMBER 
            && phoneNumber <= BusinessConstants.MAX_PHONE_NUMBER;
    }
    
    /**
     * Validates a phone number string.
     * 
     * @param phoneNumberStr Phone number string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumberStr) {
        if (phoneNumberStr == null || phoneNumberStr.length() != BusinessConstants.PHONE_NUMBER_LENGTH) {
            return false;
        }
        try {
            long phoneNumber = Long.parseLong(phoneNumberStr);
            return isValidPhoneNumber(phoneNumber);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates a credit card number.
     * 
     * @param cardNumber Credit card number string
     * @return true if valid, false otherwise
     */
    public static boolean isValidCreditCard(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        String cleanedNumber = cardNumber.replaceAll("\\s+", "");
        return cleanedNumber.length() == BusinessConstants.CREDIT_CARD_LENGTH 
            && cleanedNumber.matches("\\d+");
    }
    
    /**
     * Validates an item ID.
     * 
     * @param itemId Item ID to validate
     * @return true if valid (positive integer), false otherwise
     */
    public static boolean isValidItemId(int itemId) {
        return itemId > 0;
    }
    
    /**
     * Validates a quantity.
     * 
     * @param quantity Quantity to validate
     * @return true if valid (positive), false otherwise
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
    
    /**
     * Validates a price.
     * 
     * @param price Price to validate
     * @return true if valid (non-negative), false otherwise
     */
    public static boolean isValidPrice(double price) {
        return price >= 0.0;
    }
    
    /**
     * Validates a username.
     * 
     * @param username Username to validate
     * @return true if valid (not null/empty, alphanumeric), false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]+$");
    }
    
    /**
     * Validates a password.
     * 
     * @param password Password to validate
     * @return true if valid (not null/empty), false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty();
    }
}
