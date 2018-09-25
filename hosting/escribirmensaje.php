<?php

if(empty($_REQUEST["envio"]) || empty($_REQUEST["receptor"]))
	echo "Error";
else{
	$envio = $_REQUEST["envio"];
	$receptor = $_REQUEST["receptor"];
	$mensaje = $_REQUEST["mensaje"];

	require("db_config.php");

	$conexion = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error al conectar con la BD");

	$consultaSQL = "INSERT INTO conversaciones (id, envio, receptor, mensaje) VALUES (NULL, '".$envio."', '".$receptor."', '".$mensaje."');";

	$resultadoConsulta = mysqli_query($conexion, $consultaSQL);
	mysqli_close($conexion) or die("Error al cerrar la conexión");

	if (!$resultadoConsulta)
		echo "Error al enviar el mensaje";
	else
		echo "Enviado!";
}
?>