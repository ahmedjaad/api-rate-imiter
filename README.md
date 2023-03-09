# API Rate limiter

The application to demonstrate the idea of rate limiting API requests, from the API consumer, the application accepts any HTTP GET request whose url matches with /api/v1/**, the request will be checked against the rules, if it fails an HTTP response with the 429 code will be returned  and if it passes it will forward to the protected API service
(for simplicity, the hypothetical service has only one and point /api_service/api/v1)

## Prerequisite
* Java 17 

## How to Run
The app make use of Spring runtime profiles feature, the app understands two valid profiles
* `standalone`: This is the default profile if no specific profiles is supplied, this is meant to run when no distributed cache is available (more ont the presentation deck)
* `distributed`: This profile is meant when Redis is available, Redis must be up and running and accessible by the app before running the app, by default the app is looking for the locally running Redis on port 6379, but this can be changed either in the [application-distributed.properties](src/main/resources/application-distributed.properties) the property name is `custom.cacheManager.redis.connection.url` or while running the app by setting JVM system property 