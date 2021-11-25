[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=6320410&assignment_repo_type=AssignmentRepo)

##Ejercicio 2.F
El metodo liberarRecursos lo llamamos al final de cada método que realiaza escritura, lectura, actualización o borrado en la base de datos
en concreto en la clausula finally.
El metodo cerrarConexion se llama una vez se han realizado todas operaciones deseadas en la base de datos.
Es decir se abre una conexión al instaciar la clase Cafes, y se cierra llamando al método cerrarConexión cuando ya no se qiren hacer
más operaciones con la base de datos.

##Ejercicio 2.G
Esta nueva versión es menos propensa a fallos, ya que el hacer estaticos los objetos necesarios para interactuar con la base de datos
no se tienen que manipular directamente desde cada método, y es más dificil que se quede algún recurso abierto o que empiecen la ejecución 
de algun método teniendo algún valor distinto de null.

No creo que esta nueva versión presente ningún inconveniente respecto a la versión anterior, ya que el código queda algo más modularizado,
más limpio, y más corto y legible.