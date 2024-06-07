package JDBC;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static{
        //JDBCUtils.class获得对象
        //getClassLoader()类加载器
        //getResourceAsStream("db.properties")加载资源文件放到输入流中
        InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("db.properties");

        //用此类型加载流文件
        Properties p = new Properties();
        try {
            p.load(is);
            driver = p.getProperty("driver");
            url = p.getProperty("url");
            username = p.getProperty("username");
            password = p.getProperty("password");
            //加载驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获得连接对象的方法,用此方法连接数据库
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;        //有异常也有返回值
    }

    //关闭连接和释放资源的方法
    public static void close(Connection conn, Statement statement, ResultSet result,CallableStatement callableStatement){
        try {
            if(result!=null){
                result.close();
                result = null;
            }
            if(statement!=null){
                statement.close();
                statement = null;
            }
            if(conn!=null){
                conn.close();
                conn = null;
            }
            if(callableStatement!=null){
                callableStatement.close();
                callableStatement=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
