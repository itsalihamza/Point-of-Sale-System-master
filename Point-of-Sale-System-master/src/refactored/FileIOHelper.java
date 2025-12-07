import java.io.*;
import java.util.*;

/**
 * Refactored helper class for file I/O operations.
 * Eliminates duplicate file reading/writing code across the application.
 * 
 * REFACTORING TYPE: Extract Class + Extract Method
 * SMELL ADDRESSED: Duplicate Code (repeated ~15 times)
 */
public class FileIOHelper {
    
    /**
     * Reads all lines from a file.
     * Centralizes error handling and resource management.
     * 
     * @param filePath Path to the file
     * @return List of lines, or empty list on error
     */
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to open file: " + filePath);
        } catch (IOException ex) {
            System.err.println("Error reading file: " + filePath);
        }
        
        return lines;
    }
    
    /**
     * Writes lines to a file, overwriting existing content.
     * Uses try-with-resources for automatic resource cleanup.
     * 
     * @param filePath Path to the file
     * @param lines Lines to write
     * @return true if successful, false otherwise
     */
    public static boolean writeAllLines(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException ex) {
            System.err.println("Error writing to file: " + filePath);
            return false;
        }
    }
    
    /**
     * Appends a line to a file.
     * 
     * @param filePath Path to the file
     * @param line Line to append
     * @return true if successful, false otherwise
     */
    public static boolean appendLine(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException ex) {
            System.err.println("Error appending to file: " + filePath);
            return false;
        }
    }
    
    /**
     * Parses a space-separated line into tokens.
     * Handles empty lines and parsing errors gracefully.
     * 
     * @param line Line to parse
     * @return Array of tokens, or empty array if line is invalid
     */
    public static String[] parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        return line.split("\\s+");
    }
}
