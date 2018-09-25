<?php

if(empty($_REQUEST["envio"]) || empty($_REQUEST["receptor"]))
	echo "Error";
else{
	$envio = $_REQUEST["envio"];
	$receptor = $_REQUEST["receptor"];

	require("db_config.php");

	$conexion = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error al conectar con la BD");

	$consultaSQL = "SELECT mensaje, envio, receptor FROM conversaciones WHERE (envio = '".$envio."' AND receptor = '".$receptor."' ) OR (receptor = '".$envio."' AND envio = '".$receptor."' ) ORDER BY id ASC;";

	$resultadoConsulta = mysqli_query($conexion, $consultaSQL) or die ("Error al realizar la consulta");
	if (mysqli_num_rows($resultadoConsulta) > 0)
	{
		$respuestaJSON["mensajes"] = array();
		while ($fila = mysqli_fetch_array($resultadoConsulta))
		{
			$mensaje = array();
			$mensaje["envio"] = $fila["envio"];
			$mensaje["receptor"] = $fila["receptor"];
			$mensaje["mensaje"] = $fila["mensaje"];
			array_push($respuestaJSON["mensajes"], $mensaje);
		}
		echo json_encode($respuestaJSON);
	}
	mysqli_close($conexion) or die("Error al cerrar la conexión");
}
?>
