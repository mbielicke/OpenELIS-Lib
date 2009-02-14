package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class CommandEvent<DATA> extends GwtEvent<CommandHandler<DATA>> {

	private Enum command;
	private DATA data;
	
	private static Type<CommandHandler<?>> TYPE;
	
	protected CommandEvent(Enum command, DATA data) {
		this.command = command;
		this.data = data;
	}
	
	public static <DATA> void fire(HasCommandHandlers<DATA> source, Enum command, DATA data) {
		if(TYPE != null){
			CommandEvent<DATA> ce = new CommandEvent<DATA>(command,data);
			source.fireEvent(ce);
		}
	}
	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<CommandHandler<?>> getType() {
	  if (TYPE == null) {
	    TYPE = new Type<CommandHandler<?>>();
	  }
	  return TYPE;
	}
	
	public Enum getCommand() {
		return command;
	}
	
	public DATA getData() {
		return data;
	}
	
	@Override
	protected void dispatch(CommandHandler<DATA> handler) {
		handler.performCommand(command,data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Type<CommandHandler<DATA>> getAssociatedType() {
		// TODO Auto-generated method stub
		return (Type) TYPE;
	}

}
