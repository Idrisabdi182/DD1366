import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;

public class Lexer {
    private List<Token> tokens;
    private int currentToken;

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
        String input = readInput(in);

        tokens = new ArrayList<>();
        currentToken = 0;

        int i = 0;
        int row = 1;

        while (i < input.length()) {
            char c = input.charAt(i);


            if (Character.isWhitespace(c)) {
                if (c == '\n') row++;
                i++;
                continue;
            }

            if (c == '%') {
                while (i < input.length() && input.charAt(i) != '\n') {
                    i++;
                }
                // newline handled in next loop iteration
                continue;
            }

            if (c == '.') {
                tokens.add(new Token(TokenType.PERIOD, row));
                i++;
                continue;
            }


            if (c == '"') {
                tokens.add(new Token(TokenType.QUOTE, row));
                i++;
                continue;
            }

            if (c == '#') {
                if (i + 6 < input.length()) {
                    String hex = input.substring(i, i + 7);
                    if (hex.matches("#[0-9A-Fa-f]{6}")) {
                        tokens.add(new Token(TokenType.HEX, hex, row));
                        i += 7;
                        continue;
                    }
                }
                tokens.add(new Token(TokenType.ERROR, row));
                i++;
                continue;
            }

            if (Character.isDigit(c)) {
                int start = i;
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    i++;
                }

                String numStr = input.substring(start, i);

                if (i < input.length()) {
                    char next = input.charAt(i);
                    if (next == '"' || Character.isLetter(next)) {
                        tokens.add(new Token(TokenType.ERROR, row));
                        continue;
                    }
                }

                tokens.add(new Token(TokenType.DECIMAL, Integer.parseInt(numStr), row));
                continue;
            }

            if (Character.isLetter(c)) {
                int start = i;
                while (i < input.length() && Character.isLetter(input.charAt(i))) {
                    i++;
                }
            
                String word = input.substring(start, i).toLowerCase();
            
                boolean needsArg =
                    word.equals("forw") ||
                    word.equals("back") ||
                    word.equals("left") ||
                    word.equals("right") ||
                    word.equals("color") ||
                    word.equals("rep");
            
                if (needsArg) {
                    if (i < input.length() && !Character.isWhitespace(input.charAt(i))) {
                        tokens.add(new Token(TokenType.ERROR, row));
                        continue;
                    }
                }
            
                switch (word) {
                    case "forw":
                        tokens.add(new Token(TokenType.FORW, row));
                        break;
                    case "back":
                        tokens.add(new Token(TokenType.BACK, row));
                        break;
                    case "left":
                        tokens.add(new Token(TokenType.LEFT, row));
                        break;
                    case "right":
                        tokens.add(new Token(TokenType.RIGHT, row));
                        break;
                    case "up":
                        tokens.add(new Token(TokenType.UP, row));
                        break;
                    case "down":
                        tokens.add(new Token(TokenType.DOWN, row));
                        break;
                    case "color":
                        tokens.add(new Token(TokenType.COLOR, row));
                        break;
                    case "rep":
                        tokens.add(new Token(TokenType.REP, row));
                        break;
                    default:
                        tokens.add(new Token(TokenType.ERROR, row));
                }
            
                continue;
            }

            tokens.add(new Token(TokenType.ERROR, row));
            i++;
        }

        tokens.add(new Token(TokenType.EOF, row));
    }

    public Token peekToken() {
        return tokens.get(currentToken);
    }

    public Token nextToken() {
        return tokens.get(currentToken++);
    }

    public boolean hasMoreTokens() {
        return currentToken < tokens.size();
    }
}
