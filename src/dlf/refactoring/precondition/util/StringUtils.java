package dlf.refactoring.precondition.util;

import java.util.Random;

public class StringUtils {

	public static String createRandomString(String characters, int length) {
			Random rng = new Random();
			char[] text = new char[length];
		    for (int i = 0; i < length; i++) {
		        text[i] = characters.charAt(rng.nextInt(characters.length()));
		    }
		    return new String(text);
	}
	
	public static String getJavaVariableCharacters() {
		String letters = "abcdefghijklmnopqrstuvwxyz";
		String upperCaseLetters = letters.toUpperCase();
		String numbers = "0123456789";
		return letters + upperCaseLetters + numbers;
	}
}
