package infinity.states;

import infinity.InfiBot;
import infinity.InfiBot.EVENTS;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class BackState extends MoveState{
	/**
	 * Constructor.
	 * 
	 * @param robot The robot that is used.
	 */
	public BackState(InfiBot robot) {
		super(robot);
	}
	
	@Override
	public void enter() {
//	  	// divorce radar movement from gun movement
//		robot.setAdjustRadarForGunTurn(true);
//		// divorce gun movement from tank movement
//		robot.setAdjustGunForRobotTurn(true);
//		// we have no enemy yet
//		robot.enemy.reset();
//		// initial scan
//		robot.setTurnRadarRight(360);
	}
	
	@Override
	public void exit() {
//		robot.setTurnRadarRight(360 - robot.getRadarHeading());
//		robot.setAdjustRadarForGunTurn(false);
//		robot.setAdjustGunForRobotTurn(false);
//		robot.enemy.reset();
	}
	
	/**
	 * The default actions to execute when no event occurred happen in here. 
	 */
	@Override
	public void run() {
		// Limit speed to preserve energy
		robot.setMaxVelocity(maxVelocity);
		// Move forward
		robot.ahead(moveDistance * moveDirection);
		
		
		if(robot.getDistanceRemaining() <= 0) getStateMachine().enterLastState();
		
		robot.execute();
	}

	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {}

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
		goBack(e.getBearing());
	}

	/**
	 * We hit the wall with our robot.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onHitWall(HitWallEvent e) {
		goBack(e.getBearing());
	}
	
	public void goBack(double bearing){
		robot.setTurnRight(45);
		// Check whether or not the wall is in front of us (180 / 2 = 90 degrees)
		if(robot.isInRange(bearing, 90)) robot.back(moveDistance);
		else robot.ahead(moveDistance);
	}

	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onCustomEvent(CustomEvent e) {}
}
