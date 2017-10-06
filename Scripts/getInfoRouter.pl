#!/usr/bin/perl
#Script getInfoRouter.pl obtiene la infomación de los enrutadores tales como la versión, 
#el estado de las interfaces, la tabla de enrutamiento y los logs.

use DBI;
use DBD::mysql;
use Time::Piece;
use Net::Telnet::Cisco;

#Setteo de variables iniciales
my $date = localtime->strftime('%m-%d-%Y-%R');
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
		$query_select = "SELECT HOSTNAME,IP,USER,PASSWD FROM $tablename where routers.ID_CLIENT=$idCliente ;";
		$query_handle = $dbi_connect->prepare( $query_select );

		#Ejecutar el query
		$query_handle->execute();

		#Enlazar las columnas a las variables
		$query_handle->bind_columns(undef, \$name, \$ip, \$user, \$pass );


		#Recorrer datos obtenidos
		while  ( $query_handle->fetch()){
			print "$name - $ip - $user - $pass \n";

			#Inicia sesion en el enrutador remotamente
			$session = Net::Telnet::Cisco->new(Host => $ip );
			$session->login ( $user ,$pass );

			$path_data = "/home/soporte/Netmon/$nameCliente/$name";
			
			#ejecuta los comandos y almacena los show en su variable correspondiente.
			@output = $session->cmd( "show version" );
			@outInterfaces = $session->cmd( "show ip int brief" );
			@outIproute = $session->cmd( "show ip route" );
			@outLogs = $session->cmd( "show logging | begin Log Buffer" );

			#obtiene el show y lo guardar en un archivo.
			open(SHVERSION, ">$path_data/show_version_$date.txt");
			foreach $out ( @output ){
				print SHVERSION $out;
			}

			open(INTERFACES, ">$path_data/show_interfaces_$date.txt");
			foreach $out ( @outInterfaces ){
				print INTERFACES $out;
			}

			open(IPROUTE, ">$path_data/show_iproute_$date.txt");
			foreach $out ( @outIproute ){
				print IPROUTE $out;
			}

			open(LOGS, ">$path_data/show_logs_$date.txt");
			foreach $out ( @outLogs ){
				print LOGS $out;
			}

			close(SHVERSION);
			close(INTERFACES);
			close(IPROUTE);
			close(LOGS);
			
			$query_update = "UPDATE routers SET PATH_VERSION='$path_data/show_version_$date.txt' , PATH_INTERFACES= '$path_data/show_interfaces_$date.txt' , PATH_ROUTING='$path_data/show_iproute_$date.txt' , PATH_LOGS='$path_data/show_logs_$date.txt' WHERE HOSTNAME='$name';";  ;
			$query_hupdate = $dbi_connect->prepare( $query_update );

			#Ejecutar el query
			$query_hupdate->execute();
			$session->close;
		}
}

$query_hclient-> finish();
$query_handle -> finish();
$dbi_connect->disconnect;
