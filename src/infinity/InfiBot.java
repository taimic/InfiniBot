package infinity;

import infinity.states.MoveState;
import infinity.states.ShootState;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

public class InfiBot extends AdvancedRobot {
	/**
	 * Holds all the custom events. 
	 */
	public enum EVENTS {
		CUSTOM_NEAR_WALLS,
	    CUSTOM_NOT_NEAR_WALLS
	}
	
	/**
	 * The state machine. 
	 */
	private StateMachine stateMachine;
	
	/**
	 * The distance to the walls for firing an event, if we are too close.
	 */
	private double marginToWalls = 100;

	/**
	 * run:  Fire's main run function
	 */
	public void run() {
		// Initialize
		initialize();
		initCustomEvents();
		
		// Initialize state machine and register all states
		stateMachine = new StateMachine();
		stateMachine.register(new MoveState(this), new ShootState(this));
		
		// Set default state
		stateMachine.changeState(MoveState.class);

		// Execute the states method
		while (true) stateMachine.getCurrentState().run();
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
	
	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e The event holding the corresponding data
	 */
	public void onCustomEvent(CustomEvent e) {
		if(stateMachine != null) stateMachine.getCurrentState().onCustomEvent(e);
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
}