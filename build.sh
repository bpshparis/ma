#!/bin/bash

JAVAC=$(command -v javac)

if [ "${JAVAC}" == "" ]; then
	echo "ERROR !!! javac not found PATH. Exiting..."
	exit 1
fi

rm -rf WebContent/WEB-INF/classes/*.class

javac -cp wlp/*:WebContent/WEB-INF/lib/* -d WebContent/WEB-INF/classes/ src/com/bpshparis/wsvc/app0/*.java

exit 0
