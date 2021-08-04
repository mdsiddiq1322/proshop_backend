package org.mds.utils;

import java.sql.SQLException;

import org.mds.models.Auth;
import org.mds.models.Product;

public class ValidationUtils {

    public static boolean validateProduct(Product product, int count) throws SQLException {
        String id = Integer.toString(product.getId());
        Product foundProduct = (Product) ProductUtils.getProduct(id);
        if(count <= foundProduct.getStock_count()){
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateAuthFields(Auth auth) {
        if(auth.getEmail().isEmpty() || auth.getPassword().isEmpty() || auth.getRole().isEmpty()){
            return false;
        } else {
            return true;
        }
    }
}
