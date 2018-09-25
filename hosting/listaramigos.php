<?php

if(empty($_REQUEST["usuario"]))
	echo "Error";
else{
	$usu = $_REQUEST["usuario"];

	require("db_config.php");

	$conexion = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error al conectar con la BD");

	$consultaSQL = "SELECT usuarios.usuario, usuarios.telefono, usuarios.foto, usuarios.id FROM usuarios , amigos WHERE (amigo_1 = '".$usu."' AND amigo_2 = id ) OR (amigo_2 = '".$usu."' AND amigo_1 = id ) ORDER BY usuario ASC;";

	$resultadoConsulta = mysqli_query($conexion, $consultaSQL) or die ("Error al realizar la consulta");
	if (mysqli_num_rows($resultadoConsulta) > 0)
	{
		$respuestaJSON["contactos"] = array();
		while ($fila = mysqli_fetch_array($resultadoConsulta))
		{
			$contacto = array();
			$contacto["usuario"] = $fila["usuario"];
			$contacto["telefono"] = $fila["telefono"];
			$contacto["foto"] = $fila["foto"];
			$contacto["id"] = $fila["id"];
			array_push($respuestaJSON["contactos"], $contacto);
		}
		echo json_encode($respuestaJSON);
	}
	mysqli_close($conexion) or die("Error al cerrar la conexión");
}
?>
