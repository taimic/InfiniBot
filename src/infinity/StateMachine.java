package infinity;

import infinity.states.IState;
import infinity.states.State;

import java.util.ArrayList;

public class StateMachine {
	// Holds all states
	ArrayList<IState> states;
	// The current active state
	IState currentState;
	
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
			if(!this.states.contains(state)) this.states.add(state);
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
	 *  @param to The class of the registered state to change to
	 */
	public void changeState(Class<? extends State> to){
		for(IState state : this.states){
			if(state.getClass().equals(to)) currentState = state;
		}
	}
	
	/**
	 * @return The current active state. 
	 */
	public IState getCurrentState(){
		return currentState;
	}
}
