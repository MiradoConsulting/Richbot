import robocode.*;
import java.awt.Color;
import java.util.Random;

public class RichBot extends AdvancedRobot {
    int gunDirection = 1;
    Random random = new Random();

    public void run() {
        // Mere aesthetic changes
        setBodyColor(Color.black);
        setRadarColor(Color.green);
        setGunColor(Color.black);
        setBulletColor(Color.orange);

        // Infinite loop for robot movement
        while (true) {
            // Move randomly
            randomMove();
            // Turns the gun infinitely, looking for enemies
            turnGunRight(360);
        }
    }

    // Method for random movement
    private void randomMove() {
        // Randomly choose a movement direction and distance
        double moveDistance = random.nextDouble() * 200; // Move up to 200 pixels
        double turnAngle = random.nextDouble() * 90 - 45; // Turn between -45 and 45 degrees

        // Move and turn
        setAhead(moveDistance);
        setTurnRight(turnAngle);
        execute();
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Turn the robot towards the enemy
        setTurnRight(e.getBearing());
        // Shoots always that it's aiming at the enemy
        setFire(3);
        // And move forward
        setAhead(100);
        // Inverts the gun direction on each turn
        gunDirection = -gunDirection;
        // Turn 360 degrees (clockwise or anti clockwise,)
        setTurnGunRight(360 * gunDirection);
        // Execute all the pending actions
        execute();
    }
}
