package org.mds.rest;

import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.mds.models.Product;
import org.mds.utils.ProductUtils;
import org.mds.utils.QueryUtils;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryController implements ModelDriven<Object> {

    ArrayList<Product> model = new ArrayList<>();
    String id, response = "";
    int code = 200;

    public HttpHeaders show(){
        return new DefaultHttpHeaders("show");
    }

    public HttpHeaders index(){
        try {
            this.model = ProductUtils.getAllProducts("all");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response = throwables.getMessage();
            code = 500;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    public HttpHeaders create(){
        try{
            ProductUtils.createCategory(id);
            String insId = QueryUtils.getLastInsertId("category");
            response = "Category created with id: "+insId;
        } catch (SQLException e){
            e.printStackTrace();
            response = e.getMessage();
            code = 500;
        }
        return new DefaultHttpHeaders("show").withStatus(code);
    }

    @Override
    public Object getModel() {
        return response.isEmpty() ? model : response;
    }

    public String setId() {
        return this.id;
    }

    public void setId(String category) {
        if(category != null){
            try {
                this.model = ProductUtils.getAllProducts(category);
                if(this.model.isEmpty()){
                    response = "No category found";
                    code = 400;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                response = throwables.getMessage();
                code = 500;
            }
        }
        this.id = category;
    }
}
