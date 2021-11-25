import iesinfantaelena.dao.Cafes;
import iesinfantaelena.dao.Libros;
import iesinfantaelena.modelo.AccesoDatosException;
import iesinfantaelena.modelo.Libro;

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
		    System.out.println("\n *************Libros***********\n");
		    Libros libros = new Libros();
		    //Prueba isertar libros
		    libros.anadirLibro(new Libro(12345, "Sistemas Operativos", "Tanembaun", "Informática", 156,3));
		    libros.anadirLibro(new Libro(12453, "Minix", "Stallings", "Informática", 345,4));
		    libros.anadirLibro(new Libro(1325, "Linux", "Richard Stallman", "FSF", 168,10));
		    libros.anadirLibro(new Libro(1725, "Java", "Juan Garcia", "Programación", 245,9));
			} catch (AccesoDatosException e) {
				e.printStackTrace();
			}
		}

}