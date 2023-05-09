package com.emergentes.controlador;

import com.emergentes.modelo.Libro;
import com.emergentes.utiles.ConexionDB;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //opciones
            String op;
            op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
            //referecias del libro
            ArrayList<Libro> lista = new ArrayList<Libro>();
            //para obtner un objeto en la libreriaque teiene ocnexion a la base de datos 
            ConexionDB canal = new ConexionDB();
            //
            Connection conn = canal.conectar();
            //para las diferentes consultas
            PreparedStatement ps;
            //para los resultados 
            ResultSet rs;

            if (op.equals("list")) {
                //para obtener la lista de registros
                String sql = "select*from libros";
                //preparamos la sentencia
                ps = conn.prepareStatement(sql);
                //leemos
                rs = ps.executeQuery();

                while (rs.next()) {
                    Libro lib = new Libro();
                    //VAMOS A COLOCAR LOS DATOS OPTENEIDOS 
                    lib.setId(rs.getInt("id"));
                    lib.setIsbn(rs.getString("Isbn"));
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setCategoria(rs.getString("categoria"));

                    lista.add(lib);
                }
                request.setAttribute("lista", lista);
                //envia al index.jsp para mostrar la informacion
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
            if (op.equals("nuevo")) {
                //instanciar un objeto de la clase libro 
                Libro li = new Libro();
                //el objeto se pone como atribute de request
                request.setAttribute("lib", li);
                //redireccionar a editar.jsp
                request.getRequestDispatcher("editar.jsp").forward(request, response);
            }
            if (op.equals("eliminar")) {
                //obtener el id
                int id = Integer.parseInt(request.getParameter("id"));
                //realizar la eliminacion en la base de datos
                String sql = "delete from libros where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                //redireccionar a maincontroller
                response.sendRedirect("MainController");
            }

            if (op.equals("modificar")) {
                //obtener el id
                int id = Integer.parseInt(request.getParameter("id"));
                String sql = "select*from libros where id=?";
                //preparamos la sentencia
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                //leemos
                rs = ps.executeQuery();

                if (rs.next()) {
                    Libro lib = new Libro();
                    //VAMOS A COLOCAR LOS DATOS OPTENEIDOS 

                    lib.setId(rs.getInt("id"));
                    lib.setIsbn(rs.getString("Isbn"));
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setCategoria(rs.getString("categoria"));
                    request.setAttribute("lib", lib);

                }
                //envia al index.jsp para mostrar la informacion
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                response.sendRedirect("MainController");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            int id = Integer.parseInt(request.getParameter("id"));
            //      System.out.println("Valor de ID " + id);
            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("Titulo");
            String categoria = request.getParameter("categoria");

            Libro lib = new Libro();

            lib.setIsbn(isbn);
            lib.setId(id);
            lib.setTitulo(titulo);
            lib.setCategoria(categoria);

            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();

            PreparedStatement ps;

            if (id == 0) {
                String sql = "insert Into libros (isbn,titulo,categoria) values (?,?,?)";

                ps = conn.prepareStatement(sql);
                ps.setString(1, lib.getIsbn());
                ps.setString(2, lib.getTitulo());
                ps.setString(3, lib.getCategoria());

                ps.executeUpdate();

                response.sendRedirect("MainController");

            } else {
                JOptionPane.showMessageDialog(null,id);
                String sql = "update libros set isbn=? ,titulo=?,categoria=? where id = ?";

                ps = conn.prepareStatement(sql);
                ps.setString(1, lib.getIsbn());
               

                ps.setString(2, lib.getTitulo());
                ps.setString(3, lib.getCategoria());
                ps.setInt(4, id);
                ps.executeUpdate();

                response.sendRedirect("MainController");
            }

        } catch (SQLException ex) {
            System.out.println("Error en SQL" + ex.getMessage());

        }
    }
}
