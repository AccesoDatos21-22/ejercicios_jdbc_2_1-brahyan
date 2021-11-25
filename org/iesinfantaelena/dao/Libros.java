package iesinfantaelena.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import iesinfantaelena.utils.Utilidades;
import iesinfantaelena.modelo.*;




/**
 * @descrition
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Libros {

	// Consultas a realizar en BD
	private static final String CREATE_TABLE_LIBROS = "create table if not exists LIBROS "
			+ "(isbn integer not null, titulo varchar(50) not null, autor varchar(50) not null,"
			+ " editorial varchar(25) not null, paginas integer not null, copias integer not null,"
			+ " constraint isbn_pk primary key (isbn));";
	private static final String INSERT_LIBRO = "insert into LIBROS values (?,?,?,?,?,?)";
	private static final String DELETE_LIBRO = "delete from LIBROS where isbn=?";
	//Variables de instancia
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	private PreparedStatement pstmt;

	/**
	 * Constructor: inicializa conexión
	 * 
	 * @throws AccesoDatosException
	 */
	
	public Libros() throws AccesoDatosException {
		try {
			// Obtenemos la conexión
			this.con = new Utilidades(System.getProperty("user.dir") + "/resources/sqlite-properties.xml").getConnection();
			this.rs = null; this.pstmt = null;
			this.stmt = con.createStatement();
			stmt.executeUpdate(CREATE_TABLE_LIBROS);
		} catch (IOException e) {
			// Error al leer propiedades, En una aplicación real, escribo en el log y delego
			System.err.println(e.getMessage());
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		}finally {
			liberar();//liberamos recursos
		}
	}

	/**
	 * Método para cerrar la conexión
	 * 
	 * @throws AccesoDatosException
	 */
	public void cerrar() {
		Utilidades.closeConnection(con);//El metodod ya comprueba que sea nula
	}

	
	/**
	 * Método para liberar recursos
	 * 
	 * @throws AccesoDatosException
	 */
	private void liberar() {
		try {
			// Liberamos todos los recursos pase lo que pase
			//Al cerrar un stmt se cierran los resultset asociados. Podíamos omitir el primer if. Lo dejamos por claridad.
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}			
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log, no delego porque
			// es error al liberar recursos
			Utilidades.printSQLException(sqle);
		}
	}

	/**
	 * Metodo que muestra por pantalla los datos de la tabla cafes
	 * 
	 * @param con
	 * @throws SQLException
	 */
	
	public List<Libro> verCatalogo() throws AccesoDatosException {
	
		return null;

	}

    /**
     * Actualiza el numero de copias para un libro
     * @param isbn
     * @param copias
     * @throws AccesoDatosException
     */
	
	public void actualizarCopias(Libro libro) throws AccesoDatosException {
		
	}

	
    /**
     * Añade un nuevo libro a la BD
     * @param isbn
     * @param titulo
     * @param autor
     * @param editorial
     * @param paginas
     * @param copias
     * @throws AccesoDatosException
     */
	public void anadirLibro(Libro libro) throws AccesoDatosException {
		
		try {
			//Preparacion de la insercion
			pstmt = con.prepareStatement(INSERT_LIBRO);
			pstmt.setInt(1, libro.getISBN());
			pstmt.setString(2, libro.getTitulo());
			pstmt.setString(3, libro.getAutor());
			pstmt.setString(4, libro.getEditorial());
			pstmt.setInt(5, libro.getPaginas());
			pstmt.setInt(6, libro.getCopias());
			
			//Ejecucion de la insercion
			int rowAffected = pstmt.executeUpdate();
			//Si se realiza insercion se informa al usuario
			if(rowAffected>0)
				System.out.println("El libro "+libro.getTitulo()+" se inserto correctamente");
		}catch (SQLException e) {
			Utilidades.printSQLException(e);
			throw new AccesoDatosException("Error al insertar libro") ;
		}finally {
			liberar();
		}
	
	}

	/**
	 * Borra un libro por ISBN
	 * @param isbn
	 * @throws AccesoDatosException
	 */

	public void borrar(Libro libro) throws AccesoDatosException {
		
		
	}
	
	/**
	 * Devulve los nombres de los campos de BD
	 * @return
	 * @throws AccesoDatosException
	 */

	public String[] getCamposLibro() throws AccesoDatosException {
       
    return null;
	}


	public void obtenerLibro(int ISBN) throws AccesoDatosException {
		
	}




}

