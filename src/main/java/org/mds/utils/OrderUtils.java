package org.mds.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.mds.models.Invoice;
import org.mds.models.Order;
import org.mds.models.Product;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderUtils {

    static Connection conn = null;

    public static String getItemsByInvoice(int invoiceId) throws SQLException, IOException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", invoiceId);
        String query = QueryUtils.selectQuery("invoice", conditionMap);
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        ArrayList<Invoice> items = new ArrayList<>();
        boolean foundItems = false;
        while(resultSet.next()){
            foundItems = true;
            Invoice invoice = new Invoice();
            invoice.setId(resultSet.getInt("id"));
            invoice.setItem_id(resultSet.getInt("item_id"));
            invoice.setQuantity(resultSet.getInt("quantity"));
            invoice.setTotal(resultSet.getInt("total"));
            items.add(invoice);
        }
        if(foundItems){
            return JSONUtils.arrayListToJson(items);
        } else {
            return null;
        }
    }

    public static Order getOrder(String id) throws SQLException, IOException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        String query = QueryUtils.selectQuery("orders", conditionMap);
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        Order order = new Order();
        boolean orderFound = false;
        while (resultSet.next()){
            orderFound = true;
            order.setId(resultSet.getInt("id"));
            order.setTotalAmount(resultSet.getInt("total"));
            order.setOrderStatus(resultSet.getString("order_status"));
            order.setAddress(resultSet.getString("address"));
            order.setCustomerId(resultSet.getInt("customer_id"));
            order.setLineItems(getItemsByInvoice(resultSet.getInt("invoice_id")));
        }
        if(orderFound){
            return order;
        } else {
            return null;
        }
    }

    public static ArrayList<Order> getOrderByCustomer(String id) throws SQLException, IOException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> conditionMap = new HashMap<>();
        if(!id.equals("all")){
            conditionMap.put("customer_id", id);
        }
        String query = QueryUtils.selectQuery("orders", conditionMap);
        ResultSet resultSet = QueryUtils.getResult(conn, query);
        ArrayList<Order> orders = new ArrayList<>();
        boolean orderFound = false;
        while (resultSet.next()){
            orderFound = true;
            Order order = new Order();
            order.setId(resultSet.getInt("id"));
            order.setTotalAmount(resultSet.getInt("total"));
            order.setOrderStatus(resultSet.getString("order_status"));
            order.setCustomerId(resultSet.getInt("customer_id"));
            order.setAddress(resultSet.getString("address"));
            order.setInvoiceId(resultSet.getInt("invoice_id"));
            order.setLineItems(getItemsByInvoice(resultSet.getInt("invoice_id")));
            orders.add(order);
        }
        if(orderFound){
            return orders;
        } else {
            return null;
        }
    }

    public static void updateStock(List<Invoice> invoices) throws SQLException {
        conn = DatabaseUtils.getConnection();
        for(Invoice invoice: invoices){
            String productId = Integer.toString(invoice.getItem_id());
            Product product = ProductUtils.getProduct(productId);
            int newStockCount = product.getStockCount() - invoice.getQuantity();
            product.setStockCount(newStockCount);
            System.out.println("stock count: "+product.getStockCount());
            ProductUtils.updateProduct(productId, product);
        }
    }

    public static void createOrder(Order order) throws SQLException, JsonProcessingException, Exception {
        conn = DatabaseUtils.getConnection();
        List<Invoice> invoice = JSONUtils.jsonToObjectArray(order.getLineItems());
        int id = insertInvoice(invoice);
        System.out.println(id);
        order.setInvoiceId(id);
        String query = "insert into orders (total, customer_id, order_status, invoice_id, address) values ('"+order.getTotalAmount()+"','"+order.getCustomerId() +"','"+order.getOrderStatus()+"','"+id+"', '"+order.getAddress()+"')";
        int result = QueryUtils.executeQueryGetId(conn, query);
        updateStock(invoice);
        System.out.println("Result: "+result);
    }

    private static int insertInvoice(List<Invoice> invoices) throws SQLException, Exception {
        String errors = "[";
        for(Invoice invoice: invoices){
            String productId = Integer.toString(invoice.getItem_id());
            Product product = ProductUtils.getProduct(productId);
            if(!ValidationUtils.validateProduct(product, invoice.getQuantity())){
                errors+="{\"id\": "+ invoice.getItem_id() +",\"count\": "+ ProductUtils.getProductCount(invoice.getItem_id()) +"}";
            }
        }
        errors += "]";
        if(errors.length() != 2){
            throw new Exception(errors);
        } else {

            conn = DatabaseUtils.getConnection();
            String lastId = QueryUtils.getLastInsertId("invoice");
            int id = 1;
            if(!lastId.isEmpty()){
                id = Integer.parseInt(lastId);
                id += 1;
            }
            for (Invoice invoice : invoices) {
                invoice.setId(id);
                String query = QueryUtils.insertQuery("invoice", invoice, false);
                QueryUtils.executeQuery(conn, query);
            }
            return id;
        }
    }

    public static void updateOrder(String id, Order order) throws SQLException {
        conn = DatabaseUtils.getConnection();
        HashMap<Object, Object> updates = new HashMap<>();
        HashMap<Object, Object> conditions = new HashMap<>();
        if(!order.getOrderStatus().isEmpty()){
            updates.put("order_status", order.getOrderStatus());
        }
        conditions.put("id", id);
        String query = QueryUtils.updateQuery("orders", updates, conditions);
        System.out.println(query);
        int result = QueryUtils.executeQueryGetId(conn, query);
        System.out.println("Result: "+result);
    }

}
