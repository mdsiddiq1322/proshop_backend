package org.mds.rest;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.mds.models.Order;
import org.mds.utils.OrderUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersController extends ActionSupport implements ModelDriven<Object> {

    ArrayList<Order> model = new ArrayList<>();
    String id, response = "";
    int code = 200;

    public HttpHeaders show(){
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders index(){

        try {
            this.model = OrderUtils.getOrderByCustomer("all");
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            response = throwables.getMessage();
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
                this.model = OrderUtils.getOrderByCustomer(id);
                if(model == null){
                    response = "No order found for this customer!";
                    code = 400;
                }
            } catch (SQLException | IOException throwables) {
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
