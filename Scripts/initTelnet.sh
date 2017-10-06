#!/bin/bash
#Script initTelnet.sh, inicia una sesion de telnet para la ip enviada como parámetro. $1=ip
 gnome-terminal -x bash -c "telnet $1"
