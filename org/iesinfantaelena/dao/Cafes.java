package iesinfantaelena.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import iesinfantaelena.modelo.AccesoDatosException;
import iesinfantaelena.utils.Utilidades;

/**
 * @descrition
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Cafes   {
	//Atributos de clase, ejercicio 2.A
  	private static Connection con;
	private static ResultSet rs;
	private static PreparedStatement ps;
	private static Statement stmt;
	// Consultas a realizar en BD
	private static final String SELECT_CAFES_QUERY = "select CAF_NOMBRE, PROV_ID, PRECIO, VENTAS, TOTAL from CAFES";
	private static final String SEARCH_CAFE_QUERY = "select * from CAFES WHERE CAF_NOMBRE= ?";
	private static final String INSERT_CAFE_QUERY = "insert into CAFES values (?,?,?,?,?)";
	private static final String DELETE_CAFE_QUERY = "delete from CAFES WHERE CAF_NOMBRE = ?";
	private static final String SEARCH_CAFES_PROVEEDOR = "select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID";

    private static final String CREATE_TABLE_PROVEEDORES ="create table if not exists proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));";

    private static final String CREATE_TABLE_CAFES ="create table if not exists CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));";

    	/**
	 * Constructor: inicializa conexión
	 * 
	 * @throws AccesoDatosException
	 */
	
	public Cafes() {
		
		//Se inicializan los atribuntos de CLASE a null ejercicio 2.B 
		
		Cafes.stmt = null;
		Cafes.ps = null;
		Cafes.rs = null;
			
		try {
			con = new Utilidades().getConnection();//Se establece conexion 2.B
	        
			stmt = con.createStatement(); 
	
	        stmt.executeUpdate(CREATE_TABLE_PROVEEDORES);       
	        stmt.executeUpdate(CREATE_TABLE_CAFES);  
	
	        stmt.executeUpdate("insert into proveedores values(49, 'PROVerior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');");
	        stmt.executeUpdate("insert into proveedores values(101, 'Acme, Inc.', '99 mercado CALLE', 'Groundsville', 'CA', '95199');");
	        stmt.executeUpdate("insert into proveedores values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');");
			
			} catch (IOException e) {
				// Error al leer propiedades
				// En una aplicación real, escribo en el log y delego
				System.err.println(e.getMessage());
			} catch (SQLException sqle) {
				Utilidades.printSQLException(sqle);
			} finally {
				liberarRecursos();
			}
	}

		/**
		 * Metodo que muestra por pantalla los datos de la tabla cafes
		 * 
		 * @param con
		 * @throws SQLException
		 */
		public void verTabla() throws AccesoDatosException {
	
			try {			
				// Creación de la sentencia
				stmt = con.createStatement();
				// Ejecución de la consulta y obtención de resultados en un ResultSet
				rs = stmt.executeQuery(SELECT_CAFES_QUERY);
				// Recuperación de los datos del ResultSet
				while (rs.next()) {
					String coffeeName = rs.getString("CAF_NOMBRE");
					int supplierID = rs.getInt("PROV_ID");
					float PRECIO = rs.getFloat("PRECIO");
					int VENTAS = rs.getInt("VENTAS");
					int total = rs.getInt("TOTAL");
					System.out.println(coffeeName + ", " + supplierID + ", "
							+ PRECIO + ", " + VENTAS + ", " + total);
				}
	
			} catch (SQLException sqle) {
				// En una aplicación real, escribo en el log y delego
				Utilidades.printSQLException(sqle);
				throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
			} finally {
				liberarRecursos();
			}

		}

		/**
		 * Mótodo que busca un cafe por nombre y muestra sus datos
		 *
		 * @param nombre
		 */
		public void buscar(String nombre) throws AccesoDatosException {
		
			try {			
				// Creación de la sentencia
				ps = con.prepareStatement(SEARCH_CAFE_QUERY);
				ps.setString(1, nombre);
				// Ejecución de la consulta y obtención de resultados en un
				// ResultSet
				rs = ps.executeQuery();
				// Recuperación de los datos del ResultSet
				if (rs.next()) {
					String coffeeName = rs.getString("CAF_NOMBRE");
					int supplierID = rs.getInt("PROV_ID");
					float PRECIO = rs.getFloat("PRECIO");
					int VENTAS = rs.getInt("VENTAS");
					int total = rs.getInt("TOTAL");
					System.out.println(coffeeName + ", " + supplierID + ", "
							+ PRECIO + ", " + VENTAS + ", " + total);
				}
		
			} catch (SQLException sqle) {
				// En una aplicación real, escribo en el log y delego
				Utilidades.printSQLException(sqle);
				throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
			} finally {
				liberarRecursos();
			}

		}

		/**
		 * Mótodo para insertar una fila
		 * 
		 * @param nombre
		 * @param provid
		 * @param precio
		 * @param ventas
		 * @param total
		 * @return
		 */
		public void insertar(String nombre, int provid, float precio, int ventas,
				int total) throws AccesoDatosException {

			try {
				ps = con.prepareStatement(INSERT_CAFE_QUERY);
				ps.setString(1, nombre);
				ps.setInt(2, provid);
				ps.setFloat(3, precio);
				ps.setInt(4, ventas);
				ps.setInt(5, total);
				// Ejecución de la inserción
				ps.executeUpdate();
			} catch (SQLException sqle) {
				// En una aplicación real, escribo en el log y delego
				Utilidades.printSQLException(sqle);
				throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
			} finally {
				liberarRecursos();
			}
		}

		/**
		 * Mótodo para borrar una fila dado un nombre de cafó
		 * 
		 * @param nombre
		 * @return
		 */
		public void borrar(String nombre) throws AccesoDatosException {
		
			try {
				// Creación de la sentencia
				ps = con.prepareStatement(DELETE_CAFE_QUERY);
				ps.setString(1, nombre);
				// Ejecución del borrado
				int rowsAffected = ps.executeUpdate();
				if (rowsAffected>0)
					System.out.println("café "+nombre+ " ha sido borrado.");
				
			} catch (SQLException sqle) {
				// En una aplicación real, escribo en el log y delego
				Utilidades.printSQLException(sqle);
				throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
			} finally {
				liberarRecursos();
			}
		}

		/**
		 * Mótodo que busca un cafe por nombre y muestra sus datos
		 *
		 * @param nombre
		 */
		public void cafesPorProveedor(int provid) throws AccesoDatosException {
		
			try {
				//Se establece conexion con la base de datos
				//con = new Utilidades().getConnection();
				// Creacion de la consulta con parametros
				ps = con.prepareStatement(SEARCH_CAFES_PROVEEDOR);
				ps.setInt(1, provid);
				//Obtencion de resultado de la query
				rs = ps.executeQuery();
				//Lectura de las diferentes columnas recuperadas
				while (rs.next()) {
					//info cafes
					String CAF_NOMBRE = rs.getString("CAF_NOMBRE");
					int PROV_ID = rs.getInt("PROV_ID");
					float PRECIO = rs.getFloat("PRECIO");
					int VENTAS = rs.getInt("VENTAS");
					int TOTAL = rs.getInt("TOTAL");
					//info proveedor
					String PROV_NOMBRE = rs.getString("PROV_NOMBRE");
					String CALLE = rs.getString("CALLE");
					String CIUDAD = rs.getString("CIUDAD");
					String PAIS = rs.getString("PAIS");
					int CP = rs.getInt("CP");
					//Muestra informacion obtenida de la base de datos
					String infoCafe =  CAF_NOMBRE+";"+ PROV_ID+ ";"+PRECIO+";"+VENTAS+";"+TOTAL;
					String infoProveedor = PROV_NOMBRE+";"+CALLE+";"+CIUDAD+";"+PAIS+";"+CP;
					System.out.println("\ninformacion del cafe: "+infoCafe+"\nInformacion proveedor: "+infoProveedor);
					
				}

			} catch (SQLException sqle) {
				
				Utilidades.printSQLException(sqle);
				throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
			} catch (Exception e) { //Errores imprevistos
				
				System.err.println(e.getMessage());
			}finally {
			//Libera todos los recursos
				liberarRecursos();
			}
		}
		
		public void cerrarConexion() { //Cierra conexion con base de datos Ejercicio 2.C
			Utilidades.closeConnection(Cafes.con);
		}
		
		public void liberarRecursos() { //Se liberan el resto de recursos Ejercicio 2.D
			 
				try {
					if (Cafes.rs != null) {
						Cafes.rs.close();
						Cafes.rs = null;
					}
					if (Cafes.ps != null) {
						Cafes.ps.close();
						Cafes.ps = null;
					}
					
					if (Cafes.stmt != null) {
						Cafes.stmt.close();
						Cafes.stmt = null;
					}
						
				} catch (SQLException e) {
					Utilidades.printSQLException(e);
				}
			
		}
}
