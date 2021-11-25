package iesinfantaelena.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	private static final String UPDATE_COPIAS_LIBRO = "update LIBROS set copias=? where isbn=?";
	private static final String SELECT_ALL_LIBROS = "select * from LIBROS";
	private static final String SELECT_LIBRO = "select * from LIBROS where isbn=?";
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
	
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();
		try {
			//Obtenemos resultset con las columnas a partir de la consulta
			stmt = con.createStatement();
			rs = stmt.executeQuery(SELECT_ALL_LIBROS);
			//recorremos el resultset para agregar los libros a la lista
			while(rs.next()) {
				int isbn = rs.getInt(1);
				String titulo = rs.getString(2);
				String autor = rs.getString(3);
				String editorial = rs.getString(4);
				int paginas = rs.getInt(5);
				int copias = rs.getInt(6);
				
				listaLibros.add(new Libro(isbn,titulo,autor,editorial,paginas,copias));
			}
		}catch (SQLException e) {
			Utilidades.printSQLException(e);
			throw new AccesoDatosException("Error al recuperar la tabla LIBROS");
		}finally {
			liberar();
		}
		return listaLibros;

	}

    /**
     * Actualiza el numero de copias para un libro
     * @param isbn
     * @param copias
     * @throws AccesoDatosException
     */
	
	public void actualizarCopias(Libro libro) throws AccesoDatosException {
		
		try {
			//Preparacion de la actualizacion
			pstmt = con.prepareStatement(UPDATE_COPIAS_LIBRO);
			pstmt.setInt(1, libro.getCopias());
			pstmt.setInt(2, libro.getISBN());
			//Ejecucion de la actualizacion
			int rowsAffected = pstmt.executeUpdate();
			if(rowsAffected>0)
				System.out.println("\nlas copias del libro: "+libro.getTitulo()+" se actualizaron correctamente");
		}catch (SQLException e) {
			Utilidades.printSQLException(e);
			Utilidades.printSQLException(e);
			throw new AccesoDatosException("Error al modificar las copias del libro: "+libro.getTitulo());
		}finally {
			liberar();
		}
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
				System.out.println("\nEl libro "+libro.getTitulo()+" se inserto correctamente");
		}catch (SQLException e) {
			Utilidades.printSQLException(e);
			throw new AccesoDatosException("Error al insertar el libro: "+libro.getTitulo()) ;
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
		
		try {
			//Se establece el libro a borrar
			pstmt = con.prepareStatement(DELETE_LIBRO);
			pstmt.setInt(1, libro.getISBN());
			//Se ejecuta el borrado
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected>0)
				System.out.println("\nEl libro: "+libro.getTitulo()+" se ha BORRADO con exito");
		} catch (SQLException e) {
			Utilidades.printSQLException(e);
			throw new AccesoDatosException("Error al borrar el libro: "+libro.getTitulo());
		}finally {
			liberar();
		}
		
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
		try {
			//Obtenemos resultset con las columnas del libro con el ISBN pasado por parametro
			pstmt = con.prepareStatement(SELECT_LIBRO);
			pstmt.setInt(1, ISBN);
			rs = pstmt.executeQuery();
			//recorremos el resultset para agregar los libros a la lista
			while(rs.next()) {
				int isbn = rs.getInt(1);
				String titulo = rs.getString(2);
				String autor = rs.getString(3);
				String editorial = rs.getString(4);
				int paginas = rs.getInt(5);
				int copias = rs.getInt(6);
				//Mostramos la informacion obtenida
				System.out.println("\nInformacion del libro buscado con ISBN: "+ISBN);
				System.out.println(new Libro(isbn,titulo,autor,editorial,paginas,copias));
			}
		}catch (SQLException e) {
			Utilidades.printSQLException(e);
			throw new AccesoDatosException("\nNo se ha podido recuperar la informacion del libro con ISBN: "+ISBN);
		}finally {
			liberar();
		}
	}
}

