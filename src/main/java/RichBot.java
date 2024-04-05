import robocode.*;

public class RichBot extends Robot {

    boolean movingForward = true; // Flag to track the movement direction
    boolean turretPointingForward = true; // Flag to track the turret direction

    public void run() {
        while (true) {
            moveAlongTopWall();
            alternateTurretDirection();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Check if there's a robot in the direction the turret is pointing
        if ((turretPointingForward && e.getBearing() >= 0) || (!turretPointingForward && e.getBearing() < 0)) {
            // If yes, fire at the scanned robot
            fire(1);
        }
    }

    private void moveAlongTopWall() {
        double targetX = getX(); // X coordinate remains the same
        double targetY;

        // Determine target Y coordinate based on movement direction
        if (movingForward) {
            // Move to the top wall
            targetY = getBattleFieldHeight() - 20;
        } else {
            // Move away from the top wall
            targetY = 100;
        }

        // Move towards the target position
        goTo(targetX, targetY);

        // Change direction when reaching the top wall or bottom limit
        if ((targetY == getBattleFieldHeight() - 20 && getY() == getBattleFieldHeight() - 20) ||
                (targetY == 100 && getY() == 100)) {
            movingForward = !movingForward;
        }
    }

    private void alternateTurretDirection() {
        // Alternate turret direction between forward and backward
        if (turretPointingForward) {
            turnGunRight(180); // Point the turret backward
        } else {
            turnGunLeft(180); // Point the turret forward
        }
        turretPointingForward = !turretPointingForward; // Update turret direction flag
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
}
