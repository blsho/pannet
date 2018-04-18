import pyowm, os

ENV_CITY = 'CITY_NAME'
ENV_KEY = 'OPENWEATHER_API_KEY'

try:
  city = os.environ[ENV_CITY]
  key = os.environ[ENV_KEY]
  #key = "877508fae39e147ca145416f48c39421"
except(KeyError):
  print("Environment variables " + ENV_CITY + ", " + ENV_KEY +" are missing")
  quit()
  
owm = pyowm.OWM(key)
observation = owm.weather_at_place(city)
w = observation.get_weather()
print("source=openweathermap, city=\"" + city + "\", description=\"" + w.get_detailed_status() + "\", temp=\"" + str(w.get_temperature('celsius').get('temp')) + "\", humidity=" + str(w.get_humidity())) 