package infinity.states;

import infinity.InfiBot;
import infinity.InfiBot.EVENTS;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class MoveState extends State{
	double maxVelocity = 5;
	double moveDistance = 100;
	double moveDirection = 1;
	
	boolean changedDirection = false;

	private double lastDirectionChange = 0;
	
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
		super.enter();
		
		// Align gun with radar
		robot.turnGunLeft(robot.getGunHeading());
		robot.turnRadarLeft(robot.getRadarHeading());
	}
	
	/**
	 * The default actions to execute when no event occurred happen in here. 
	 */
	@Override
	public void run() {
		// Lock radar and gun
		if (robot.getRadarHeading() == 0)
			robot.setAdjustRadarForGunTurn(false);
			
		if(robot.getGunHeading() == 0) 
			robot.setAdjustGunForRobotTurn(false);
		
		// Limit speed to preserve energy
		robot.setMaxVelocity(maxVelocity);
		// Move forward
		robot.ahead(moveDistance * moveDirection);
		// Reset flag
		resetChangedTurn();
	}
	
	/**
	 * Resets the flag that is used to prevent multiple event calls. 
	 */
	private void resetChangedTurn() {
		changedDirection = !(System.currentTimeMillis() - lastDirectionChange > 100);
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
			getStateMachine().changeState(BackState.class);
		}

		if (e.getCondition().getName().equals(EVENTS.CUSTOM_TURN_FINISHED.toString())){
			if (!changedDirection){ 
				lastDirectionChange = System.currentTimeMillis();
				changedDirection = true;
				robot.inverseTurnDirection();
				robot.setTurnRight(robot.getTurnOverDegrees() * robot.getTurnDirection());
			}
		}
	}
}
