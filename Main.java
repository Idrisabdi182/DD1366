/**
 * Exempel på rekursiv medåkning.  Se README för mer info.  
 *
 * Författare: Idris Mahmud Abdi
 */
public class Main {
    public static void main(String args[]) throws java.io.IOException, SyntaxError {
        boolean verbose = args.length > 0 && args[0].equals("-v");
        Lexer lexer = new Lexer(System.in);
        Parser parser = new Parser(lexer, verbose);
        ParseTree result = parser.parse();
        // Parsning klar, gör vad vi nu vill göra med syntaxträdet
        System.out.println(result.evaluate());
    }
}
