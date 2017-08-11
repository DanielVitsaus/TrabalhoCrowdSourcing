<?php 
	// database class including
include "dbAccess.php";
//include "RUUser.class.php";
include "errorHandling.php";

ini_set('display_errors',1);
ini_set('display_startup_erros',1);
error_reporting(E_ALL);

		switch ($_SERVER ['REQUEST_METHOD']) 
		{
			case 'GET' :
				
				if(isset($_GET['req']))
				{
					$req = $_GET['req'];
					$key = $_GET['key'];
					if($key == 'app' )
					{
						//REQUISIÇÃO DADOS DOS VIDEOS
						if($req == '1')
						{
							$resposta = processRequestDataVideo();
					 		echo json_encode ($resposta, JSON_FORCE_OBJECT );
					 	}
					 	//requisição dados para montar o mapa de calor
					 	if($req =='2')
					 	{
					 		$resposta = processDataBD();
					 		//echo json_encode ($resposta, JSON_FORCE_OBJECT );
					 		echo $resposta;

					 	}
					 	if($req == '3')
					 	{
					 		

						 	$dadosConexao['macAP'] = $_GET['bssid'];
						 	$dadosConexao['linkSpeed'] = $_GET['linkSpeed'];
						 	$dadosConexao['rede'] = $_GET['ssid'];
							$dadosConexao['lat'] = $_GET['latitude'];
							$dadosConexao['macCliente'] = $_GET['macAddress'];
							$dadosConexao['frequencia'] = $_GET['frequency'];
							$dadosConexao['potencia'] = $_GET['rssi'];
							$dadosConexao['long'] = $_GET['longetude'];
							$dadosConexao['qualidade'] = $_GET['rssiLevel'];
							$dadosConexao['ipCliente'] = $_GET['ipAddress'];
							$dadosConexao['key'] = $_GET['key'];
							
							if($_GET['key'] == 'app')
							{
								$resposta = processInsertConectionData($dadosConexao);
								if($resposta)
								{
									echo 'dados enviados';
								}
								else
								{
									echo 'erroooo';
								}
					
							}
							else
							{
								header("HTTP/1.0 401 Not authorized");
								echo '401 Not authorized';
							}

					 	 }

					}
					else
					{
						header("HTTP/1.0 401 Not authorized");
						echo '401 Not authorized';
					}
				 }

				break;
		 }




		//função que retorna dados dos vídeos
		function processRequestDataVideo()
		{
			$dados['code'] = '200';
			$dados['nome'] = 'RedBullPlayStreets';
		  	$caminho ='http://mconfdev.ufjf.br/aplicativo/mpds/redbull_2sec.mpd';
		 	$dados['mpd'] =  "$caminho";
			return $dados;
		}

		function processInsertConectionData($dadosConexao)
		{
			
			$macCliente = $dadosConexao['macCliente'];
			$macAP = $dadosConexao['macAP'];
			$qualidade = $dadosConexao['qualidade'];
			$lat = $dadosConexao['lat'];
			$long = $dadosConexao['long'];
			$ls = $dadosConexao['linkSpeed'];
			$rede = $dadosConexao['rede'];
			$frequencia = $dadosConexao['frequencia'];
			$pot = $dadosConexao['potencia'];
			$ipCliente = $dadosConexao['ipCliente'];


			$db = new dbAccess (); // database global variable
	 	 	$db->connect (); // trying to connect with values defined in class
			$dbh = $db->getDatabaseHandler();
			$sql = "SELECT * FROM cliente WHERE mac = \"$macCliente\"";
			
			$stmt = $dbh->prepare ( $sql );
			$ok = $stmt->execute ();
			$consultaCliente = $stmt->RowCount();
			if($consultaCliente == 0)//cliente nao registrado
			{
				//registra cliente
				$dbh->beginTransaction();
				$sql = "INSERT INTO cliente (mac) VALUES (\"$macCliente\")";
				$stmt = $dbh->prepare ( $sql );
				$ok = $stmt->execute ();
				if($ok)
				{
					$dbh->commit();
				}
				
			}

			$sql = "SELECT * FROM ap WHERE mac = \"$macAP\"";
			$stmt = $dbh->prepare ( $sql );
			$ok = $stmt->execute ();
			$consultaAP = $stmt->RowCount();
			
			if($consultaAP == 0)//AP nao registrado
			{

				//registra AP
				$dbh->beginTransaction();
				$sql = "INSERT INTO ap (mac) VALUES (\"$macAP\")";
				$stmt = $dbh->prepare ( $sql );
				$ok = $stmt->execute ();
				if($ok)
				{
					$dbh->commit();
				}
			}
			//////////////////////////////////////////////
			/////////////pegar id's ap e cliente//////////
			//////////////////////////////////////////////
			
			$sql1 = "SELECT idCliente FROM cliente WHERE cliente.mac = \"$macCliente\"";
			$sql2 = "SELECT idAP FROM ap WHERE ap.mac = \"$macAP\"";
			$stmt = $dbh->prepare ( $sql1 );
			$ok = $stmt->execute ();
			$idCliente =  $stmt->fetchAll ( PDO::FETCH_ASSOC );
			
			$idCliente = (int)$idCliente[0]['idCliente'];
		
			$stmt = $dbh->prepare ( $sql2 );
			$ok = $stmt->execute ();
			$idAP = $stmt->fetchAll ( PDO::FETCH_ASSOC );
			
			$idAP =(int)$idAP[0]['idAP'];
			//
			/////////////////////////////////////////////////////
			////insiro dados da conexao/////////////////////////
			//////////////////////////////////////////////////////
	
			$dbh->beginTransaction();
			$sql = "INSERT INTO conexao (idAP,idCliente,qualidade,latitude,longitude,linkspeed,rede,frequencia,potencia,ipcliente) VALUES ($idAP,$idCliente,$qualidade,$lat,$long,$ls,\"$rede\",$frequencia,$pot,\"$ipCliente\")";
			$stmt = $dbh->prepare ( $sql );
			$ok = $stmt->execute ();
			//$consulta = $stmt->fetchAll ( PDO::FETCH_ASSOC );
			
			if($ok)
			{
			 $dbh->commit();
			}
			////////////////////////////////////////////////////////////////////// 
			/// /////////////////////////////////////////////////////////////////
			// //////////////////////////////////////////////////////////////////
			return true;

			
		}

		function processDataBD()
		{
			$db = new dbAccess (); // database global variable
	 	 	$db->connect (); // trying to connect with values defined in class
			$dbh = $db->getDatabaseHandler();
			$sql = "SELECT qualidade,longitude,frequencia,latitude,linkspeed, ap.mac FROM conexao INNER JOIN ap ON conexao.idAP = ap.idAP ";
			$stmt = $dbh->prepare ( $sql );
			$ok = $stmt->execute ();
			$consulta = $stmt->fetchAll ( PDO::FETCH_ASSOC );
			
			for ($i=0; $i < count($consulta); $i++) 
			{ 
				$obj = (object)[];
				$obj->qualidade = $consulta[$i]['qualidade'];
				$obj->lng = $consulta[$i]['longitude'];
				$obj->lat = $consulta[$i]['latitude'];
				$obj->freq = $consulta[$i]['frequencia'];
				$obj->ls = $consulta[$i]['linkspeed'];
				$obj->macAP = $consulta[$i]['mac'];
				$resposta[$i] = $obj;

			}
			$resposta2 = json_encode($resposta);
			
			return $resposta2;


		}

		
		
	



 ?>