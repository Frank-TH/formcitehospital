package repositorio;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class SQLserver implements Idatabase {

    Connection conexion = null;
    private static SQLserver instancia;

    /*Ejecutar peticiones a la base de datos*/
    Statement st;
    ResultSet resultado;

    private SQLserver() {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost:3306/hospital";
        String usuario = "root";
        String clave = "";

        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, usuario, clave);
            System.out.println("La Conexion ha sido un exito!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error de Conexion! \n" + e);
        }
    }

    public static SQLserver getInstancia() {

        if (instancia == null) {
            return instancia = new SQLserver();
        }
        return instancia;
    }

    public void Desconectar() {

        try {
            st.close();
            resultado.close();
            conexion.close();
            System.out.println("Se desconecto Correctamente de la BD");

        } catch (SQLException e) {
            System.out.println("Hubo Error \n" + e);
        }
    }

//Obtener un solo dato de mi base de datos
    public String ObtenerDato(String sql, byte columna) throws SQLException {

        String dato = "";

        try {
            SQLserver.getInstancia();
            st = (Statement) conexion.createStatement();
            resultado = st.executeQuery(sql);

            while (resultado.next()) {
                dato = resultado.getString(columna);
            }
        } catch (SQLException e) {
        }

        return dato;
    }

    public ArrayList<String> PoblarComboBox(String sql) throws SQLException {
        ArrayList<String> datos = new ArrayList<>();

        try {
            SQLserver.getInstancia();
            st = (Statement) conexion.createStatement();
            resultado = st.executeQuery(sql);

            while (resultado.next()) {
                datos.add(resultado.getString(1));
            }
        } catch (SQLException e) {
        }

        return datos;
    }

//Modelando la tabla medico 
    public void tablaMedico(DefaultTableModel model, String opcion) throws SQLException {

        try {
            SQLserver.getInstancia();
            String sql = "select m.codigo, m.nombre from medico m inner join especialidad e on m.especialidad = e.codigo where e.nombre = '" + opcion + "'";
            st = (Statement) conexion.createStatement();
            resultado = st.executeQuery(sql);
            while (resultado.next()) {
                Object[] fila = {resultado.getString(1), resultado.getString(2)};
                model.addRow(fila);
            }
        } catch (SQLException e) {
        }

    }

    //Modelando la tabla horario
    public void tablaHorario(DefaultTableModel model, String codigo) throws SQLException {

        try {
            SQLserver.getInstancia();
            String sql = "select h.dia, h.turno, h.intervalo from medico m inner join horario h on m.horario = h.codigo where m.codigo = '" + codigo + "'";
            st = (Statement) conexion.createStatement();
            resultado = st.executeQuery(sql);
            while (resultado.next()) {
                Object[] fila = {resultado.getString(1), resultado.getString(2), resultado.getString(3)};
                model.addRow(fila);
            }
        } catch (SQLException e) {
        }

    }

}
