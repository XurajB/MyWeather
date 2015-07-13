# MyWeather
Sample weather application for Android that pulls weather information using World Weather Online API.

Description:
User searches a location by tapping on search icon on Actionbar. Possible search results are given to user to choose from using Search API. The selection is passed to Weather service where application calls World Weather Online API using the provided location and unique API key to get weather data back. The data is received in JSON and is parsed into Java objects and displayed in the UI. The weather icon is downloaded from the URL provided by the service. When a user taps anywhere in the UI, a five day weather forcast is displayed for that location. The user can go back and enter another location and repeat the process. When the application loads default weather data for "Chicago, IL" is displayed.The weather data and image download tasks are handled in worker thread by using AsyncTask.

Assumptions
* All temperature measurements are done in Fahrenheit (°F).
* Only mobile views are created. Layouts for Tablet and other displays are not provided.

