// An example lexical analyzer that recognizes relational operators and identifiers
// DSM, 2017
// Gerardo Abril
/**
 * @author Jerry Abril
 * @Verion 1.0.0
 */
import java.io.PushbackReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Jerry Abril
 * 
 * It will read from any file and tokenize each token.
 */
public class Lexer {

	// PushbackReader supports pushing characters back on the input stream
	PushbackReader reader = null;

	/**
	 * The Lexer will perform Lexical Analysis for the given file
	 * 
	 * @param filename The file we will be reading tokens from
	 * @throws FileNotFoundException In case the file is not found
	 */
	public Lexer(String filename) throws FileNotFoundException {

		// Create a PushbackReader that can accept up to 1 pushbacks
		this.reader = new PushbackReader(new FileReader(filename), 1);
	}

	/**
	 * Will analyze for identifier key words
	 * 
	 * @return Token based on analyzed token
	 * @throws IOException Let the compiler know we know about the exception and to ignore it.
	 */
	public Token analyzeIdentifier() throws IOException {

		// Collect characters in a StringBuilder
		StringBuilder identifier = new StringBuilder();

		while (true) {
			int c = this.reader.read();

			if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_') {
				reader.unread(c);
				break;
			} else {
				identifier.append((char) c);
			}
		}

		String identString = identifier.toString();

