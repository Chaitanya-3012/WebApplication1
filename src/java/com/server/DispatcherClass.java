package com.server;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherClass extends HttpServlet {

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int total = 0;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><style>");
        out.println("table { margin: 0 auto; border-collapse: collapse; width: 90%; font-size: 18px; }");
        out.println("th, td { border: 1px solid black; padding: 10px; text-align: center; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("h2 { background-color: #f2f2f2;text-align:center;font-size:48px; }");
        out.println("</style></head><body>");
        out.println("<h2>CART</h2>");
        out.println("<table border='1'><tr><th>Product</th><th>Price</th><th>Quantity</th></tr>");

        try (Connection conn = DispatcherClass.con()) {
            Statement st = conn.createStatement();
            String dispatch = "SELECT * FROM "+LoginServlet.username+" WHERE CartNum = "+LoginServlet.cartno;
            ResultSet rs = st.executeQuery(dispatch);

            while (rs.next()) {
                out.println("<tr><td> " + rs.getString("Product") + " </td>");
                out.println("<td> " + rs.getInt("Price") + " </td>");
                out.println("<td> " + rs.getInt("Quantity") + " </td></tr>");
            }
            rs = st.executeQuery("select sum(Quantity * Price) as total from "+LoginServlet.username+" where CartNum = "+LoginServlet.cartno);
            if (rs.next()) {
                total = rs.getInt("total");
            }
            out.println("<tr><td colspan='2'><b>Total</b></td><td><b>" + total + "</b></td></tr>");
            out.println("</table>");
            out.println("</body></html>");
        } catch (SQLException e) {
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
