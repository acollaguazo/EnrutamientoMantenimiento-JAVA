#!/bin/bash
#Este script permite agregar un dispositivo router pasando los siguientes parámetros:
# $1 -> hostname , $2->ip , $3->username , $4->password

echo "insert into dev_routers (HOSTNAME, IP, USER, PASSWD)
values ('$1','$2','$3','$4');" | mysql -uroot avengersDB