		// Check if the name's value is a keyword
		if (identString.equalsIgnoreCase("if")) {
			return new Token(TokenType.IF); 
		} else if (identString.equalsIgnoreCase("while")) {
			return new Token(TokenType.WHILE); 
		} else if (identString.equalsIgnoreCase("then")) {
			return new Token(TokenType.THEN); 
		} else if (identString.equalsIgnoreCase("do")) {
			return new Token(TokenType.DO); 
		} else if (identString.equalsIgnoreCase("and")) {
			return new Token(TokenType.AND);
		} else if (identString.equalsIgnoreCase("or")) {
			return new Token(TokenType.OR);
		} else if (identString.equalsIgnoreCase("endif")) {
			return new Token(TokenType.ENDIF);
		} else if (identString.equalsIgnoreCase("endsub")) {
			return new Token(TokenType.ENDSUB);
		} else if (identString.equalsIgnoreCase("endwhile")) {
			return new Token(TokenType.ENDWHILE);
		} else if (identString.equalsIgnoreCase("input")) {
			return new Token(TokenType.INPUT);
		} else if (identString.equalsIgnoreCase("not")) {
			return new Token(TokenType.NOT);
		} else if (identString.equalsIgnoreCase("print")) {
			return new Token(TokenType.PRINT);
		} else if (identString.equalsIgnoreCase("return")) {
			return new Token(TokenType.RETURN);
		} else if (identString.equalsIgnoreCase("sub")) {
			return new Token(TokenType.SUB);
		} else if (identString.equalsIgnoreCase("var")) {
			return new Token(TokenType.VAR);
		} else if (identString.equalsIgnoreCase("end")){
			return new Token (TokenType.END);
		} else if (identString.equalsIgnoreCase("program")){
			return new Token (TokenType.PROGRAM);
		} else {
			return new Token(TokenType.NAME, identString);
		}
	}


	/**
	 * Will analyze for numbers in the file
	 * 
	 * @return Tokens based on analyzed token
	 * @throws IOException Let the compiler know we know about the exception and to ignore it.
	 */
	public Token analyzeNumber() throws IOException {

		// Basic strategy: read characters in a loop and add them to
		// the value of the number we're building until we read something
		// that's not a digit

		int value = 0;

		while (true) {
			int c = this.reader.read();

			if (Character.isDigit(c)) {
				value = value * 10 + Character.getNumericValue(c);
			} else {
				this.reader.unread(c);
				break;
			}
		}

		return new Token(TokenType.NUMBER, String.valueOf(value));
	}

	/**
	 * Will analyze for the next token
	 * 
	 * @return Token based on analyzed token
	 * @throws Exception Let the compiler know we know about the exception and to ignore it.
	 */
	public Token nextToken() throws Exception {

		while (true) {

			int c = reader.read();

			// End-of-file
			if (c == -1) {
				return new Token(TokenType.EOF);
			}

			// A single = is an EQUAL token
			else if (c == '=') {
				return new Token(TokenType.EQUAL); 
			} 

			// Two tokens start with >
			else if (c == '>') {
				int next = this.reader.read();

				if (next == '=') {
					return new Token(TokenType.GREATER_THAN_OR_EQUAL); 
				} else {
					this.reader.unread(next);
					return new Token(TokenType.GREATER_THAN);
				}
			}

			// Three tokens start with <
			else if (c == '<') {
				int next = this.reader.read();

				if (next == '=') {
					return new Token(TokenType.LESS_THAN_OR_EQUAL); 
				} else if (next == '>') {
					return new Token(TokenType.NOT_EQUAL);
				} else {
					this.reader.unread(next);
					return new Token(TokenType.LESS_THAN);
				}
			}

			// Basic Arithmetic
			else if (c == '+') {
				return new Token(TokenType.PLUS); 
			}else if (c == '-') {
				return new Token(TokenType.MINUS);
			}else if (c == '*') {
				return new Token(TokenType.TIMES); 
			}else if (c == '/') {
				return new Token(TokenType.DIVIDE); 
			}else if ( c == '%'){
				return new Token(TokenType.MOD);
			}

			// First character is a letter
			else if (Character.isLetter(c)) {

				// Push it back on the stack, then call analyzeIdentifier
				reader.unread(c);
				return this.analyzeIdentifier();
			}

			// First character is a digit
			else if (Character.isDigit(c)) {
				reader.unread(c);
				return this.analyzeNumber();
			}

			// This is a special case to catch a bug in PushbackReader.
			// If you unread() a -1, which can easily happen if the last
			// token in the program is a NAME or NUMBER, the PushbackReader
			// converts it internally to 65535, and will return that value
			// the next time you call read().
			else if (c == 65535) {
				continue;
			}

			// Assign and Colon
			else if (c == ':'){
				int next = this.reader.read();

				if (next == '='){
					return new Token (TokenType.ASSIGN);
				}else{
					return new Token (TokenType.COLON);
				}
			}

			else if (c == ','){
				return new Token (TokenType.COMMA);
			}

			//Parentheses
			else if (c == '('){
				return new Token (TokenType.LEFT_PAREN);
			} else if (c == ')'){
				return new Token (TokenType.RIGHT_PAREN);
			}

			else if (c == '{'){
				int next = this.reader.read ();;
				while (next != '}'){
					if (next == -1){
						return new Token (TokenType.UNKNOWN);
					}
					next = this.reader.read();
				}
				if (next == '}'){
					return new Token(TokenType.COMMENT);
				}			

			}

			else if (c == '\n'){
				return new Token (TokenType.NEWLINE);
			}

		
			else if (!Character.isWhitespace(c)){
				System.out.println ("The Error is: " + (char)c);
				return new Token (TokenType.UNKNOWN);
			}
		}
	}

	/**
	 * Main method to run the program 
	 * 
	 * @param args The mandatory arguments to run the program
	 */
	public static void main(String[] args) {

		try {
			Lexer lex = new Lexer("Test/Error.a");
			Token t;
			int lineNumber = 1;
			do {
				t = lex.nextToken();
				if (t.type == TokenType.NEWLINE) {
					lineNumber ++;
					//System.out.println(lineNumber); 
				} else if (t.type ==TokenType.UNKNOWN){
					System.out.println (t + " At line number: " + lineNumber);
				}else {
					System.out.print(t  + " ");
				}
			} while (t.type != TokenType.EOF && t.type != TokenType.UNKNOWN);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
