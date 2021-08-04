package org.mds.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mds.models.Product;

public class ProductUtils {

    static Connection conn = null;
    static ResultSet resultSet = null;

    public static Product getProduct(String id) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", id);
        String query = QueryUtils.selectQuery("product", conditionMap );
        resultSet = QueryUtils.getResult(conn, query);
        Product product = new Product();
        boolean isProductFound = false;
        while (resultSet.next()) {
            isProductFound = true;
            parseResultToProduct(product, resultSet);
        }
        product.setCost_price(0);
        if (isProductFound) {
            return product;
        }
        else{
            return null;
        }
    }

    public static String getProductCount(int i) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", i);
        String query = QueryUtils.selectQuery("product", conditionMap );
        resultSet = QueryUtils.getResult(conn, query);
        String count = "";
        boolean isProductFound = false;
        while (resultSet.next()) {
            isProductFound = true;
            count = Integer.toString(resultSet.getInt("stock_count"));
        }
        if (isProductFound) {
            return count;
        }
        else{
            return null;
        }
    }

    public static int getProductCategoryId(String category) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        conditionMap.put("category_name", category);
        String query = QueryUtils.selectQuery("category", conditionMap);
        resultSet = QueryUtils.getResult(conn, query);
        boolean isCategoryFound = false;
        int categoryId = 0;
        while (resultSet.next()) {
            isCategoryFound = true;
            categoryId = resultSet.getInt("id");
        }
        if(isCategoryFound){
            return categoryId;
        } else {
            return 0;
        }
    }
    public static String getProductCategoryName(int category) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", Integer.toString(category));
        String query = QueryUtils.selectQuery("category", conditionMap);
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        boolean isCategoryFound = false;
        String categoryName = null;
        while (resultSet.next()) {
            isCategoryFound = true;
            categoryName = resultSet.getString("category_name");
        }
        if(isCategoryFound){
            return categoryName;
        } else {
            return null;
        }
    }

    public static ArrayList<Product> getAllProducts(String categoryName) throws SQLException {
        conn = DatabaseUtils.getConnection();
        System.out.println("category name: "+categoryName);
        int category = getProductCategoryId(categoryName);
        HashMap<Object, Object> conditionMap = new HashMap<>();
        if(!categoryName.equals("all")){
            conditionMap.put("category", category);
        }
        String query = QueryUtils.selectQuery("product", conditionMap);
        ResultSet products = QueryUtils.getResult(conn, query);
        ArrayList<Product> list = new ArrayList<>();
        while (products.next()){
            Product product = new Product();
            parseResultToProduct(product, products);
            product.setCost_price(0);
            list.add(product);
        }
        return list;
    }

    private static void parseResultToProduct(Product product, ResultSet resultSet) throws SQLException {
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getInt("price"));
        product.setImage(resultSet.getString("image"));
        product.setStock_count(resultSet.getInt("stock_count"));
        product.setCost_price(resultSet.getInt("cost_price"));
        product.setCreated_at(resultSet.getDate("created_at").toString());
        int categoryId = resultSet.getInt("category");
        String categoryName = ProductUtils.getProductCategoryName(categoryId);
        product.setCategory(categoryName);
    }

    public static void insertProduct(Product product) throws SQLException {
        conn = DatabaseUtils.getConnection();
        String query = QueryUtils.insertQuery("product", product, true);
        int result = QueryUtils.executeQueryGetId(conn, query);
        System.out.println("Result: "+result);
    }

    public static void updateProduct(String id, Product product) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> updates = new HashMap<>();
        HashMap<Object, Object> conditions = new HashMap<>();
        if(!product.getName().isEmpty()){
            updates.put("name", product.getName());
        }
        if(!product.getCategory().isEmpty()){
            int catId = getProductCategoryId(product.getCategory());
            updates.put("category", catId);
        }
        if(!product.getDescription().isEmpty()){
            updates.put("description", product.getDescription());
        }
        if(!product.getImage().isEmpty()){
            updates.put("image", product.getImage());
        }
        if(product.getPrice() != 0){
            updates.put("price", product.getPrice());
        }
        if(product.getCost_price() != 0){
            updates.put("cost_price", product.getCost_price());
        }
        updates.put("stock_count", product.getStock_count());
        conditions.put("id", id);
        String query = QueryUtils.updateQuery("product", updates, conditions);
        int result = QueryUtils.executeQueryGetId(conn, query);
        System.out.println("Result: "+result);
    }

    public static void createCategory(String category) throws SQLException {
        conn = DatabaseUtils.getConnection();
        String query = "insert into category (category_name) values ('"+category+"')";
        int result = QueryUtils.executeQuery(conn, query);
        System.out.println("Result: "+result);
    }

    public static void updateCategory(String id, String category_name) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> updates = new HashMap<>();
        HashMap<Object, Object> conditions = new HashMap<>();
        updates.put("category_name", category_name);
        conditions.put("id", id);
        String query = QueryUtils.updateQuery("product", updates, conditions);
        int result = QueryUtils.executeQueryGetId(conn, query);
        System.out.println("Result: "+result);
    }
}
