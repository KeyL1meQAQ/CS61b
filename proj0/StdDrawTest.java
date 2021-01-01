public class StdDrawTest {

    public static String picture = "images/starfield.jpg";

    public static void drawBackground() {
        double radius = 2.5e11;

        StdDraw.setScale(radius * (-1), radius);
        StdDraw.clear();
        
        StdDraw.picture(0, 0, picture);

        StdDraw.show();
    }

    public static void main(String[] args) {
        drawBackground();
    }
}