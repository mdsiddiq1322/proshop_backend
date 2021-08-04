package org.mds.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.mds.models.Customer;

public class CustomerUtils {

    static Connection conn = null;

    public static void createCustomer(Customer customer) throws SQLException {
        conn = DatabaseUtils.getConnection();
        String query = QueryUtils.insertQuery("customer", customer, true);
        int result = QueryUtils.executeQuery(conn, query);
        System.out.println("Result: "+result);
    }

    public static String loginCustomer(String email, String password) throws SQLException, JsonProcessingException {
        System.out.println(email+" "+password);
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        HashMap<Object, Object> result = new HashMap<>();
        conditionMap.put("email", email);
        conditionMap.put("password", password);
        String query = QueryUtils.selectQuery("customer", conditionMap);
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        Customer customer = new Customer();
        String token = "";
        boolean isCustomerFound = false;
        while(resultSet.next()){
            isCustomerFound = true;
            customer.setId(resultSet.getInt("id"));
            customer.setName(resultSet.getString("name"));
            customer.setEmail(resultSet.getString("email"));
            customer.setPhone(resultSet.getString("phone"));
        }
        System.out.println(customer.getName());
        if(isCustomerFound){
            token = AuthUtils.generateToken(JSONUtils.convertObjectToMap(customer, false));
        } else {
            throw new SQLException("Please check your email or password!");
        }
        if(!token.isEmpty()){
            result.put("id", customer.getId());
            result.put("name", customer.getName());
            result.put("email", customer.getEmail());
            result.put("phone", customer.getPhone());
            result.put("tokrn", token);
            return JSONUtils.mapToJsonString(result);
        } else {
            return null;
        }
    }

    public static Customer getCustomer(String id) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", id);
        String query = QueryUtils.selectQuery("customer", conditionMap );
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        Customer customer = new Customer();
        boolean isCustomerFound = false;
        while (resultSet.next()) {
            isCustomerFound = true;
            parseResultToCustomer(customer, resultSet);
        }
        if (isCustomerFound) {
            return customer;
        }
        else{
            return null;
        }
    }

    private static void parseResultToCustomer(Customer customer, ResultSet resultSet) throws SQLException {
        customer.setId(resultSet.getInt("id"));
        customer.setName(resultSet.getString("name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setBillingAddress(resultSet.getString("billing_address"));
        customer.setShippingAddress(resultSet.getString("shipping_address"));
    }

    public static ArrayList<Customer> getAllCustomers() throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        String query = QueryUtils.selectQuery("customer", conditionMap);
        ResultSet customers = QueryUtils.getResult(conn, query);
        ArrayList<Customer> list = new ArrayList<>();
        while (customers.next()){
            Customer customer = new Customer();
            parseResultToCustomer(customer, customers);
            list.add(customer);
        }
        return list;
    }
}
