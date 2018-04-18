# Exercise 1
Script getweather.py reads enviroment variables OPENWEATHER_API_KEY and CITY_NAME. Script fails if the variables are not set. OPENWEATHER_API_KEY should contain API key of service Open Weather Map. CITY_NAME should be name geological place. Input and output of the script execution should look like:
```
$ export
declare -x OPENWEATHER_API_KEY="xxxxxxxxxxxx"
declare -x CITY_NAME="Honolulu"
$ python getweather.py
source=openweathermap, city="Honolulu", description="few clouds", temp=70.2, humidity=75
```

File site.yml is Ansible playlist for Docker installation on Ubuntu. It follows the recomended installation guide:
```
https://docs.docker.com/install/linux/docker-ce/ubuntu/#install-docker-ce
```
It also modifies log file. 
	
To build image from Dockerfile run:
```
docker build -t weather:dev .
```
