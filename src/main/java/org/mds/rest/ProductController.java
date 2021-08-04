package org.mds.rest;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.mds.models.Product;
import org.mds.services.ProductService;

import java.sql.SQLException;

public class ProductController extends ActionSupport implements ModelDriven<Object> {

    String id, response = "";
    private Product model = new Product();
    int code = 200;

    public HttpHeaders show(){
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    public HttpHeaders index() {
        response = "Invalid action";
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders create() {
        try {
            response = ProductService.insertProduct(model);
        } catch (Exception throwable) {
            throwable.printStackTrace();
            response = throwable.getMessage();
            code = 500;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    public HttpHeaders update(){
        try{
            response = ProductService.updateProduct(id, model);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            response = throwable.getMessage();
            code = 500;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    @Override
    public Object getModel() {
        return (response.equals("") ? model : response);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id != null){
            try {
                this.model = ProductService.getProduct(id);
                if(this.model == null){
                    response = "No product found";
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
}
