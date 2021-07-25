package com.htx.schoolstarter.jdbc;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class JDBCExample {

    private Connection connection;

    public void SampleJdbcDemo() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo1","root","");

            //增删改查
            //inset
//        String insertSql = "insert into users values(?,?,?)";
//        PreparedStatement statement = connection.prepareCall(insertSql);
//        statement.setInt(1,2);
//        statement.setString(2,"test2");
//        statement.setString(3,"test2");
//        int i = statement.executeUpdate();
//        System.out.println(i);

            //select
//        String selectQuery = "select * from users where id=?";
//        PreparedStatement statement2 = connection.prepareCall(selectQuery);
//        statement2.setInt(1,1);
//        ResultSet rs = statement2.executeQuery();
//        while(rs.next()) {
//            System.out.println(rs.getString("name"));
//        }

            //update
//        String updateSql = "update users set comment=? where id=?";
//        PreparedStatement statement3 = connection.prepareCall(updateSql);
//        statement3.setString(1,"test1");
//        statement3.setInt(2,1);
//        int i2 = statement3.executeUpdate();
//        System.out.println(i2);

            //delete
//        String deleteSql = "delete from users where id=?";
//        PreparedStatement statement4 = connection.prepareCall(deleteSql);
//        statement4.setInt(1,2);
//        int i3 = statement4.executeUpdate();
//        System.out.println(i3);


            //事务 批处理

            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String sql1 = "insert into users values(1,'test1','test1')";
            String sql2 = "insert into users values(2,'test2','test2')";
            String sql3 = "insert into users values(3,'test3','test3')";
            statement.addBatch(sql1);
            statement.addBatch(sql2);
            statement.addBatch(sql3);
            int[] results = statement.executeBatch();
            connection.commit();
            statement.clearBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }


    }

}
