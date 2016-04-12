package infinity.states;

import infinity.InfiBot;
import infinity.InfiBot.EVENTS;
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
	public MoveState(InfiBot robot) {
		super(robot);
	}
	
	@Override
	public void enter(){
		
	}
	
	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
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
		getStateMachine().changeState(BackState.class);
	}
	
	/**
	 * We hit the wall with our robot.
	 * 
	 * @param e The event holding the corresponding data
	 */
	@Override
	public void onHitWall(HitWallEvent e){
		getStateMachine().changeState(BackState.class);
	}
	
	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onCustomEvent(CustomEvent e) {
		if (e.getCondition().getName().equals(EVENTS.CUSTOM_NEAR_WALLS.toString())){
			System.out.println("TOO CLOSE TO WALL");
			getStateMachine().changeState(BackState.class);
		}
	}
}
