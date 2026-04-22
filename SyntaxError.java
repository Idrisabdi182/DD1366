
/** Klass för att representera syntaxfel.  I praktiken vill man nog
 * även ha med ett litet felmeddelande om *vad* som var fel, samt på
 * vilken rad/position felet uppstod
 */
public class SyntaxError extends Exception {
    private int row;

    // Constructor
    public SyntaxError(int row) {
        super("Syntaxfel på rad " + row);
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}
