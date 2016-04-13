package infinity.states;

import infinity.InfiBot;
import infinity.InfiBot.EVENTS;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class CircState extends State {
	/**
	 * Constructor.
	 */
	public CircState(InfiBot robot) {
		super(robot);
	}
	
	@Override
	public void enter() {
		super.enter();
		// divorce radar movement from gun movement
		robot.setAdjustRadarForGunTurn(true);
		// divorce gun movement from tank movement
		robot.setAdjustGunForRobotTurn(true);
		// we have no enemy yet
		robot.getEnemy().reset();
		// initial scan
		robot.setTurnRadarRight(360);
	}

	/**
	 * The default actions to execute when no event occurred happen in here.
	 */
	@Override
	public void run() {
		// rotate the radar
		robot.setTurnRadarRight(360);
		// sit & spin
		robot.setTurnRight(5);
		robot.setAhead(20);
		// doGun does predictive targeting
		robot.doGun();
		// carry out all the queued up actions
		robot.execute();
	}

	/**
	 * An enemy robot was found by the scanner.
	 * 
	 * @param e
	 *            The event holding the corresponding data
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// track if we have no enemy, the one we found is significantly
		// closer, or we scanned the one we've been tracking.
		if (robot.getEnemy().none() || e.getDistance() < robot.getEnemy().getDistance() - 70
				|| e.getName().equals(robot.getEnemy().getName())) {

			// track him using the NEW update method
			robot.getEnemy().update(e, this.robot);
		}
	}

	/**
	 * We were hit by a bullet.
	 * 
	 * @param e
	 *            The event holding the corresponding data
	 */
	@Override
	public void onHitByBullet(HitByBulletEvent e) {
	}

	/**
	 * We hit a robot directly with our robot (collision).
	 * 
	 * @param e
	 *            The event holding the corresponding data
	 */
	@Override
	public void onHitRobot(HitRobotEvent e) {
		getStateMachine().changeState(BackState.class);
	}

	@Override
	public void onHitWall(HitWallEvent e) {
		getStateMachine().changeState(BackState.class);
	}

	/**
	 * This is called for every custom event that was registered.
	 * 
	 * @param e
	 *            The event holding the corresponding data
	 */
	@Override
	public void onCustomEvent(CustomEvent e) {
		if (e.getCondition().getName().equals(EVENTS.CUSTOM_NEAR_WALLS.toString())){
			System.out.println("TOO CLOSE TO WALL");
			getStateMachine().changeState(BackState.class);
		}
	}
}
