package infinity;

import infinity.states.IState;

import java.util.ArrayList;

public class StateMachine {
	/**
	 * All registered states. 
	 */
	ArrayList<IState> states;
	/**
	 * The current active state. 
	 */
	IState currentState;
	/**
	 * The last active state. 
	 */
	IState lastState;
	
	/**
	 * Constructor. 
	 */
	public StateMachine() {
		states = new ArrayList<>();
	}
	
	/**
	 * Registers the given state(s).
	 * 
	 * @param states All states to register
	 */
	public void register(IState... states){
		for(IState state : states){
			if(!this.states.contains(state)){
				this.states.add(state);
				state.registerStateMachine(this);
			}
		}
	}
	
	/**
	 * Unregisters the given state(s).
	 * 
	 * @param states All states to unregister
	 */
	public void unregister(IState... states){
		for(IState state : states){
			if(this.states.contains(state)) this.states.remove(state);
		}		
	}
	
	/**
	 * Changes the current active state to the state with the given class, if available.
	 * 
	 * @param to The class of the registered state to change to
	 */
	public void changeState(Class<? extends IState> to){
		for(IState state : this.states){
			if(state.getClass().equals(to)){
				if(currentState != null) currentState.exit();
				lastState = currentState;
				currentState = state;
				currentState.enter();
				System.out.println("CHANGE STATE TO : " + state.getClass().getSimpleName());
			}
		}
	}
	
	/**
	 * @return The current active state. 
	 */
	public IState getCurrentState(){
		return currentState;
	}
	
	/**
	 * @return The last active state. 
	 */
	public IState getLastState(){
		return lastState;
	}
	
	/**
	 * Enters the last state. Calling this more than once will cause the state machine
	 * to jump between the last two states.
	 */
	public void enterLastState(){
		if(lastState != null) changeState(lastState.getClass());
	}
}
