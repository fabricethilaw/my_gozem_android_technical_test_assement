package com.fabricethilaw.gozem.network

import okhttp3.*


/**
 * This will help us to mock our networking code while the API is not implemented
 * yet on Backend side.
 */
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // if (BuildConfig.DEBUG) {
        val uri = chain.request().url().uri().toString()
        val responseString: String = when {
            uri.endsWith("authenticate") -> {
                "Successful auth"
              // chain.request().body().
            }
            uri.endsWith("register") -> {
                "Successful registration"
            }
            uri.endsWith("profile") -> {
                mockedProfileJSON
            }

            else -> ""
        }

        return chain.proceed(chain.request())
            .newBuilder()
            .code(SUCCESS_CODE)
            .protocol(Protocol.HTTP_2)
            .message(responseString)
            .body(
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseString.toByteArray()
                )
            )
            .addHeader("content-type", "application/json")
            .build()
        //  } else {
        //      // just to be on safe side.
        //      throw IllegalAccessError("MockInterceptor is only meant for Testing Purposes and " +  "bound to be used only with DEBUG mode")
        //   }
    }

}

private const val SUCCESS_CODE = 200

const val mockedProfileJSON = """
[
  {
    "type": "profile",
    "content": {
      "image": "https://some.server/profile_image_url.png",
      "name": "John Doe",
      "email": "john@domain.com"
    }
  },
  {
    "type": "map",
    "content": {
      "title": "Location",
      "pin": "https://some.server/map_pin_image_url.png",
      "lat": 1.2345,
      "lng": 1.2345
    }
  },
  {
    "type": "data",
    "content": {
      "title": "Information",
      "source": "wss://echo.websocket.org",
      "value": "Loading..."
    }
  }
]
"""