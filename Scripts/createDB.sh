#!/bin/bash
#Script createDB.sh crea la base de datos para almacenar información acerca de los routers de los clientes
echo "drop database IF EXISTS avengersDB;" | mysql -uroot -proot
#CREA LA BASE DE DATOS AVENGERSDB
echo "create database avengersDB;" | mysql -uroot -proot
#Crea la tabla clientes para almacenar datos de nuestros clientes.
echo "create table clientes(
	ID_C INT NOT NULL,
	NAME VARCHAR(50),
	PRIMARY KEY(ID_C) );" | mysql -uroot -proot avengersDB
#Crea la tabla routers para almacenar las rutas de los archivos con los parámetros obtenidos mediante el script getInfoRouter.pl.
echo "create table routers(
	ID INT NOT NULL AUTO_INCREMENT,
	ID_CLIENT INT NOT NULL,
	HOSTNAME VARCHAR(20) NOT NULL,
	IP VARCHAR(20),
	USER VARCHAR(50),
	PASSWD VARCHAR(50),
	PATH_VERSION VARCHAR(100),
	PATH_INTERFACES VARCHAR(100),
	PATH_ROUTING VARCHAR(100),
	PATH_LOGS VARCHAR(100),
	PRIMARY KEY (ID),
	FOREIGN KEY (ID_CLIENT) REFERENCES clientes(ID_C) );" | mysql -uroot -proot avengersDB
#Crea la tabla configs para almacenar las rutas de los archivos de configuración obtenidos mediante el script configsBackup.pl.
echo "create table configs(
    	ID int not null auto_increment,
    	ID_DEV int not null,
	fecha DATE,
    	PATH varchar(200),
    	primary key(ID),
    	foreign key(ID_DEV) references routers(ID));" | mysql -uroot -proot avengersDB

#AGREGA A LOS CLIENTES
echo "insert into clientes values (001,'Constructora_Edebrecht');" | mysql -uroot -proot avengersDB
echo "insert into clientes values (002,'Cooperativa_Fedora');" | mysql -uroot -proot avengersDB
echo "insert into clientes values (003,'Corporacion_Ecuatoriana_de_GAS');" | mysql -uroot -proot avengersDB

#AGREGA DISPOSITIVOS A LA TABLA ROUTERS
echo "insert into routers (ID_CLIENT, HOSTNAME, IP, USER, PASSWD)
values (001,'CE1','10.10.10.2','admin','admin');" | mysql -uroot -proot avengersDB

echo "insert into routers (ID_CLIENT, HOSTNAME, IP, USER, PASSWD)
values (001,'CE2','10.20.10.2','admin','admin');" | mysql -uroot -proot avengersDB

echo "insert into routers (ID_CLIENT, HOSTNAME, IP, USER, PASSWD)
values (002,'CF1','10.30.10.2','admin','admin');" | mysql -uroot -proot avengersDB

echo "insert into routers (ID_CLIENT, HOSTNAME, IP, USER, PASSWD)
values (002,'CF2','10.30.10.3','admin','admin');" | mysql -uroot -proot avengersDB

echo "insert into routers (ID_CLIENT, HOSTNAME, IP, USER, PASSWD)
values (003,'CG1','209.165.200.233','admin','admin');" | mysql -uroot -proot avengersDB

echo "insert into routers (ID_CLIENT, HOSTNAME, IP, USER, PASSWD)
values (003,'CG2','209.165.200.237','admin','admin');" | mysql -uroot -proot avengersDB

