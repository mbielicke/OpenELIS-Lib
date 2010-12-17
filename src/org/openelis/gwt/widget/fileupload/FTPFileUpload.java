package org.openelis.gwt.widget.fileupload;


import gwtupload.client.IFileInput;
import gwtupload.client.IUploader.UploaderConstants;

import java.util.HashMap;

import org.openelis.gwt.event.FTPStartUploadEvent;
import org.openelis.gwt.event.FTPStartUploadHandler;
import org.openelis.gwt.event.HasFTPStartUploadHandlers;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.Label;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FTPFileUpload extends Composite implements HasText, HasName, HasChangeHandlers, IFileInput, HasFTPStartUploadHandlers {
	
		  
		  /**
		   * An abstract class which is the base for specific browser implementations.
		   */
		  private static class DecoratedFileUploadImpl {

		    protected Widget button;
		    protected AbsolutePanel container;
		    protected FileUploadWithMouseEvents input;

		    public void init(AbsolutePanel container, FileUploadWithMouseEvents input) {
		      this.container = container;
		      this.input = input;
		    }

		    public void resize() {
		      if (button != null) {
		        container.setWidth(button.getOffsetWidth() + "px");
		        container.setHeight(button.getOffsetHeight() + "px");
		      }
		    }

		    public void setButton(Widget widget) {
		      this.button = widget;
		      if (button instanceof HasMouseOverHandlers) {
		        ((HasMouseOverHandlers) button).addMouseOverHandler(new MouseOverHandler() {
		          public void onMouseOver(MouseOverEvent event) {
		            button.addStyleDependentName(STYLE_BUTTON_OVER_SUFFIX);
		          }
		        });
		      }
		      if (button instanceof HasMouseOutHandlers) {
		        ((HasMouseOutHandlers) button).addMouseOutHandler(new MouseOutHandler() {
		          public void onMouseOut(MouseOutEvent event) {
		            button.removeStyleDependentName(STYLE_BUTTON_OVER_SUFFIX);
		          }
		        });
		      }
		    }
		  }

		  /**
		   * Implementation for browsers which support the click() method:
		   * IE, Chrome, Safari
		   * 
		   * The hack here is to put the customized button
		   * and the file input statically positioned in an absolute panel. 
		   * This panel has the size of the button, and the input is not shown 
		   * because it is placed out of the width and height panel limits.
		   * 
		   */
		  @SuppressWarnings("unused")
		  private static class DecoratedFileUploadImplClick extends DecoratedFileUploadImpl {

		    private static HashMap<Widget, HandlerRegistration> clickHandlerCache = new HashMap<Widget, HandlerRegistration>();

		    private static native void clickOnInputFile(Element elem) /*-{
		      elem.click();
		    }-*/;

		    public void init(AbsolutePanel container, FileUploadWithMouseEvents input) {
		      super.init(container, input);
		      container.add(input, 500, 500);
		      DOM.setStyleAttribute(container.getElement(), "cssFloat", "left");
		      DOM.setStyleAttribute(container.getElement(), "display", "inline");
		    }

		    public void setButton(Widget widget) {
		      super.setButton(widget);
		      HandlerRegistration clickRegistration = clickHandlerCache.get(widget);
		      if (clickRegistration != null) {
		        clickRegistration.removeHandler();
		      }
		      if (button != null) {
		        if (button instanceof HasClickHandlers) {
		          clickRegistration = ((HasClickHandlers) button).addClickHandler(new ClickHandler() {
		            public void onClick(ClickEvent event) {
		              clickOnInputFile(input.getElement());
		            }
		          });
		          clickHandlerCache.put(widget, clickRegistration);
		        }
		      }
		    }
		  }

		  /**
		   * Implementation for browsers which do not support the click() method:
		   * FF, Opera
		   * 
		   * The hack here is to place the customized button and the file input positioned
		   * statically in an absolute panel which has size of the button. 
		   * The file input is wrapped into a transparent panel, which also has the button
		   * size and is placed covering the customizable button.
		   * 
		   * When the user puts his mouse over the button and clicks on it, what really 
		   * happens is that the user clicks on the transparent file input showing
		   * the choose file dialog.
		   * 
		   */  
		  @SuppressWarnings("unused")
		  private static class DecoratedFileUploadImplNoClick extends DecoratedFileUploadImpl {

		    private SimplePanel wrapper;

		    public void init(AbsolutePanel container, FileUploadWithMouseEvents input) {
		      super.init(container, input);
		      wrapper = new SimplePanel();
		      wrapper.add(input);
		      container.add(wrapper, 0, 0);
		      wrapper.setStyleName("wrapper");
		      
		      // Not using GWT 2.0.x way to set Style attributes in order to be
		      // compatible with old GWT releases
		      DOM.setStyleAttribute(container.getElement(), "cssFloat", "left");
		      DOM.setStyleAttribute(wrapper.getElement(), "textAlign", "left");
		      DOM.setStyleAttribute(wrapper.getElement(), "zIndex", "1");
		      DOM.setStyleAttribute(input.getElement(), "marginLeft", "-1500px");
		      DOM.setStyleAttribute(input.getElement(), "fontSize", "500px");
		      DOM.setStyleAttribute(input.getElement(), "borderWidth", "0px");
		      DOM.setStyleAttribute(input.getElement(), "opacity", "0");
		      DOM.setElementAttribute(input.getElement(), "size", "1");
		      
		      // Trigger over and out handlers which already exist in the covered button.
		      input.addMouseOverHandler(new MouseOverHandler() {
		        public void onMouseOver(MouseOverEvent event) {
		          if (button != null) {
		            button.fireEvent(event);
		          }
		        }
		      });
		      input.addMouseOutHandler(new MouseOutHandler() {
		        public void onMouseOut(MouseOutEvent event) {
		          if (button != null) {
		            button.fireEvent(event);
		          }
		        }
		      });
		    }

		    public void resize() {
		      super.resize();
		      if (button != null) {
		        wrapper.setWidth(button.getOffsetWidth() + "px");
		        wrapper.setHeight(button.getOffsetHeight() + "px");
		      }
		    }
		  }

		  /**
		   * A FileUpload which implements onChange, onMouseOver and onMouseOut events.
		   * 
		   * Note FileUpload in version 2.0.x implements onChange event, but we put it here 
		   * in order to be compatible with 1.6.x
		   *
		   */
		  public static class FileUploadWithMouseEvents extends FileUpload implements HasMouseOverHandlers, HasMouseOutHandlers, HasChangeHandlers {

		    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		      return addDomHandler(handler, ChangeEvent.getType());
		    }

		    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		      return addDomHandler(handler, MouseOutEvent.getType());
		    }

		    public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
		      return addDomHandler(handler, MouseOverEvent.getType());
		    }
		  }

		  private static final String STYLE_BUTTON_OVER_SUFFIX = "over";
		  private static final String STYLE_CONTAINER = "DecoratedFileUpload";
		  protected Widget button;
		  protected HorizontalPanel hp;
		  protected VerticalPanel vp;
		  protected TextBox text;
		  protected AbsolutePanel container;
		  protected FileUploadWithMouseEvents input = new FileUploadWithMouseEvents();
		  protected boolean reuseButton = false;
		  private DecoratedFileUploadImpl impl;
		  private UploaderConstants strs = GWT.create(UploaderConstants.class);
		  private String service;

		  public FTPFileUpload(String service) {
			this.service = service;
		    impl = GWT.create(DecoratedFileUploadImpl.class);
		    vp = new VerticalPanel();
		    hp = new HorizontalPanel();
		    container = new AbsolutePanel();
		    container.addStyleName(STYLE_CONTAINER);
		    text = new TextBox();
		    hp.add(text);
		    initWidget(vp);
		    vp.add(hp);
		    vp.add(container);
		    impl.init(container, input);
		    Button ab = new Button();
		    ab.setStyleName("Button");
		    Label load = new Label("Load...");
		    load.setStyleName("ScreenLabel");
		    ab.setDisplay(load);
		    hp.add(ab);
		    final HasFTPStartUploadHandlers source = this;
		    ab.addClickHandler(new ClickHandler() {
		    	public void onClick(ClickEvent event) {
		    		FTPStartUploadEvent.fire(source, text.getText());
		    	}
		    });
		    ab.setEnabled(true);
		    setButton(new Label("Choose file..."));
		  }

		  public FTPFileUpload(String service, Widget button) {
		    this(service);
		    setButton(button);
		  }

		  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		    return input.addChangeHandler(handler);
		  }

		  public String getFilename() {
		    return input.getFilename();
		  }

		  public FileUpload getFileUpload() {
		    return input;
		  }

		  public String getName() {
		    return input.getName();
		  }

		  public String getText() {
		    if (button == null) {
		      return "";
		    }
		    if (button instanceof HasText) {
		      return ((HasText) button).getText();
		    } else {
		      return button.toString();
		    }
		  }

		  public Widget getWidget() {
		    return this;
		  }

		  @Override
		  public void onAttach() {
		    super.onAttach();
		    if (button == null) {
		      button = new Button();
		      ((Button)button).setDisplay(new Label(strs.uploaderBrowse()));
		      setButton(button);
		    } else {
		      impl.resize();
		    }
		  }

		  public void setButton(Widget button) {
		    assert button instanceof HasClickHandlers : "Button should extend HasClickHandlers";
		    if (this.button != null) {
		      container.remove(this.button);
		    }
		    this.button = button;
		    container.add(button, 0, 0);
		    impl.setButton(button);
		    impl.resize();
		  }

		  public void setButtonSize(String width, String height) {
		    button.setSize(width, height);
		    impl.resize();
		  }

		  public void setName(String fieldName) {
		    input.setName(fieldName);
		  }

		  public void setText(String text) {
		    if (button instanceof HasText) {
		      ((HasText) button).setText(text);
		      impl.resize();
		    }
		  }

		public IFileInput newInstance() {
			return new FTPFileUpload(service);
		}

		public void setLength(int length) {
			// TODO Auto-generated method stub
			
		}
		
		public HandlerRegistration addFTPStartUploadHandler(
				FTPStartUploadHandler handler) {
			return addHandler(handler, FTPStartUploadEvent.getType());
		}
		
		public String getService() {
			return service;
		}
}
