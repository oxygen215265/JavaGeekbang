## 按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率



### 1.直接插入

一万条数据就用了10分钟

```java
        Random rand = new Random();
        Connection connection = dataSource.getConnection();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String sql = "insert into t_order(commodity_id, user_id, order_amount, order_status) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareCall(sql);

        for(int i=0;i<10000;i++) {
            statement.setInt(1,rand.nextInt(100));
            statement.setInt(2,rand.nextInt(100));
            statement.setInt(3,rand.nextInt(100));
            statement.setInt(4,rand.nextInt(100));
            statement.executeUpdate();
        }

        stopWatch.stop();
        System.out.println( "used: "+stopWatch.getTotalTimeMillis());
```


### 2.事务一次性提交 setAutoCommit(false)
100万 178秒

```java
        Random rand = new Random();
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String sql = "insert into t_order(commodity_id, user_id, order_amount, order_status) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareCall(sql);

        for(int i=0;i<1000000;i++) {
            statement.setInt(1,rand.nextInt(100));
            statement.setInt(2,rand.nextInt(100));
            statement.setInt(3,rand.nextInt(100));
            statement.setInt(4,rand.nextInt(100));
            statement.executeUpdate();
        }

        connection.commit();
        stopWatch.stop();
        System.out.println( "used: "+stopWatch.getTotalTimeMillis());

```



### 3.批量处理 statement.executeBatch();

100万 216秒

```java

        Random rand = new Random();
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String sql = "insert into t_order(commodity_id, user_id, order_amount, order_status) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareCall(sql);

        for(int i=0;i<1000000;i++) {
            statement.setInt(1,rand.nextInt(100));
            statement.setInt(2,rand.nextInt(100));
            statement.setInt(3,rand.nextInt(100));
            statement.setInt(4,rand.nextInt(100));
            statement.addBatch();
        }

        statement.executeBatch();

        connection.commit();
        stopWatch.stop();
        System.out.println( "used: "+stopWatch.getTotalTimeMillis());

```





