cd .\cisen-assemble\target\cisen-assemble-0.0.1-SNAPSHOT-assemble
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 -jar bin/felix.jar
cd .\..\..\..