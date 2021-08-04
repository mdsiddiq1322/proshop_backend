package org.mds.rest;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.mds.models.Customer;
import org.mds.utils.CustomerUtils;

import java.sql.SQLException;

public class CustomerController extends ActionSupport implements ModelDriven<Object> {

    Customer model = new Customer();
    String id, response = "";
    int code = 200;

    public HttpHeaders show(){
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders index(){
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders create(){
        try {
            CustomerUtils.createCustomer(model);
            response = "Signup success!";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response = throwables.getMessage();
            code = 500;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    public HttpHeaders update(){
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
            response = e.getMessage();
            code = 500;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id != null){
            try {
                this.model = CustomerUtils.getCustomer(id);
                if(model == null){
                    response = "No order found!";
                    code = 400;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                response = throwables.getMessage();
                code=500;
            }
        }
        this.id = id;
    }

    @Override
    public Object getModel() {
        return response.isEmpty() ? model : response;
    }
}
