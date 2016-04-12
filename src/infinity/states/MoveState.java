package infinity.states;

import infinity.TestRobot.EVENTS;
import robocode.AdvancedRobot;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class MoveState extends State{	
	/**
	 * Constructor. 
	 */
	public MoveState(AdvancedRobot robot) {
		super(robot);
	}

	double turn = 360;
	double maxVelocity = 5;
	double moveDistance = 100;
	double lastTime = 0;
	double moveDirection = 1;
	
	/**
	 * The default actions to execute when no event occurred happen in here. 
	 */
	@Override
	public void run() {
		// Turn to the right over x degrees
		robot.setTurnRight(turn);
		// Limit speed to preserve energy
		robot.setMaxVelocity(maxVelocity);
		// Move forward
		robot.ahead(moveDistance * moveDirection);
	}
	
	public void turnAround(){
		robot.setTurnLeft(turn * .5f);
	}

	private boolean notHandlingCloseWalls = true;

	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		robot.fire(3);
		if(System.currentTimeMillis() - lastTime > 300 && notHandlingCloseWalls){
			lastTime = System.currentTimeMillis();
			// Switch turn direction
			turn *= -1;
			// Reset run directions
			robot.setTurnRight(turn);
		}
	}

	/**
	 * We were hit by a bullet.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onHitByBullet(HitByBulletEvent e) {}

	/**
	 * We hit a robot directly with our robot (collision).
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onHitRobot(HitRobotEvent e) {
		turnAround();
	}
	
	/**
	 * We hit the wall with our robot.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onHitWall(HitWallEvent e){
		System.out.println(e.getBearing());
	}
	
	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onCustomEvent(CustomEvent e) {
		if (e.getCondition().getName().equals(EVENTS.CUSTOM_NEAR_WALLS.toString())){
			if (notHandlingCloseWalls) {
				notHandlingCloseWalls = false;
				moveDirection *= -1;
				System.out.println("MOVING BACK");
			}
		}else if (e.getCondition().getName().equals(EVENTS.CUSTOM_NOT_NEAR_WALLS.toString())){
			moveDirection *= 1;
			notHandlingCloseWalls = true;
		}
	}
}
