package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class UnselectionEvent<I>  extends GwtEvent<UnselectionHandler<I>> {

	  /**
	   * Handler type.
	   */
	  private static Type<UnselectionHandler<?>> TYPE;
	  private final I unselectedItem;
	  private boolean canceled;

	  /**
	   * Fires a selection event on all registered handlers in the handler
	   * manager.If no such handlers exist, this method will do nothing.
	   * 
	   * @param <I> the selected item type
	   * @param source the source of the handlers
	   * @param selectedItem the selected item
	   */
	  public static <I> UnselectionEvent<I> fire(HasUnselectionHandlers<I> source, I unselectedItem) {
	    if (TYPE != null) {
	      UnselectionEvent<I> event = new UnselectionEvent<I>(unselectedItem);
	      source.fireEvent(event);
	      return event;
	    }
	    return null;
	  }

	  /**
	   * Gets the type associated with this event.
	   * 
	   * @return returns the handler type
	   */
	  public static Type<UnselectionHandler<?>> getType() {
	    if (TYPE == null) {
	      TYPE = new Type<UnselectionHandler<?>>();
	    }
	    return TYPE;
	  }

	  /**
	   * Creates a new selection event.
	   * 
	   * @param selectedItem selected item
	   */
	  protected UnselectionEvent(I unselectedItem) {
	    this.unselectedItem = unselectedItem;
	  }

	  // The instance knows its BeforeSelectionHandler is of type I, but the TYPE
	  // field itself does not, so we have to do an unsafe cast here.
	  @SuppressWarnings({ "unchecked", "rawtypes" })
	  @Override
	  public final Type<UnselectionHandler<I>> getAssociatedType() {
	    return (Type) TYPE;
	  }

	  /**
	   * Gets the selected item.
	   * 
	   * @return the selected item
	   */
	  public I getUnselectedItem() {
	    return unselectedItem;
	  }
	  
	  public void cancel() {
		  canceled = true;
	  }
	  
	  public boolean isCanceled() {
		  return canceled;
	  }

	  @Override
	  protected void dispatch(UnselectionHandler<I> handler) {
	    handler.onUnselection(this);
	  }
}
