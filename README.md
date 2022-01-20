# My Solution for a Gozem technical Android assessment

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/33c01a20e6ef4a5892809b1d418b88b2)](https://www.codacy.com/gh/fabricethilaw/my_gozem_test/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fabricethilaw/my_gozem_test&amp;utm_campaign=Badge_Grade)

The business case is a mobile application that allows the user to authenticate/register
and track his/her GPS location in real time.


## Operational modules of the solution

### A mock server created with Postman

For the case, I created a mocked RESTful API interface exposing 3 endpoints :
 * Authentication endpoint: 	**POST {server_url}/authenticate**
 * Registration endpoint:	**POST {server_url}/register**
 * Registration endpoint:	**GET {server_url}/profile**

### A fully implemented native Android application
The android application has the following functions

- User authentication through RESTful webservice integration
- GPS tracking and Map display
- Instant messages display with Websocket

![Demo.png](https://github.com/fabricethilaw/my_gozem_test/blob/develop/demo.png)

## Tech stack to build the Mobile application

- Kotlin & Java programming languages
- MVVM architecture (for the projet's package and code structure)
- Android Jetpack (to implement the UI using modern design practices)
- Android Runtime Permissions management
- Navigation component (For user navigation flow through the app)
- Kotlin Coroutines (asynchronous and time consuming process)
- Retrofit2 library (for http requests over the network)
- Reactive streams (Rx Java/Kotlin)
- Google Maps API for the map display
- GPS
- Websockets
- JUnit and Espresso (unit tests and instrumented tests)
- Git


