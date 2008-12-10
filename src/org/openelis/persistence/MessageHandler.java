package org.openelis.persistence;

public interface MessageHandler<O extends Message> {
    
    public void handle(O message);

}
