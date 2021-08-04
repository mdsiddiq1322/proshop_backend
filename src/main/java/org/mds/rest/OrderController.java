package org.mds.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.mds.models.Order;
import org.mds.utils.OrderUtils;
import org.mds.utils.QueryUtils;

import java.io.IOException;
import java.sql.SQLException;

public class OrderController extends ActionSupport implements ModelDriven<Object> {

    Order model = new Order();
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
            OrderUtils.createOrder(model);
            String insId = QueryUtils.getLastInsertId("orders");
            response = "Order created with id: "+insId;
        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
            response = throwables.getMessage();
            code = 500;
        } catch(Exception e){
            e.printStackTrace();
            response = e.getMessage();
            code = 400;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    public HttpHeaders update(){
        try{
            System.out.println("update id: "+id+" "+model.getOrderStatus());
            OrderUtils.updateOrder(id, model);
            response = "Order updated";
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            response = throwable.getMessage();
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
                this.model = OrderUtils.getOrder(id);
                if(model == null){
                    response = "No order found!";
                    code = 400;
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
                response = throwables.getMessage();
                code=500;
            } catch(Exception e){
                e.printStackTrace();
                response = e.getMessage();
                code = 400;
            }
        }
        this.id = id;
    }

    @Override
    public Object getModel() {
        return response.isEmpty() ? model : response;
    }
}
