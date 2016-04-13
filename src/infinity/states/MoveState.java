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
		robot.turnGunLeft(robot.getGunHeading());
		robot.turnRadarLeft(robot.getRadarHeading());
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

		if (robot.getGunHeading() != 0) {
			//robot.turnGunLeft(-robot.getGunHeading());
		}else{
			robot.setAdjustGunForRobotTurn(false);
		}
		if (robot.getRadarHeading() != 0) {
			
			//robot.turnRadarLeft(-robot.getRadarHeading());
		}else{
			robot.setAdjustRadarForGunTurn(false);
			
		}
		
		
		// Limit speed to preserve energy
		robot.setMaxVelocity(maxVelocity);
		// Move forward
		robot.ahead(moveDistance * moveDirection);
		resetChangedTurn();
	}
	
	private void resetChangedTurn() {
		if(System.currentTimeMillis() - lastDirectionChange >100){
			changedDirection = false;
		}
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
			System.out.println("TOO CLOSE TO WALL");
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
