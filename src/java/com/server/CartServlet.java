package com.server;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CartServlet extends HttpServlet {

    public static Connection con() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/website", "root", "admin");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
        return (conn);
    }

    public static boolean tableExists(Connection con, String tableName) throws SQLException {
        DatabaseMetaData meta = con.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String product = request.getParameter("product");
        int price = Integer.parseInt(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Connection con;
        con = CartServlet.con();

        try (Statement st = con.createStatement()) {
            if (tableExists(con,  LoginServlet.username)) {

                String Insert = "insert into " + LoginServlet.username + "(CartNum,Product,Price,Quantity)values('" + LoginServlet.cartno + "','" + product + "','" + price + "','" + quantity + "')";
                st.executeUpdate(Insert);
                response.setContentType("text/html");

                PrintWriter out = response.getWriter();

                out.println("<html><body>");
                out.println("<script type='text/javascript'>");
                out.println("alert('Added to CART!!'); window.location.href = 'Menu.html';");
                out.println("</script>");
                out.println("</body></html>");
            } else {

                String create = "CREATE TABLE " + LoginServlet.username + "(`CartNum` INT(10) NOT NULL,`Product` TEXT NOT NULL,`Price` INT(11) NOT NULL,`Quantity` INT(11) NOT NULL)";
                st.executeUpdate(create);

                String Insert = "insert into " + LoginServlet.username + "(CartNum,Product,Price,Quantity)values('" + LoginServlet.cartno + "','" + product + "','" + price + "','" + quantity + "')";
                st.executeUpdate(Insert);
                response.setContentType("text/html");

                PrintWriter out = response.getWriter();

                out.println("<html><body>");
                out.println("<script type='text/javascript'>");
                out.println("alert('Added to CART!!'); window.location.href = 'Menu.html';");
                out.println("</script>");
                out.println("</body></html>");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.print("<h1>Your Cart</h1>");
    }

}
