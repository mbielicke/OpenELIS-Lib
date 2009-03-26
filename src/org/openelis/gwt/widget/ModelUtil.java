package org.openelis.gwt.widget;

import org.openelis.gwt.common.data.TableDataModel;
import org.openelis.gwt.common.data.TableDataRow;

import java.util.HashMap;

public class ModelUtil {

    public static <T> HashMap<T,TableDataRow<T>> getModelMap(TableDataModel<TableDataRow<T>> model, HashMap<T,TableDataRow<T>> map) {
        for(TableDataRow<T> row : model.list)
            map.put(row.key,row);
        return map;
    }
    
    public static <T extends TableDataRow<?>> T getRowByKey(TableDataModel<T> model, Object key) {
       if(key == null)
           return model.get(0);
       for(T row : model.list){
           if(key.equals(row.key)){
               return (T)row;
           }
       }
       return null;    
    }
}
