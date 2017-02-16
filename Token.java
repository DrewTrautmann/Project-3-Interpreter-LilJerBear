// A Token recognized by the lexical analyzer
// DSM, 2017

enum TokenType {
   AND, ASSIGN, COLON, COMMA, COMMENT, DIVIDE, DO,
   ENDIF, ENDSUB,ENDWHILE, EOF, EQUAL, GREATER_THAN, 
   GREATER_THAN_OR_EQUAL, IF, INPUT, LEFT_PAREN, LESS_THAN,
   LESS_THAN_OR_EQUAL, MINUS, MOD, NAME, NEWLINE, NOT,
   NOT_EQUAL, NUMBER, OR, PLUS, PRINT, RETURN, RIGHT_PAREN, 
   SUB, THEN, TIMES, VAR, WHILE, UNKNOWN
}

public class Token {
   TokenType type;
   String value;  // Some tokens, like IDENT, have an associated value
   
   public Token(TokenType type) {
     this.type = type;
     this.value = null;
   }
   
   public Token(TokenType type, String value) {
     this.type = type;
     this.value = value;
   }
   
   public String toString() {
     if (this.type == TokenType.NAME || this.type == TokenType.NUMBER) {
       return "<" + this.type + ", " + this.value + ">";
     } else {
        return "<" + this.type + ">"; 
     }
   }
}
