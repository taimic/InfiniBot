package infinity.states;

import infinity.InfiBot;
import infinity.InfiBot.EVENTS;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class BackState extends State{
	
	double maxVelocity = 5;
	double moveDistance = 100;
	double moveDirection = 1;
	
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
		// divorce radar movement from gun movement
		robot.setAdjustRadarForGunTurn(true);
		// divorce gun movement from tank movement
		robot.setAdjustGunForRobotTurn(true);
		// we have no enemy yet
		robot.enemy.reset();
		// initial scan
		robot.setTurnRadarRight(360);
	}
	
	@Override
	public void exit() {

	}
	
	/**
	 * The default actions to execute when no event occurred happen in here. 
	 */
	@Override
	public void run() {
		System.out.println("back radar " + robot.getRadarTurnRemaining());
		robot.setTurnRadarRight(360);
		// Limit speed to preserve energy
		robot.setMaxVelocity(maxVelocity);
		// Move forward
		robot.ahead(moveDistance * moveDirection);
		
		robot.doGun();
		
		robot.execute();
		
		if(robot.getDistanceRemaining() <= 0) getStateMachine().enterLastState();
		

	}

	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// track if we have no enemy, the one we found is significantly
		// closer, or we scanned the one we've been tracking.
		if (robot.enemy.none() || e.getDistance() < robot.enemy.getDistance() - 70
				|| e.getName().equals(robot.enemy.getName())) {

			// track him using the NEW update method
			robot.enemy.update(e, this.robot);
		}
	}
	
	
	public void onRobotDeath(RobotDeathEvent e) {
		robot.onRobotDeath(e);
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
