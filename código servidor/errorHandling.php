<?PHP
define("ERR_DB_NOT_CONNECTED", 1); //Error raised when you expect to be connected to the database but is not
define("ERR_DB_MALFORMED_SQL_QUERY", 2); //You have problems in your SQL query
define("ERR_READER_NOT_FOUND", 3); //No reader was found for the specific ID
define("ERR_READER_NO_CONNECTION", 4); 
define("ERR_READER_TIMEDOUT", 5);
define("ERR_READER_UNKNOW", 6);
define("ERR_CARD_TIMEOUT", 7);
define("ERR_PERSON_NOT_FOUND", 8);
define("ERR_PERSON_BIND_CARD", 9);
define("ERR_PERSON_PROFILE_NOT_FOUND", 10);

class serverException extends Exception {
	
	public function __construct($code,$details="") {
		$message = getErrorMessage($code). (empty($details)?"":" | Detalhes:" . $details);
        parent::__construct($message, $code);
    }
	
	public function encodeToJson() {
		$responseArray = array();
		$responseArray ["status"] = "error";
		$responseArray ["errcode"] = $this->getCode() ;
		$responseArray ["desc"] = $this->getMessage() ;
		return json_encode ( $responseArray );
	}
}

function getErrorMessage($errcode) {
	switch ($errcode) {
		case ERR_DB_NOT_CONNECTED:
			return "Banco de dados não conectado";
		case ERR_DB_MALFORMED_SQL_QUERY:
			return "Erro ao executar consulta SQL";
			default:
			return "Erro desconhecido: Codigo {$errcode}.";
	}
} 



?>