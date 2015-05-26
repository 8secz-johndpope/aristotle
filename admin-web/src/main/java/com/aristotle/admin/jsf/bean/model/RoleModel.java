package com.aristotle.admin.jsf.bean.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.aristotle.core.persistance.Role;

public class RoleModel extends ListDataModel<Role> implements SelectableDataModel<Role> {

	public RoleModel() {  
    }  
  
    public RoleModel(List<Role> data) {
        super(data);  
    }  
    
	@Override
    public Role getRowData(String rowKey) {
        List<Role> roles = (List<Role>) getWrappedData();
        
        for (Role oneRole : roles) {
            System.out.println("getRowData : " + oneRole.getId() + ", " + rowKey);
            if(oneRole.getId().toString().equals(rowKey))  
                return oneRole;  
        }  
        System.err.println("getRowData : No Match for " + rowKey);
        return null;  
	}

	@Override
    public Object getRowKey(Role role) {
        System.out.println("getRowKey : " + role.getId().toString());
		return role.getId().toString();
	}

}
