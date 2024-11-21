package com.server;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignUp extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String user = request.getParameter("username");
        String password = request.getParameter("password");
        Connection con;
        con = SignUp.con();

        try (Statement st = con.createStatement()) {

            String Insert = "insert into userpass(UserName,Password)values('" + user + "','" + password + "')";
            st.executeUpdate(Insert);
            
            response.sendRedirect("index.html");
        } catch (SQLException ex) {
            Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
