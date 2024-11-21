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

public class LoginServlet extends HttpServlet {

    public static String username;
    public static int cartno = 1;
    public static int prevcart = 1;

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

        String user = request.getParameter("username");
        String password = request.getParameter("password");
        Connection con;
        con = LoginServlet.con();

        try (Statement st = con.createStatement()) {

            String Find = "select * from userpass where UserName = '" + user + "' AND Password = '" + password + "'";
            ResultSet res = st.executeQuery(Find);

            if (res.next()) {

                if (tableExists(con, user)) {
                    String select = "SELECT MAX(CartNum) AS lastcart FROM " + user + " ";
                    ResultSet set = st.executeQuery(select);

                    if (set.next()) {
                        cartno = set.getInt("lastcart");
                        prevcart = cartno;
                        cartno++;
                    }
                }
                username = user;
                response.sendRedirect("Menu.html");
            } else {
                response.setContentType("text/html");

                PrintWriter out = response.getWriter();

                out.println("<html><body>");
                out.println("<script type='text/javascript'>");
                out.println("alert('Wrong USERNAME or PASSWORD!!'); window.location.href = 'index.html';");
                out.println("</script>");
                out.println("</body></html>");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
