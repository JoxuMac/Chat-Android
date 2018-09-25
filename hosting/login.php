<?php
if(empty($_REQUEST["usuario"]))
	echo "Inserta un usuario";
else{
	if(empty($_REQUEST["password"]))
		echo "Inserta una contraseña";
	else {
		$usuario = $_REQUEST["usuario"];
		$password = $_REQUEST["password"];

		require("db_config.php");
		$conexion = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error al conectar con la BD");

		$SQL = "SELECT * FROM `usuarios` WHERE usuario ='".$usuario."' AND password ='".$password."'";
		$resultado = mysqli_query($conexion, $SQL) or die ("Error al realizar la consulta");
		if (mysqli_num_rows($resultado) > 0) {
			$fila = mysqli_fetch_array($resultado);
			echo $fila["id"];
		} else {
			echo "0";
		}

		mysqli_close($conexion) or die("Error al cerrar la conexión");
	}	
}
?>