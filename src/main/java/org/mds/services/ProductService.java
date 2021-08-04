package org.mds.services;
import java.sql.SQLException;

import org.mds.models.Product;

import org.mds.utils.ProductUtils;
import org.mds.utils.QueryUtils;

public class ProductService {
    public static String insertProduct(Product model) throws SQLException {
        ProductUtils.insertProduct(model);
        String insId = QueryUtils.getLastInsertId("product");
        return "Product Inserted with id: "+insId;
    }

    public static String updateProduct(String id, Product model) throws SQLException {
        ProductUtils.updateProduct(id, model);
        return "Product updated";
    }

    public static Product getProduct(String id) throws SQLException{
        return ProductUtils.getProduct(id);
    }
}