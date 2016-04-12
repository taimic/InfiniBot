package infinity.states;

import infinity.InfiBot.EVENTS;
import robocode.AdvancedRobot;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class MoveState extends State{	
	double turn = 360;
	double maxVelocity = 5;
	double moveDistance = 100;
	double lastTime = 0;
	double doTurn = 1;
	double moveDirection = 1;
	
	/**
	 * Constructor.
	 * 
	 * @param robot The robot that is used.
	 */
	public MoveState(AdvancedRobot robot) {
		super(robot);
	}
	
	/**
	 * The default actions to execute when no event occurred happen in here. 
	 */
	@Override
	public void run() {
		// Turn to the right over x degrees
		robot.setTurnRight(turn * doTurn);
		// Limit speed to preserve energy
		robot.setMaxVelocity(maxVelocity);
		// Move forward
		robot.ahead(moveDistance * moveDirection);
		
		if(robot.getDistanceRemaining() <= 0) doTurn = 1;
	}
	
	public void turnAround(){
		robot.setTurnLeft(turn * doTurn * .5f);
	}
	
	/**
	 * @param bearing The bearing of the enemy robot
	 * @param halfRadius The halfRadius to look in
	 * 
	 * @return Whether or not the robot with the given bearing is in range of the given radius ([-halfRadius..halfRadius]). 
	 */
	public boolean isInRange(double bearing, double halfRadius){
		return (bearing > -halfRadius && bearing < halfRadius);
	}

	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// Fire at robot with the fire power depending on the distance to the robot
		robot.fire(Math.min(((robot.getBattleFieldWidth() + robot.getBattleFieldHeight()) * .5f) / e.getDistance(), 3));
		
		if(System.currentTimeMillis() - lastTime > 300){
			// Reset last time
			lastTime = System.currentTimeMillis();
			// Switch turn direction
			if(e.getDistance() < ((robot.getBattleFieldWidth() + robot.getBattleFieldHeight()) * .25f)) turn *= -1;
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
		System.out.println("HIT ROBOT");
		// Check whether or not the robot is in front of us (180 / 2 = 90 degrees)
		doTurn = 0;
		robot.setTurnRight(turn * doTurn);
		if(isInRange(e.getBearing(), 90)) robot.back(moveDistance);
		else robot.ahead(moveDistance);
	}
	
	/**
	 * We hit the wall with our robot.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onHitWall(HitWallEvent e){
		System.out.println("HIT WALL");
		// Check whether or not the wall is in front of us (180 / 2 = 90 degrees)
		doTurn = 0;
		robot.setTurnRight(turn * doTurn);
		if(isInRange(e.getBearing(), 90)) robot.back(moveDistance);
		else robot.ahead(moveDistance);
	}
	
	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onCustomEvent(CustomEvent e) {
		if (e.getCondition().getName().equals(EVENTS.CUSTOM_NEAR_WALLS.toString())){
			
		}else if (e.getCondition().getName().equals(EVENTS.CUSTOM_NOT_NEAR_WALLS.toString())){
			
		}
	}
}
