package infinity.states;

import infinity.StateMachine;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

public abstract class State implements IState{
	/**
	 * The robot that is in use. 
	 */
	protected AdvancedRobot robot;
	/**
	 * The state machine that is used. 
	 */
	protected StateMachine stateMachine;
	
	/**
	 * Constructor. 
	 */
	public State(AdvancedRobot robot) {
		this.robot = robot;
	}
	
	/**
	 * The default actions to execute when no event occurred happen in here. 
	 */
	@Override
	public void run() {}

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
	public void onHitRobot(HitRobotEvent e) {}
	
	/**
	 * @return The registered state machine. 
	 */
	public StateMachine getStateMachine(){
		return stateMachine;
	}
	
	/**
	 * Registers the state machine.
	 * 
	 * @param stateMachine The state machine to register
	 */
	public void registerStateMachine(StateMachine stateMachine){
		this.stateMachine = stateMachine;
	}
}
