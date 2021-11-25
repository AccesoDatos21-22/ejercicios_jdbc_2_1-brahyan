import iesinfantaelena.dao.Cafes;
import iesinfantaelena.dao.Libros;
import iesinfantaelena.modelo.AccesoDatosException;
import iesinfantaelena.utils.Utilidades;

public class Main {

	public static void main(String[] args) {
	
		
		try {
			
			Cafes cafes = new Cafes();
		    cafes.insertar("Cafetito", 150, 1.0f, 100,1000);
		    cafes.insertar("Cafe tacilla", 150, 2.0f, 100,1000);
		    cafes.verTabla();
		    cafes.buscar("tacilla");
		    cafes.cafesPorProveedor(150);
		    cafes.borrar("Cafe tacilla");
		    cafes.verTabla();
		    
		    cafes.cerrarConexion();//Se cierra la conexion
		    
		    //Prueba conexion sqlite
		    Libros libro = new Libros();
		
			} catch (AccesoDatosException e) {
				e.printStackTrace();
			}
		}

}