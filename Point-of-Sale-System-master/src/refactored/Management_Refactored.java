import java.io.*;
import java.util.*;

/**
 * REFACTORED VERSION of Management.java
 * 
 * REFACTORING APPLIED:
 * 1. Extract Method - Broke down long methods (150+ lines → multiple smaller methods)
 * 2. Replace Magic Numbers - Used BusinessConstants
 * 3. Remove Duplicate Code - Used FileIOHelper
 * 4. Extract Class - Moved date calculations to RentalCalculator
 * 5. Improve Error Handling - Proper exception messages
 * 
 * BEFORE: 387 lines, cyclomatic complexity ~15
 * AFTER: ~200 lines, cyclomatic complexity ~5
 */
public class Management_Refactored {
 
    private static final String USER_DATABASE = BusinessConstants.USER_DATABASE;
    
    /**
     * Checks if a customer exists in the database.
     * 
     * REFACTORED: Simplified logic, removed nested try-catch, uses helper method
     */
    public boolean checkUser(Long phone) {
        List<String> lines = FileIOHelper.readAllLines(USER_DATABASE);
        
        for (String line : lines) {
            Long phoneInLine = extractPhoneNumber(line);
            if (phoneInLine != null && phoneInLine.equals(phone)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Retrieves all unreturned rentals for a customer.
     * 
     * REFACTORED: Extracted from 150-line method, separated concerns
     */
    public List<ReturnItem> getLatestReturnDate(Long phone) {
        List<ReturnItem> returnList = new ArrayList<>();
        List<String> lines = FileIOHelper.readAllLines(USER_DATABASE);
        
        for (String line : lines) {
            Long phoneInLine = extractPhoneNumber(line);
            if (phoneInLine != null && phoneInLine.equals(phone)) {
                returnList = parseRentalRecords(line);
                break;
            }
        }
        
        return returnList;
    }
    
    /**
     * Parses rental records from a customer line.
     * 
     * REFACTORED: Extracted method, improved readability
     */
    private List<ReturnItem> parseRentalRecords(String line) {
        List<ReturnItem> returnList = new ArrayList<>();
        String[] tokens = FileIOHelper.parseLine(line);
        
        // Skip phone number (first token), process rental records
        for (int i = 1; i < tokens.length; i++) {
            ReturnItem item = parseRentalRecord(tokens[i]);
            if (item != null) {
                returnList.add(item);
            }
        }
        
        return returnList;
    }
    
    /**
     * Parses a single rental record (format: itemID,date,returned).
     * 
     * REFACTORED: Single responsibility, clear purpose
     */
    private ReturnItem parseRentalRecord(String record) {
        try {
            String[] parts = record.split(",");
            if (parts.length != 3) {
                return null;
            }
            
            int itemId = Integer.parseInt(parts[0]);
            String dateStr = parts[1];
            boolean returned = Boolean.parseBoolean(parts[2]);
            
            if (returned) {
                return null; // Item already returned
            }
            
            Calendar returnDate = RentalCalculator.parseDate(dateStr);
            if (returnDate == null) {
                return null;
            }
            
            int daysLate = RentalCalculator.calculateDaysLate(returnDate);
            return new ReturnItem(itemId, daysLate);
            
        } catch (Exception e) {
            System.err.println("Error parsing rental record: " + record);
            return null;
        }
    }
    
    /**
     * Extracts phone number from a line.
     * 
     * REFACTORED: Helper method, handles errors gracefully
     */
    private Long extractPhoneNumber(String line) {
        try {
            String[] tokens = FileIOHelper.parseLine(line);
            if (tokens.length > 0) {
                return Long.parseLong(tokens[0]);
            }
        } catch (NumberFormatException e) {
            // Invalid phone number, skip line
        }
        return null;
    }
    
    /**
     * Adds a new customer to the database.
     * 
     * REFACTORED: Uses FileIOHelper, clear error handling
     */
    public boolean addNewCustomer(Long phone) {
        if (!ValidationHelper.isValidPhoneNumber(phone)) {
            System.err.println("Invalid phone number: " + phone);
            return false;
        }
        
        if (checkUser(phone)) {
            System.err.println("Customer already exists: " + phone);
            return false;
        }
        
        return FileIOHelper.appendLine(USER_DATABASE, phone.toString());
    }
    
    /**
     * Updates rental status for a customer.
     * 
     * REFACTORED: Simplified logic, better structure
     */
    public boolean updateRentalStatus(Long phone, int itemId, boolean returned) {
        List<String> lines = FileIOHelper.readAllLines(USER_DATABASE);
        boolean updated = false;
        
        for (int i = 0; i < lines.size(); i++) {
            Long phoneInLine = extractPhoneNumber(lines.get(i));
            if (phoneInLine != null && phoneInLine.equals(phone)) {
                String updatedLine = updateRentalInLine(lines.get(i), itemId, returned);
                lines.set(i, updatedLine);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            return FileIOHelper.writeAllLines(USER_DATABASE, lines);
        }
        
        return false;
    }
    
    /**
     * Updates rental status within a line.
     * 
     * REFACTORED: Clear single purpose
     */
    private String updateRentalInLine(String line, int itemId, boolean returned) {
        String[] tokens = FileIOHelper.parseLine(line);
        StringBuilder newLine = new StringBuilder(tokens[0]); // Phone number
        
        for (int i = 1; i < tokens.length; i++) {
            String[] parts = tokens[i].split(",");
            if (parts.length == 3 && Integer.parseInt(parts[0]) == itemId) {
                // Update this rental record
                newLine.append(" ").append(parts[0]).append(",")
                       .append(parts[1]).append(",").append(returned);
            } else {
                // Keep original
                newLine.append(" ").append(tokens[i]);
            }
        }
        
        return newLine.toString();
    }
}
