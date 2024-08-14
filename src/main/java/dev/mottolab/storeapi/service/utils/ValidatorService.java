package dev.mottolab.storeapi.service.utils;

public class ValidatorService {
    public static int checkMatchString(String str1, String str2) {
        int matchCount = 0;

        // Ensure both strings are of the same length
        if (str1.length() == str2.length()) {
            // Compare characters at the same position in both strings
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) == str2.charAt(i)) {
                    matchCount++;
                }
            }
        }

        return matchCount;
    }
}
