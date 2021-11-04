package ru.netology.web.data;


import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Value
public class Sql {
    private static final String url = System.getProperty("db.url");
    private static final String user = "app";
    private static final String password = "pass";
    private static Connection conn;

    public static String getStatusPurchase() throws SQLException {
        QueryRunner runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            val selectStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;create table payment_entity(	status int null,	created int null);";
            val status = runner.query(conn, selectStatus, new BeanHandler<>(PaymentEntity.class));
            return status.getStatus();
        }
    }


    public static String getTransactionIdFromPaymentEntity() throws SQLException {
        QueryRunner runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            val selectSql = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1;create table payment_entity(	transaction_id int null,	created int null);";
            val status = runner.query(conn, selectSql, new BeanHandler<>(PaymentEntity.class));
            return status.getTransaction_id();
        }
    }

    public static OrderEntity getDataFromOrderEntity() throws SQLException {
        QueryRunner runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            val selectOrderEntity = "SELECT payment_id, credit_id FROM order_entity  LIMIT 1;create table order_entity(	payment_id int null,	credit_id int null);";
            return runner.query(conn, selectOrderEntity, new BeanHandler<>(OrderEntity.class));
        }
    }

    public static String getStatusPurchaseByCredit() throws SQLException {
        QueryRunner runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            val selectStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;create table credit_request_entity(	status int null,	created int null);";
            val status = runner.query(conn, selectStatus, new BeanHandler<>(CreditRequestEntity.class));
            return status.getStatus();
        }
    }

    public static String getBankIdFromPaymentEntity() throws SQLException {
        QueryRunner runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)
        ) {
            val selectStatus = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1;create table credit_request_entity(	bank_id int null,	created int null);";
            val status = runner.query(conn, selectStatus, new BeanHandler<>(CreditRequestEntity.class));
            return status.getBank_id();
        }
    }

    public static void clearDBTables() throws SQLException {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user,password)) {
            runner.update(conn, "DELETE FROM credit_request_entity");
            runner.update(conn, "DELETE FROM order_entity");
            runner.update(conn, "DELETE FROM payment_entity");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
