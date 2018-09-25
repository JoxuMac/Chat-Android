<?php

if(empty($_REQUEST["usuario"]))
	echo "Error";
else{
	$usu = $_REQUEST["usuario"];

	require("db_config.php");

	$conexion = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error al conectar con la BD");
	
	$consultaSQL = "SELECT usuarios.usuario, conversaciones.mensaje, conversaciones.envio, conversaciones.receptor FROM usuarios, conversaciones WHERE (envio = '".$usu."'  AND receptor = usuarios.id ) OR( receptor = '".$usu."' AND envio = usuarios.id) GROUP BY envio UNION SELECT usuarios.usuario, conversaciones.mensaje, conversaciones.envio, conversaciones.receptor FROM usuarios, conversaciones WHERE (envio = '".$usu."'  AND receptor = usuarios.id ) OR( receptor = '".$usu."' AND envio = usuarios.id) GROUP BY receptor;";

	$resultadoConsulta = mysqli_query($conexion, $consultaSQL) or die ("Error al realizar la consulta");
	if (mysqli_num_rows($resultadoConsulta) > 0)
	{
		$respuestaJSON["conversaciones"] = array();
		while ($fila = mysqli_fetch_array($resultadoConsulta))
		{
			$contacto = array();
			$contacto["usuario"] = $fila["usuario"];
			$contacto["mensaje"] = $fila["mensaje"];
			$contacto["envio"] = $fila["envio"];
			$contacto["receptor"] = $fila["receptor"];
			array_push($respuestaJSON["conversaciones"], $contacto);
		}
		echo json_encode($respuestaJSON);
	}
	mysqli_close($conexion) or die("Error al cerrar la conexión");
}
?>
