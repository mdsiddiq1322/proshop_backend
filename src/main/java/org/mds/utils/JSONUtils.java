package org.mds.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mds.models.Invoice;
import org.mds.models.Product;
import java.lang.reflect.Field;

public class JSONUtils {

    static ObjectMapper mapper = new ObjectMapper();

    public static Object convertResultSetArraytoObject(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultMeta = resultSet.getMetaData();
        HashMap<String, Object>[] productsArray = null;
        while(resultSet.next()){
            int numColumns = resultMeta.getColumnCount();
            HashMap<Object, Object> obj = new HashMap<>();

            for (int i=1; i<numColumns+1; i++) {
                String column_name = resultMeta.getColumnName(i);

                if(resultMeta.getColumnType(i)==java.sql.Types.ARRAY){
                    obj.put(column_name, resultSet.getArray(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.BIGINT){
                    obj.put(column_name, resultSet.getInt(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.BOOLEAN){
                    obj.put(column_name, resultSet.getBoolean(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.BLOB){
                    obj.put(column_name, resultSet.getBlob(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.DOUBLE){
                    obj.put(column_name, resultSet.getDouble(column_name)); 
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.FLOAT){
                    obj.put(column_name, resultSet.getFloat(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.INTEGER){
                    obj.put(column_name, resultSet.getInt(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.NVARCHAR){
                    obj.put(column_name, resultSet.getNString(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.VARCHAR){
                    obj.put(column_name, resultSet.getString(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.TINYINT){
                    obj.put(column_name, resultSet.getInt(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.SMALLINT){
                    obj.put(column_name, resultSet.getInt(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.DATE){
                    obj.put(column_name, resultSet.getDate(column_name));
                }
                else if(resultMeta.getColumnType(i)==java.sql.Types.TIMESTAMP){
                    obj.put(column_name, resultSet.getTimestamp(column_name));   
                }
                else{
                    obj.put(column_name, resultSet.getObject(column_name));
                }
            }
        }    
        return productsArray;
    }

    public static Object convertProductToJson(Product product) {
        HashMap<String, Object> productObject = new HashMap<>();
        productObject.put("id", product.getId());
        productObject.put("name", product.getName());
        productObject.put("description", product.getDescription());
        productObject.put("image", product.getImage());
        productObject.put("category", product.getCategory());
        productObject.put("price", product.getPrice());
        productObject.put("stock_count", product.getStockCount());
        productObject.put("created_at", product.getCreated_at());
        return productObject;
    }

    public static HashMap<Object, Object> convertObjectToMap(Object object, boolean exceptId) {
        HashMap<Object, Object> productObject = new HashMap<>();
        Field[] allFields = object.getClass().getDeclaredFields();
        for (Field field : allFields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(object);
                if(exceptId){
                    if(!field.getName().equals("id")){
                        productObject.put(field.getName(), value);
                    }
                } else {
                    productObject.put(field.getName(), value);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } 
        return productObject;
    }

    public static Map<String, String> jsonToObject(String json){
        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(json, new TypeReference<HashMap<String, String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String mapToJsonString(HashMap<Object, Object> map) throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(map);
        return result;
    }

    public static List<Invoice> jsonToObjectArray(String lineItems) throws JsonProcessingException {
        return mapper.readValue(lineItems, new TypeReference<List<Invoice>>() {});
    }

    public static String arrayListToJson(ArrayList<Invoice> lineItems) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, lineItems);
        return out.toString();
    }
}
