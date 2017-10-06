#!/bin/sh
# Script cortafuegos.sh para la configuración de iptables
#
# Primero borramos todas las reglas previas que puedan existir
iptables -F
iptables -X
iptables -Z

# Después definimos que las politicas por defecto
iptables -P INPUT DROP
iptables -P OUTPUT ACCEPT

# Para evitar errores en el sistema, debemos aceptar
# todas las comunicaciones por la interfaz lo (localhost)
iptables -A INPUT -i lo -j ACCEPT

# Aceptamos las comunicaciones que nos interesan
iptables -A INPUT -s 209.165.200.244/30 -j ACCEPT
iptables -A INPUT -s 209.165.200.233 -j ACCEPT
iptables -A INPUT -s 209.165.200.237 -j ACCEPT
iptables -A INPUT -s 10.10.10.2 -j ACCEPT
iptables -A INPUT -s 10.20.10.2 -j ACCEPT
iptables -A INPUT -s 10.30.10.2 -j ACCEPT
iptables -A INPUT -s 10.30.10.3 -j ACCEPT
iptables -A INPUT -s 192.168.1.0/24 -j ACCEPT
iptables -A INPUT -s 192.168.2.0/24 -j ACCEPT
iptables -A INPUT -s 192.168.7.0/24 -j ACCEPT
iptables -A INPUT -s 192.168.8.0/24 -j ACCEPT
iptables -A INPUT -s 192.168.4.0/24 -j ACCEPT

# Comprobamos cómo quedan las reglas
iptables -L -n
