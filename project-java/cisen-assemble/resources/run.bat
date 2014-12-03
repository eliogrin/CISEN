@echo off
rem java -jar bin/felix.jar
java -Xdebug -Xrunjdwp:transport=dt_socket,address=5000,server=y -jar bin/felix.jar
