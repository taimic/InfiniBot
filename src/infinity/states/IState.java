package infinity.states;

import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public interface IState {
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
	
	void OnHitWall(HitWallEvent e);
	
	void onCustomEvent(CustomEvent e);
}
