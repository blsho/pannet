To run port scaner call java with proper switch
```
$ java -jar portScaner.jar --help
usage: portScaner
 -e,--maxPort <arg>   Port number where scanning stops. Default value =
					  1023
 -h,--help            Shows this information
 -p,--threads <arg>   Number of threads used during scanning. Default
					  value = 4
 -r,--resetCache      Reset cache abou opened port
 -s,--minPort <arg>   Port number where scanning starts. Default value = 1
 -t,--timeout <arg>   Timeout after which is socket considered closed.
					  Default value = 50
```
						  
Example:
```
$ java -jar portScaner.jar --threads 50 --timeout 10 --maxPort 5000 1.1.1.1 8.8.8.8
*Target - 1.1.1.1: Full scan results:*
Host: 1.1.1.1 Port: 993/opened/tcp
Host: 1.1.1.1 Port: 995/opened/tcp
Host: 1.1.1.1 Port: 110/opened/tcp
Host: 1.1.1.1 Port: 119/opened/tcp
Host: 1.1.1.1 Port: 25/opened/tcp
Host: 1.1.1.1 Port: 143/opened/tcp
Host: 1.1.1.1 Port: 443/opened/tcp
Host: 1.1.1.1 Port: 563/opened/tcp
Host: 1.1.1.1 Port: 465/opened/tcp
Host: 1.1.1.1 Port: 587/opened/tcp
*Target - 8.8.8.8: Full scan results:*
Host: 8.8.8.8 Port: 993/opened/tcp
Host: 8.8.8.8 Port: 995/opened/tcp
Host: 8.8.8.8 Port: 110/opened/tcp
Host: 8.8.8.8 Port: 119/opened/tcp
Host: 8.8.8.8 Port: 25/opened/tcp
Host: 8.8.8.8 Port: 143/opened/tcp
Host: 8.8.8.8 Port: 443/opened/tcp
Host: 8.8.8.8 Port: 53/opened/tcp
Host: 8.8.8.8 Port: 563/opened/tcp
Host: 8.8.8.8 Port: 465/opened/tcp
Host: 8.8.8.8 Port: 587/opened/tcp
```