#!/usr/bin/perl
#Realiza los respaldos diarios de las configuraciones de los equipos.
#obtenido para cada enrutador. 
use DBI;
use DBD::mysql;
use Time::Piece;
use Net::Telnet::Cisco;

#Setteo de variables iniciales
my $date = localtime->strftime('%Y-%m-%d-%R');
$database = "avengersDB";
$tablename = "routers";
$user = "root";
$password = "root";
$tftp_server = "209.165.200.246";
$dsn = "dbi:mysql:$database:localhost:3306"; # data source name

#Conexión a la base de datos
#PERL DBI connect
$dbi_connect = DBI->connect( $dsn, $user, $password) or die "No se puede conectar: $DBI::errstr\n";

$query_client = "SELECT * FROM clientes;";
$query_hclient = $dbi_connect->prepare( $query_client );
$query_hclient->execute();
$query_hclient->bind_columns(undef, \$idCliente, \$nameCliente );

while ( $query_hclient->fetch()){
	#Preparar el query
		$query_select = "SELECT ID,HOSTNAME,IP,USER,PASSWD FROM $tablename where routers.ID_CLIENT=$idCliente ;";
		$query_handle = $dbi_connect->prepare( $query_select );

		#Ejecutar el query
		$query_handle->execute();

		#Enlazar las columnas a las variables
		$query_handle->bind_columns(undef, \$id, \$name, \$ip, \$user, \$pass );

		#Recorrer datos obtenidos
		while  ( $query_handle->fetch()){
			print "$id - $name - $ip - $user - $pass \n";

			$session = Net::Telnet::Cisco->new(Host => $ip );
			$session->login ( $user ,$pass );

			$pathConfig = "tftp://$tftp_server/RoutersConfig/$name-$date-config";
			$pathSaveScript = "/srv/tftp/RoutersConfig/$name-$date-config";

			#realiza un backup del show running.
			$session->cmd( "copy running-config $pathConfig\n\n\n" );
			$session->close;
			
			#inserta un nuevo registro de la ruta del archivo de configuración
			$query_insert = "insert into configs (ID_DEV, fecha, PATH) values ('$id' ,'$date' , '$pathSaveScript')";
			$query_hinsert = $dbi_connect->prepare( $query_insert );
			$query_hinsert->execute();
			$query_hinsert->finish();
		}
}

$query_hclient-> finish();
$query_handle -> finish();
$dbi_connect->disconnect;
