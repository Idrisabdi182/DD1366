enum TokenType {
    FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, REP, PERIOD, QUOTE, DECIMAL, HEX, ERROR, EOF
}

class Token {
    private TokenType type;
    private Object data;
    private int row;

    public Token(TokenType type, int row) {
        this.type = type;
        this.data = null;
        this.row = row;
    }

    public Token(TokenType type, Object data, int row) {
        this.type = type;
        this.data = data;
        this.row = row;
    }

    public TokenType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public int getRow() {
        return row;
    }

    public String toString() {
        if (data == null) {
            return type.toString();
        } else {
            return type + "(" + data + ")";
        }
    }
}
