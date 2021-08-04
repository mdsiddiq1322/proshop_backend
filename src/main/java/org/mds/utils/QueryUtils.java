package org.mds.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map.Entry;

public class QueryUtils {

    static Connection conn = null;

    public static PreparedStatement prepareStatement(Connection conn, String query) throws SQLException{
        return conn.prepareStatement(query);
    }

    public static PreparedStatement prepareStatementRetId(Connection conn, String query) throws SQLException{
        return conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    public static ResultSet getResult(Connection conn, String query) throws SQLException {
        PreparedStatement preparedStatement = prepareStatement(conn, query);
        return preparedStatement.executeQuery();
    }

    public static String selectQuery(String table, HashMap<Object, Object> conditionMap) {
        if(conditionMap.isEmpty()){
            return "select * from "+table+"  where 1";
        }
        String query = "select * from "+table+" where ";
        int i=0, l = conditionMap.size();
        for(Entry<Object, Object> condition: conditionMap.entrySet()){
            query+= condition.getKey().toString()+" = '"+condition.getValue().toString()+"'";
            if(i!=(l-1)){
                query+=" and ";
            }
            i++;
        }
        return query;
    }

    public static String deleteQuery(String table, String key, String value) {
        return "delete from "+table+"  where "+key+"  = '"+value+"'";
    }

    public static String insertQuery(String table, Object object, boolean exceptId) {
        HashMap<Object, Object> keyValue = JSONUtils.convertObjectToMap(object, exceptId);
        String keyString = "", valueString = "";
        int length = keyValue.size(), i=0;
        System.out.println("size: "+keyValue.size());
        for(Entry<Object, Object> pair: keyValue.entrySet()){
            keyString+=pair.getKey();
            valueString+="'"+pair.getValue()+"'";
            if(i<length-1){
                keyString+=",";
                valueString+=",";
            }
            i++;
        }
        return "insert into "+table+" ("+keyString+") values ("+valueString+")";
    }
    
    public static String updateQuery(String table, HashMap<Object, Object> updates, HashMap<Object, Object> conditions) {
        String query = "update "+table+" set ";
        int length = updates.size(), clength = conditions.size(), i=0;
        for(Entry<Object, Object> pair: updates.entrySet()){
            if(pair.getValue() != null){
                query+=pair.getKey()+" = ";
                query+="'"+pair.getValue()+"'";
                if(i!=length-1){
                    query+=",";
                }
            }
            i++;
        }
        i=0;
        query+=" where ";
        for(Entry<Object, Object> pair: conditions.entrySet()){
            query += pair.getKey()+" = ";
            query += "'"+pair.getValue()+"'";
            if(i!=clength-1){
                query+=" and ";
            }
            i++;
        }
        return query;
    }

    public static int executeQuery(Connection conn, String query) throws SQLException {
        PreparedStatement preparedStatement = prepareStatement(conn, query);
        return preparedStatement.executeUpdate();
    }

    public static int executeQueryGetId(Connection conn, String query) throws SQLException {
        PreparedStatement preparedStatement = prepareStatementRetId(conn, query);
        return preparedStatement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
    }

    public static String getLastInsertId(String table) throws SQLException {
        conn = DatabaseUtils.getConnection();
        String query = "select * from "+table+" order by id desc";
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        String id = "";
        boolean isIdFound = false;
        while(resultSet.next()){
            isIdFound = true;
            id = Integer.toString(resultSet.getInt("id"));
            break;
        }
        if(isIdFound){
            return id;
        }
        else {
            return null;
        }
    }
}
