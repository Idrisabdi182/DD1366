import java.util.Locale;

public class Leona {
    String c;
    double x1;
    double y1;
    double x2;
    double y2;

    // Create a new Leona object
    public Leona(String c, double x1, double y1, double x2, double y2) {
        this.c = c;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

   

    public void setNewX2Y2(int d, double v) {
        double currentx = getCurrentX();
        double currenty = getCurrentY();
        x2 = currentx + (double) d * Math.cos(Math.PI * v / 180);
        y2 = currenty + (double) d * Math.sin(Math.PI * v / 180);
    }

    public void setNewX1Y1() {
        x1 = x2;
        y1 = y2;
    }

    public double getCurrentX() {
        return x1;
    }

    public double getCurrentY() {
        return y1;
    }

    public void printLeona() {
        System.out.printf("%s %.4f %.4f %.4f %.4f%n", c, x1, y1, x2, y2);
    }

    public static void main(String[] args) {
        Leona leona = new Leona("#0000FF", 0.0, 0.0, 0.0, 0.0);
        System.out.printf(Locale.US, "%s %.4f %.4f %.4f %.4f%n", leona.c, leona.x1, leona.y1, leona.x2, leona.y2);

    }
} 
