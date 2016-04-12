package infinity.states;

import infinity.StateMachine;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public interface IState {
	/**
	 * Registers the state machine for further access through the states.
	 * 
	 *  @param stateMachine The state machine to register
	 */
	void registerStateMachine(StateMachine stateMachine);
	
	/**
	 * @return The registered state machine. 
	 */
	StateMachine getStateMachine();
	
	/**
	 * Enters the state. 
	 */
	void enter();
	
	/**
	 * Exits the state. 
	 */
	void exit();
	
	/**
	 * The default actions to execute when no event occurred happen in here. 
	 */
	void run();
	
	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e The event holding the corresponding data
	 */
	void onScannedRobot(ScannedRobotEvent e);
	
	/**
	 * We were hit by a bullet.
	 * 
	 * @param e The event holding the corresponding data
	 */
	void onHitByBullet(HitByBulletEvent e);
	
	/**
	 * We hit a robot directly with our robot (collision).
	 * 
	 * @param e The event holding the corresponding data
	 */
	void onHitRobot(HitRobotEvent e);
	
	/**
	 * We hit the wall with our robot.
	 * 
	 * @param e The event holding the corresponding data
	 */
	void onHitWall(HitWallEvent e);
	
	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e The event holding the corresponding data
	 */
	void onCustomEvent(CustomEvent e);
}
