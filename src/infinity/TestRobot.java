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

public class TestRobot extends AdvancedRobot {
	/**
	 * The state machine. 
	 */
	private StateMachine stateMachine;

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
		while (true) {
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
	
	private void initCustomEvents(){
		addCustomEvent(new Condition("too_close_to_walls") {
			public boolean test() {
				return (
					// we're too close to the left wall
					getX() <= 100 ||
					// or we're too close to the right wall
					getX() >= getBattleFieldWidth() - 100 ||
					// or we're too close to the bottom wall
					getY() <= 100 ||
					// or we're too close to the top wall
					getY() >= getBattleFieldHeight() - 100
				);
			}
		});
	}
	
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