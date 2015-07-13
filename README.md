# MyWeather
Sample weather application for Android that pulls weather information using World Weather Online API.

Description:
User searches a location by tapping on search icon on Actionbar. Possible search results are given to user to choose from using Search API. The selection is passed to Weather service where application calls World Weather Online API using the provided location and unique API key to get weather data back. The data is received in JSON and is parsed into Java objects and displayed in the UI. The weather icon is downloaded from the URL provided by the service. When a user taps anywhere in the UI, a five day weather forcast is displayed for that location. The user can go back and enter another location and repeat the process. When the application loads default weather data for "Chicago, Illinois" is displayed.The weather data and image download tasks are handled in worker thread by using AsyncTask.

Sample API endpoint for weather: http://api.worldweatheronline.com/free/v2/weather.ashx?key=b315fed477ad4084a7f848f218956&q=baltimore,md&num_of_days=5&tp=24&format=json
Sample API endpoint for location search: http://api.worldweatheronline.com/free/v2/search.ashx?key=b315fed477ad4084a7f848f218956&query=washington&num_of_results=5&format=json

Assumptions
* All temperature measurements are done in Fahrenheit (°F).
* Only mobile views are created. Layouts for Tablet and other displays are not provided.

