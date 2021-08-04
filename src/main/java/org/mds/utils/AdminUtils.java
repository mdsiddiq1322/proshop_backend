package org.mds.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.mds.models.Admin;

public class AdminUtils {

    static Connection conn = null;
    
    public static String loginAdmin(String email, String password) throws SQLException, JsonProcessingException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        HashMap<Object, Object> result = new HashMap<>();
        conditionMap.put("email", email);
        conditionMap.put("password", password);
        String query = QueryUtils.selectQuery("employee", conditionMap);
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        Admin admin = new Admin();
        String token = "";
        boolean isCustomerFound = false;
        while(resultSet.next()){
            isCustomerFound = true;
            admin.setId(resultSet.getInt("id"));
            admin.setName(resultSet.getString("name"));
            admin.setEmail(resultSet.getString("email"));
        }
        if(isCustomerFound){
            token = AuthUtils.generateAdminToken(JSONUtils.convertObjectToMap(admin, false));
        } else {
            return "Please check your email or password!";
        }
        if(!token.isEmpty()){
            result.put("id", admin.getId());
            result.put("name", admin.getName());
            result.put("email", admin.getEmail());
            result.put("role", "admin");
            result.put("token", token);
            return JSONUtils.mapToJsonString(result);
        } else {
            return null;
        }
    }
}
