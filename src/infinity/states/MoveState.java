package infinity.states;

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
		robot.ahead(moveDistance);
	}
	
	public void turnAround(){
		robot.setTurnLeft(turn * .5f);
	}

	private int tooCloseToWall = 0;
	public void onCustomEvent(CustomEvent e) {
		if (e.getCondition().getName().equals("too_close_to_walls"))
		{
			if (tooCloseToWall <= 0) {
				// if we weren't already dealing with the walls, we are now
				tooCloseToWall += 100;
			//	robot.setMaxVelocity(0); // stop!!!
				robot.setTurnRight(180);
				robot.ahead(100);
			}
		}
	}

	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		robot.fire(3);
		if(System.currentTimeMillis() - lastTime > 300){
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
	
	@Override
	public void OnHitWall(HitWallEvent e){
		System.out.println(e.getBearing());
	}
}
