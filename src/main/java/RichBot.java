import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * RichBot - a robot by (your name here)
 */
public class RichBot extends Robot
{
	boolean inDanger = false;
	
	public void run() {
		while (true) {
            if (inDanger) {
                // If in danger, move to a safe location
                moveToSafety();
            } else {
                // If not in danger, rotate the gun to scan for targets
                turnGunRight(360);
            }
        }
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
        if (!inDanger) {
            // If not in danger, fire at the scanned robot
            fire(1);
        }
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
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
