public class Planet{

    public double xxPos; // Its current x position
    public double yyPos; // Its current y position
    public double xxVel; // Its current velocity in x direction
    public double yyVel; // Its current velocity in y direction
    public double mass; // Its mass
    public String imgFileName; // The name of the file that corresponds to the image that depicts the planet
    public static final double G = 6.67e-11;

    /* The first constructor */
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    /* The second constructor */
    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    /* Calculate the distance between two Planets */
    public double calcDistance(Planet p) {
        double distance;
        double xxPosDif = xxPos - p.xxPos;
        double yyPosDif = yyPos - p.yyPos;

        distance = Math.sqrt(xxPosDif * xxPosDif + yyPosDif * yyPosDif);
        return distance;
    }

    /* Calculate the force exerted on the planet by the given planet */
    public double calcForceExertedBy(Planet p) {
        double force;
        force = (G * mass * p.mass) / Math.pow(calcDistance(p), 2);
        return force; 
    }

    /* Calculate the force exerted in X */
    public double calcForceExertedByX(Planet p) {
        double difX, forceX;
        difX = p.xxPos - xxPos;
        forceX = (calcForceExertedBy(p) * difX) / calcDistance(p);
        return forceX; 
    }

    /* Calculate the force exerted in Y */
    public double calcForceExertedByY(Planet p) {
        double difY, forceY;
        difY = p.yyPos - yyPos;
        forceY = (calcForceExertedBy(p) * difY) / calcDistance(p);
        return forceY;
    }

    /* Calculate the net force exerted in X */
    public double calcNetForceExertedByX(Planet[] allPlanets) {
        double netForceX = 0;
        int length = allPlanets.length;
        for (int i = 0; i < length; i++) {
            if (this.equals(allPlanets[i])) {
                continue;
            }
            netForceX = netForceX + calcForceExertedByX(allPlanets[i]);
        }
        return netForceX;
    }

    /* Calculate the net force exerted in Y */
    public double calcNetForceExertedByY(Planet[] allPlanets) {
        double netForceY = 0;
        int length = allPlanets.length;
        for (int i = 0; i < length; i++) {
            if (this.equals(allPlanets[i])) {
                continue;
            }
            netForceY = netForceY + calcForceExertedByY(allPlanets[i]);
        }
        return netForceY;
    }

    /* Update the velocity and position */
    public void update(double dt, double fX, double fY) {
        /* Calculate the acceleration in X and Y */
        double acceX, acceY;
        acceX = fX / mass;
        acceY = fY / mass;

        /* Calculate the new velocity */
        xxVel = xxVel + dt * acceX;
        yyVel = yyVel + dt * acceY;

        /* Calculate the new position */
        xxPos = xxPos + dt * xxVel;
        yyPos = yyPos + dt * yyVel;
    }

    /* Draw a planet */
    public void draw() {
        StdDraw.picture(xxPos, yyPos, imgFileName);
    }

}