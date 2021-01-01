public class NBody {

    /* Read the radius of the universe */
    public static double readRadius(String fileName) {
        if (fileName == null) {
            return 0;
        }

        In in = new In(fileName);
        int planetNumber = in.readInt();
        double radius = in.readDouble();

        return radius;
    }

    /* Read all the data of planets in the file */
    public static Planet[] readPlanets(String fileName) {
        if (fileName == null) {
            return null;
        }

        In in = new In(fileName);
        int planetNumber = in.readInt();

        Planet[] allPlanets = new Planet[planetNumber];
        double radius = in.readDouble();

        for (int i = 0; i < planetNumber; i++) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            Planet p = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
            allPlanets[i] = p;
        }

        return allPlanets;
    }

    public static void main(String[] args) {
        
        /* Read the radius and the planets data */
        double T, dt;
        String fileName;
        T = Double.parseDouble(args[0]);
        dt = Double.parseDouble(args[1]);
        fileName = args[2];

        double radius = readRadius(fileName);
        Planet[] allPlanets = readPlanets(fileName);   

        /* Draw the background */
        String picture = "images/starfield.jpg";
        StdDraw.setScale(radius * (-1), radius);
        StdDraw.clear();
        
        StdDraw.picture(0, 0, picture);

        /* Draw all the planets */
        int length = allPlanets.length;

        for (int i = 0; i < length; i++) {
            allPlanets[i].draw();
        }

        /* Create animation */
        double time;
        double[] xForces = new double[length];
        double[] yForces = new double[length];
        StdDraw.enableDoubleBuffering();

        for (time = 0; time < T; time = time + dt) {
            /* Calculate the net forces for each planet */
            for (int i = 0; i < length; i++) {
                xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
                yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
            }

            /* Update the position and velocity of each planet */
            for (int i = 0; i < length; i++) {
                allPlanets[i].update(dt, xForces[i], yForces[i]);
            }

            /* Draw the background and planets */
            StdDraw.clear();
            StdDraw.picture(0, 0, picture);
            for (int i = 0; i < length; i++) {
                allPlanets[i].draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
        }

        /* Print the universe */
        StdOut.printf("%d\n", allPlanets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  allPlanets[i].xxPos, allPlanets[i].yyPos, allPlanets[i].xxVel,
                  allPlanets[i].yyVel, allPlanets[i].mass, allPlanets[i].imgFileName);
        } 
    }
}