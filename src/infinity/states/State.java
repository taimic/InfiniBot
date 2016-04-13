package infinity.states;

import infinity.InfiBot;
import infinity.StateMachine;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

public abstract class State implements IState{
	/**
	 * The robot that is in use. 
	 */
	protected InfiBot robot;
	/**
	 * The state machine that is used. 
	 */
	protected StateMachine stateMachine;
	
	/**
	 * Constructor. 
	 */
	public State(InfiBot robot) {
		this.robot = robot;
	}
	
	/**
	 * Enters the state. 
	 */
	@Override
	public void enter(){
		System.out.println("Entered state: " + this.getClass().getSimpleName());
	}
	
	/**
	 * Exits the state. 
	 */
	@Override
	public void exit(){
		System.out.println("Exited state: " + this.getClass().getSimpleName());
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
