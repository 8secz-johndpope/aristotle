package com.aristotle.admin.jsf.bean.dto;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.aristotle.core.persistance.Interest;

public class InterestModel extends ListDataModel<Interest> implements SelectableDataModel<Interest> {

	public InterestModel() {  
    }  
  
    public InterestModel(List<Interest> data) {
        super(data);  
    }  
    
	@Override
	public Interest getRowData(String rowKey) {
		List<Interest> roles = (List<Interest>) getWrappedData();  
        
        for(Interest car : roles) {  
            if(car.getId().toString().equals(rowKey))  
                return car;  
        }  
          
        return null;  
	}

	@Override
	public Object getRowKey(Interest roleDto) {
		return roleDto.getId().toString();
	}

}
