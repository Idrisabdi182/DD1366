import java.util.ArrayList;

public class Parser {
    private Lexer lexer;
    private boolean verbose;

    public Parser(Lexer lexer, boolean verbose) {
        this.lexer = lexer;
        this.verbose = verbose;
    }

    public boolean expect(TokenType t) throws SyntaxError {
        Token next = lexer.peekToken();

        if (next.getType() != t) {
            throw new SyntaxError(next.getRow());
        }

        lexer.nextToken();
        return true;
    }

    public ParseTree parse() throws SyntaxError {
        ParseTree result = Program();

        Token next = lexer.peekToken();
        if (next.getType() != TokenType.EOF) {
            throw new SyntaxError(next.getRow());
        }

        return result;
    }

    private ParseTree Program() throws SyntaxError {
        return CommandList();
    }

    private ParseTree CommandList() throws SyntaxError {
        ArrayList<ParseTree> commands = new ArrayList<>();

        while (true) {
            Token t = lexer.peekToken();

            if (
                t.getType() != TokenType.FORW &&
                t.getType() != TokenType.BACK &&
                t.getType() != TokenType.LEFT &&
                t.getType() != TokenType.RIGHT &&
                t.getType() != TokenType.UP &&
                t.getType() != TokenType.DOWN &&
                t.getType() != TokenType.REP &&
                t.getType() != TokenType.COLOR
            ) {
                break;
            }

            commands.add(Command());
        }

        return new ParseTree("CommandList", commands);
    }

    private ParseTree Command() throws SyntaxError {
        Token t = lexer.peekToken();
        int commandRow = t.getRow();

        ParseTree child;

        if (t.getType() == TokenType.FORW || t.getType() == TokenType.BACK) {
            child = Move();
            try {
                expect(TokenType.PERIOD);
            } catch (SyntaxError e) {
                throw new SyntaxError(commandRow);
            }

        } else if (t.getType() == TokenType.LEFT || t.getType() == TokenType.RIGHT) {
            child = Turn();
            try {
                expect(TokenType.PERIOD);
            } catch (SyntaxError e) {
                throw new SyntaxError(commandRow);
            }

        } else if (t.getType() == TokenType.UP || t.getType() == TokenType.DOWN) {
            child = Pen();
            try {
                expect(TokenType.PERIOD);
            } catch (SyntaxError e) {
                throw new SyntaxError(commandRow);
            }

        } else if (t.getType() == TokenType.COLOR) {
            child = Color();
            try {
                expect(TokenType.PERIOD);
            } catch (SyntaxError e) {
                throw new SyntaxError(commandRow);
            }

        } else if (t.getType() == TokenType.REP) {
            child = Rep();

        } else {
            throw new SyntaxError(t.getRow());
        }

        ArrayList<ParseTree> children = new ArrayList<>();
        children.add(child);
        return new ParseTree("Command", children);
    }

    private ParseTree Move() throws SyntaxError {
        ArrayList<ParseTree> children = new ArrayList<>();

        Token t = lexer.nextToken();
        if (t.getType() == TokenType.FORW) {
            children.add(new ParseTree("FORW"));
        } else {
            children.add(new ParseTree("BACK"));
        }

        Token num = lexer.peekToken();
        expect(TokenType.DECIMAL);
        children.add(new ParseTree(num.getData().toString()));

        return new ParseTree("Move", children);
    }

    private ParseTree Turn() throws SyntaxError {
        ArrayList<ParseTree> children = new ArrayList<>();

        Token t = lexer.nextToken();
        if (t.getType() == TokenType.LEFT) {
            children.add(new ParseTree("LEFT"));
        } else {
            children.add(new ParseTree("RIGHT"));
        }

        Token num = lexer.peekToken();
        expect(TokenType.DECIMAL);
        children.add(new ParseTree(num.getData().toString()));

        return new ParseTree("Turn", children);
    }

    private ParseTree Pen() throws SyntaxError {
        ArrayList<ParseTree> children = new ArrayList<>();

        Token t = lexer.nextToken();
        if (t.getType() == TokenType.UP) {
            children.add(new ParseTree("UP"));
        } else {
            children.add(new ParseTree("DOWN"));
        }

        return new ParseTree("Pen", children);
    }

    private ParseTree Color() throws SyntaxError {
        ArrayList<ParseTree> children = new ArrayList<>();

        lexer.nextToken(); // consume COLOR

        Token hex = lexer.peekToken();
        expect(TokenType.HEX);

        children.add(new ParseTree(hex.getData().toString()));

        return new ParseTree("Color", children);
    }

    private ParseTree Rep() throws SyntaxError {
        ArrayList<ParseTree> children = new ArrayList<>();

        Token repToken = lexer.peekToken();
        expect(TokenType.REP);

        Token num = lexer.peekToken();
        expect(TokenType.DECIMAL);
        children.add(new ParseTree(num.getData().toString()));

        ParseTree instructions = InstructionsRep(repToken.getRow());
        children.add(instructions);

        return new ParseTree("Rep", children);
    }

    private ParseTree InstructionsRep(int repRow) throws SyntaxError {
        Token t = lexer.peekToken();
    
        if (t.getType() == TokenType.QUOTE) {
            Token quoteStart = lexer.peekToken();
            expect(TokenType.QUOTE);
    
            ParseTree list = CommandList();
    
            Token next = lexer.peekToken();
            if (next.getType() != TokenType.QUOTE) {
                throw new SyntaxError(quoteStart.getRow());
            }
    
            expect(TokenType.QUOTE);
            return list;
    
        } else {
            ParseTree single = Command();
    
            Token next = lexer.peekToken();
            if (
                next.getType() == TokenType.FORW ||
                next.getType() == TokenType.BACK ||
                next.getType() == TokenType.LEFT ||
                next.getType() == TokenType.RIGHT ||
                next.getType() == TokenType.UP ||
                next.getType() == TokenType.DOWN ||
                next.getType() == TokenType.REP ||
                next.getType() == TokenType.COLOR
            ) {
                throw new SyntaxError(repRow); 
            }
    
            return single;
        }
    }
}
