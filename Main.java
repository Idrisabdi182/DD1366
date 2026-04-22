public class Main {
    public static void main(String[] args) throws java.io.IOException {
        try {
            boolean verbose = args.length > 0 && args[0].equals("-v");
    
            Lexer lexer = new Lexer(System.in);
            Parser parser = new Parser(lexer, verbose);
    
            ParseTree result = parser.parse();
    
            Interpreter interpreter = new Interpreter(new Leona("#0000FF", 0.0, 0.0, 0.0, 0.0));
            interpreter.execute(result);
    
        } catch (SyntaxError e) {
            System.out.println(e.getMessage());
        }
    }
}
