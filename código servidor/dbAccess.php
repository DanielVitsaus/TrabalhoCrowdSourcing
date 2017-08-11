<?php

/**
 * Database class that uses PDO as connection API
*/
class dbAccess {
	
	// mysql hostname
	private $hostname = '127.0.0.1';
	
	// mysql username
	private $username = 'root';
	
	// mysql password
	private $password = '250392';
	
	// mysql dbname
	private $dbname = 'aplicativo';
	
	// database connection handler
	private $dbh;
	
	// connection state
	private $connected = false;
	
	// connection error message
	private $errormsg = "";
	
	/**
	 * connects to the configured database
	 */
	public function connect() {
		try {
			$this->dbh = new PDO ( "mysql:host=$this->hostname;dbname=$this->dbname", $this->username, $this->password );
			$this->dbh->setAttribute ( PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING );
			$this->errormsg = "";
			$this->connected = true;
		} catch ( PDOException $e ) {
			$this->errormsg = $e->errorInfo;
			$this->connected = false;
		}
	}
	
	/**
	 * set the configuration to connect to a target database
	 * 
	 * @param string $dbHost
	 *        	database server name or address
	 * @param string $dbName
	 *        	database name
	 * @param string $dbUser
	 *        	database user
	 * @param string $dbPass
	 *        	database password
	 */
	public function setConnectionData($dbHost, $dbName, $dbUser, $dbPass) {
		$this->hostname = $dbHost;
		$this->dbname = $dbName;
		$this->username = $dbUser;
		$this->password = $dbPass;
	}
	
	/**
	 * give access to the PDO$dbh variable only if we are connected to the internal representation of
	 * the databse resulting from a new PDO(...) call
	 */
	public function getDatabaseHandler() {
		if ($this->connected)
			return $this->dbh;
		else
			return null;
	}
	
	/**
	 * get the internal error message when an exception occurs
	 * 
	 * @return string error message
	 */
	public function getErrorMessage() {
		return $this->errormsg;
	}
	
	/**
	 * returns if a connection with the database is established or not
	 *
	 * @return boolean
	 */
	public function getConnectionState() {
		return $this->connected;
	}
	
	/**
	 * to avoid and error from php this method was made to return the class name of this object
	 * 
	 * @return the class name
	 */
	public function __toString() {
		return $this->getProperty ();
	}
	
	/**
	 * disconnect from database releasing resources
	 */
	public function disconnect() {
		$this->dbh = null;
		$this->connected = false;
		$this->errormsg = "Disconnected";
	}
	
	/**
	 * sets the database name to connecto to
	 * 
	 * @param string $dbName database name
	 */
	public function setDatabaseName($dbName) {
		$this->dbname = $dbName;
	}
}

?>
