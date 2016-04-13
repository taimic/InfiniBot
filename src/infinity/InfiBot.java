package infinity;

import infinity.states.BackState;
import infinity.states.CircState;
import infinity.states.MoveState;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class InfiBot extends AdvancedRobot {	
	/**
	 * Holds all the custom events. 
	 */
	public enum EVENTS {
		CUSTOM_NEAR_WALLS,
	    CUSTOM_NOT_NEAR_WALLS,
	    CUSTOM_TURN_FINISHED,
	    CUSTOM_CHANGE_STATE
	}
	
	/**
	 * The state machine. 
	 */
	private StateMachine stateMachine;
	
	/**
	 * The distance to the walls for firing an event, if we are too close.
	 */
	private double marginToWalls = 50;
	private double turnOverDegrees = 360;
	private double turnDirection = 1;
	private double lastStateChange = 0;
	private double changeStateTime = 200;	// in ms
	/**
	 * The enemy robot, which is used for tracking enemies. 
	 */
	private AdvancedEnemyBot enemy = new AdvancedEnemyBot();

	/**
	 * run:  Fire's main run function
	 */
	public void run() {
		// Initialize
		initialize();
		initCustomEvents();
		
		// Initialize state machine and register all states
		stateMachine = new StateMachine();
		stateMachine.register(new MoveState(this), new BackState(this), new CircState(this));
		
		// Set default state
		stateMachine.changeState(CircState.class);

		// Execute the states method
		while (true) {
			if(getTime() - lastStateChange > 200){
				lastStateChange = getTime();
				
				if (stateMachine.getCurrentState() instanceof MoveState)
					stateMachine.changeState(CircState.class);
				else
					stateMachine.changeState(MoveState.class);
			}
			
			stateMachine.getCurrentState().run();
		}
		
	}
	
	/**
	 * Initializes the robot. 
	 */
	private void initialize(){
		setBodyColor(Color.black);
		setGunColor(Color.red);
		setRadarColor(Color.gray);
		setScanColor(Color.black);
		setBulletColor(Color.red);
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
	 * Initializes custom events.
	 */
	private void initCustomEvents(){
		// Near walls
		addCustomEvent(new Condition(EVENTS.CUSTOM_NEAR_WALLS.toString()) {
			public boolean test() {
				return isNearWalls(marginToWalls);
			}
		});
		
		// Not near walls
		addCustomEvent(new Condition(EVENTS.CUSTOM_NOT_NEAR_WALLS.toString()) {
			public boolean test() {
				return !isNearWalls(marginToWalls);
			}
		});
		
		// Turn of the robot finished
		addCustomEvent(new Condition(EVENTS.CUSTOM_TURN_FINISHED.toString()) {
			public boolean test() {
				return isTurnFinished(turnDirection == -1);
			}
		});
		
		// Turn of the robot finished
		addCustomEvent(new Condition(EVENTS.CUSTOM_CHANGE_STATE.toString()) {
			public boolean test() {
				return getTime() - lastStateChange > changeStateTime;
			}
		});
	}
	
	/**
	 * @return The enemy robot. 
	 */
	public AdvancedEnemyBot getEnemy(){
		return enemy;
	}
	
	/**
	 * @param left Whether or not we turn left
	 * 
	 *  @return Whether or not the turn has finished, depending on the direction we are turning.
	 */
	public boolean isTurnFinished(boolean left) {
		double remaining = getTurnRemaining();
		return ((remaining <= 0 && !left) || (remaining >= 0 && left));
	}
	
	/**
	 * @return Whether or not the robot is near walls.
	 */
	public boolean isNearWalls(double marginToWalls){
		return (
			// we're too close to the left wall
			getX() <= marginToWalls ||
			// or we're too close to the right wall
			getX() >= getBattleFieldWidth() - marginToWalls ||
			// or we're too close to the bottom wall
			getY() <= marginToWalls ||
			// or we're too close to the top wall
			getY() >= getBattleFieldHeight() - marginToWalls
		);
	}
	
	public double getTurnDirection(){
		return turnDirection;
	}
	
	public double getTurnOverDegrees(){
		return turnOverDegrees;
	}
	
	public void inverseTurnDirection(){
		turnDirection *= -1;
	}
	
	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onCustomEvent(CustomEvent e) {
		if(stateMachine != null){
//			if(e.getCondition().getName().equals(EVENTS.CUSTOM_CHANGE_STATE.toString())){
//				lastStateChange = getTime();
//				
//				if (stateMachine.getCurrentState() instanceof MoveState) stateMachine.changeState(CircState.class);
//				else if (stateMachine.getCurrentState() instanceof CircState) stateMachine.changeState(MoveState.class);
//			}else 
				stateMachine.getCurrentState().onCustomEvent(e);
		}
	}

	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		if(stateMachine != null) stateMachine.getCurrentState().onScannedRobot(e);
	}
	
	/**
	 * A robot died.
	 * 
	 * @param e The event holding the corresponding data  
	 */
	public void onRobotDeath(RobotDeathEvent e) {
		if (e.getName().equals(enemy.getName())) enemy.reset();
	}   

	/**
	 * We were hit by a bullet.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		if(stateMachine != null) stateMachine.getCurrentState().onHitByBullet(e);
	}

	/**
	 * We hit a robot directly with our robot (collision).
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onHitRobot(HitRobotEvent e) {
		if(stateMachine != null) stateMachine.getCurrentState().onHitRobot(e);
	}
	
	/**
	 * We hit the wall with our robot.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onHitWall(HitWallEvent e){
		if(stateMachine != null) stateMachine.getCurrentState().onHitWall(e);
	}
	
	
	public void doGun() {
		// don't shoot if I've got no enemy
		if (enemy.none())
			return;

		// calculate firepower based on distance
		double firePower = Math.min(500 / enemy.getDistance(), 3);
		// calculate speed of bullet
		double bulletSpeed = 20 - firePower * 3;
		// distance = rate * time, solved for time
		long time = (long)(enemy.getDistance() / bulletSpeed);
		
////		time -= 3.0f; // compensation test
//		System.out.println("Time" + time);
		
		// calculate gun turn to predicted x,y location
		double futureX = enemy.getFutureX(time);
		double futureY = enemy.getFutureY(time);
		double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
		// non-predictive firing can be done like this:
		//double absDeg = absoluteBearing(getX(), getY(), enemy.getX(), enemy.getY());

		// turn the gun to the predicted x,y location
		setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));

		// if the gun is cool and we're pointed in the right direction, shoot!
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
			setFire(firePower);
		}
	}

	/**
	 * Computes the absolute bearing between two points.
	 * 
	 * @param x1 The x-coordinate of the first point
	 * @param y1 The y-coordinate of the first point
	 * @param x2 The x-coordinate of the second point
	 * @param y2 The y-coordinate of the second point
	 * @return The absolute bearing.
	 */
	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
		double hyp = Point2D.distance(x1, y1, x2, y2);
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actually 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}

		return bearing;
	}

	/**
	 * Normalizes a bearing to between +180 and -180. Works with angles of any size.
	 * 
	 * @param The angle to normalize
	 * @return The normalized angle.
	 */
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
}