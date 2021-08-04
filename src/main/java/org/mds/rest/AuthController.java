package org.mds.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.mds.models.Auth;
import org.mds.utils.AdminUtils;
import org.mds.utils.CustomerUtils;
import org.mds.utils.ValidationUtils;

import java.sql.SQLException;

public class AuthController extends ActionSupport implements ModelDriven<Object> {

    Auth model = new Auth();
    String id, response = "";
    int code = 200;

    public HttpHeaders show(){
        response = "Invalid action";
        return new DefaultHttpHeaders("show");
    }
    
    public HttpHeaders index(){
        response = "Invalid action";
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders create(){
        try {
            if(!ValidationUtils.validateAuthFields(model)){
                response = "Please check the missing params!";
                code = 400;
            } else {
                String role = model.getRole();
                String email = model.getEmail();
                String password = model.getPassword();
                if(role.equals("customer")){
                    response = CustomerUtils.loginCustomer(email, password);
                } else if (role.equals("admin")){
                    response = AdminUtils.loginAdmin(email, password);
                } else {
                    response = "Invalid action!";
                }
            }
        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
            response = throwables.getMessage();
            code = 500;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    @Override
    public Object getModel() {
        return response.isEmpty() ? model : response;
    }
}
