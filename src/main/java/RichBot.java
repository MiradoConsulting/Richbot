import robocode.*;

public class RichBot extends Robot {

    boolean inDanger = false; // Flag to indicate if the robot is in danger
    int numAliveRobots = 0; // Counter to track the number of alive robots

    public void run() {
        while (true) {
            numAliveRobots = getOthers() + 1;
            if (inDanger) {
                // If in danger, move to a safe location
                moveToSafety();
            } else {
                // If not in danger, rotate the gun to scan for targets
                if (numAliveRobots == 1) {
                    moveToRemainingRobot();
                    fire(1);
                } else {
                    // Check for 75% chance to move
                    if (Math.random() <= 0.75) {
                        moveSlightly(); // Move slightly in a random direction
                    }
                    turnGunRight(30);
                }
            }
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (!inDanger) {
            // If not in danger, fire at the scanned robot
            fire(1);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        // When hit by a bullet, set the flag to indicate danger
        inDanger = true;
    }

    public void onHitWall(HitWallEvent e) {
        // When hitting a wall, set the flag to indicate danger
        inDanger = true;
    }

    private void moveToSafety() {
        // Move to a safe location away from the current position
        double safeDistance = 200; // Distance to move away from current position
        double angle = Math.random() * 360; // Random angle to move
        double newX = getX() + safeDistance * Math.cos(Math.toRadians(angle));
        double newY = getY() + safeDistance * Math.sin(Math.toRadians(angle));

        // Ensure the new position is within the battlefield boundaries
        newX = Math.max(Math.min(newX, getBattleFieldWidth() - 20), 20);
        newY = Math.max(Math.min(newY, getBattleFieldHeight() - 20), 20);

        // Move to the new position
        goTo(newX, newY);

        // Reset the danger flag as we're in a safe position now
        inDanger = false;
    }

    private void moveSlightly() {
        // Move slightly in a random direction
        double distance = 50; // Distance to move
        double angle = Math.random() * 360; // Random angle to move
        double newX = getX() + distance * Math.cos(Math.toRadians(angle));
        double newY = getY() + distance * Math.sin(Math.toRadians(angle));

        // Ensure the new position is within the battlefield boundaries
        newX = Math.max(Math.min(newX, getBattleFieldWidth() - 20), 20);
        newY = Math.max(Math.min(newY, getBattleFieldHeight() - 20), 20);

        // Move to the new position
        goTo(newX, newY);
    }

    private void goTo(double x, double y) {
        // Move the robot to the specified coordinates
        double dx = x - getX();
        double dy = y - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double angle = Math.toDegrees(Math.atan2(dx, dy)) - getHeading();

        // Normalize the angle to be between -180 and 180
        angle = normalizeAngle(angle);

        // Turn and move the robot
        turnRight(angle);
        ahead(distance);
    }

    private double normalizeAngle(double angle) {
        while (angle <= -180) {
            angle += 360;
        }
        while (angle > 180) {
            angle -= 360;
        }
        return angle;
    }

    private void moveToRemainingRobot() {
        // Move towards the remaining robot
        double targetX = getBattleFieldWidth() / 2; // Move towards the center
        double targetY = getBattleFieldHeight() / 2;

        goTo(targetX, targetY);
    }
}
