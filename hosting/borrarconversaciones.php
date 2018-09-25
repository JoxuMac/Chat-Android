<?php

if(empty($_REQUEST["usuario1"]) || empty($_REQUEST["usuario2"]))
	echo "Error";
else{
	$usuario1 = $_REQUEST["usuario1"];
	$usuario2 = $_REQUEST["usuario2"];

	require("db_config.php");

	$conexion = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error al conectar con la BD");

	$consultaSQL = "DELETE FROM conversaciones WHERE (envio = '".$usuario1."' AND receptor = '".$usuario2."' ) OR (receptor = '".$usuario1."' AND envio = '".$usuario2."' ) ;";

	$resultadoConsulta = mysqli_query($conexion, $consultaSQL);
	mysqli_close($conexion) or die("Error al cerrar la conexión");

	if ($resultadoConsulta)
		echo "Conversacion Borrada!";
	else
		echo "Error al Borrar la Conversacion!";
}
?> 
