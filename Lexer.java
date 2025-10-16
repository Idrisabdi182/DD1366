/**
 * En klass för att göra lexikal analys, konvertera indataströmmen
 * till en sekvens av tokens.  Den här klassen läser in hela
 * indatasträngen och konverterar den på en gång i konstruktorn.  Man
 * kan tänka sig en variant som läser indataströmmen allt eftersom
 * tokens efterfrågas av parsern, men det blir lite mer komplicerat.
 *
 * Författare: idris Mahmud Abdi
 */
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
    private String input;
    private List<Token> tokens;
    private int currentToken;

    // Hjälpmetod som läser in innehållet i en inputstream till en
    // sträng
    private static String readInput(InputStream f) throws java.io.IOException {
        Reader stdin = new InputStreamReader(f);
        StringBuilder buf = new StringBuilder();
        char input[] = new char[1024];
        int read = 0;
        while ((read = stdin.read(input)) != -1) {
            buf.append(input, 0, read);
        }
        return buf.toString();
    }

    public Lexer(InputStream in) throws java.io.IOException {
        String input = Lexer.readInput(in);

        Pattern tokenPattern = Pattern.compile("FORW|BACK|LEFT|RIGHT|DOWN|UP|COLOR|REP|.|[0-9]+|#[0-9A-F]{6}|\"[^\"]*\"");
        Mathcer m = tokenPattern.matcher(input);
        int inputPos = 0;
        tokens = new ArrayList<Token>();
        currentToken = 0;

        while(m.find()) {

            if (m.start() != inputPos) {
                tokens.add(new Token(TokenType.ERROR));
            }

            if (m.group().equals("FORW")) {
                tokens.add(new Token(TokenType.FORW));
            } else if (m.group().equals("BACK")) {
                tokens.add(new Token(TokenType.BACK))
            } else if (m.group().equals("LEFT")) {
                tokens.add(new Token(TokenType.LEFT))
            } else if (m.group().equals("RIGHT")) {
                tokens.add(new Token(TokenType.RIGHT))
            } else if (m.group().equals("DOWN")) {
                tokens.add(new Token(TokenType.DOWN))
            } else if (m.group().equals("UP")) {
                tokens.add(new Token(TokenType.UP))
            } else if (m.group().equals("COLOR")) {
                tokens.add(new Token(TokenType.COLOR))
            } else if (m.group().equals("REP")) {
                tokens.add(new Token(TokenType.REP))
            } else if (m.group().equals("\.")) {
                tokens.add(new Token(TokenType.PERIOD))
            } else if (m.group().equals("#[0-9A-F]{6}")) {
                tokens.add(new Token(TokenType.HEX))
            } else if (m.group().equals("\"[^\"]*\"")) {
                tokens.add(new Token(TokenType.QUOTE))
            } else if (Character.isDigit(m.group().charAt(0))) {
                tokens.add(new Token(TokenType.DECIMAL. Integer.parseInt(m.group())));
            }
            inputPos = m.end();

            if (inputPos != input.length()) {
                tokens.add(new Token(TokenType.Error));
            }
        }
    }


	// Kika på nästa token i indata, utan att gå vidare
	public Token peekToken() throws SyntaxError {
		// Slut på indataströmmen
		if (!hasMoreTokens()) 
			throw new SyntaxError();
		return tokens.get(currentToken);
	}

	// Hämta nästa token i indata och gå framåt i indata
	public Token nextToken() throws SyntaxError {
		Token res = peekToken();
		++currentToken;
		return res;
	}

	public boolean hasMoreTokens() {
		return currentToken < tokens.size();
	}

}
