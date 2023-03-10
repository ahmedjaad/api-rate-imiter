# API Rate limiter

The application to demonstrate the idea of rate limiting API requests from the API consumer, the application accepts
any HTTP GET request whose url matches with /api/v1/**, the request will be checked against the rules, if it fails an
HTTP response with the 429 code will be returned and if it passes it will forward to the protected API service
(for simplicity, the hypothetical service has only one and point /api_service/api/v1)

## Prerequisite

* Java 17

## How to Run

The app make use of Spring runtime profiles feature, the app understands two valid profiles

* `standalone`: This is the default profile if no specific profiles is supplied, this is meant to run when no
  distributed cache is available (more ont the presentation deck)
* `distributed`: This profile is meant when Redis is available, Redis must be up and running and accessible by the app
  before running the app, by default the app is looking for the locally running Redis on port 6379, but this can be
  changed either in the [application-distributed.properties](src/main/resources/application-distributed.properties) the
  property name is `custom.cacheManager.redis.connection.url` or while running the app by setting JVM system property

Two steps need to run the application

1) Build and package the app with the maven wrapper, at the project root directory run `.

```
       ./mvnw clean package`
```

2) Once the app is build and packaged successfully,
    1) `standalone` profile: run the app with this command,

```
       java -jar ./target/api-rate-limiter-0.0.1-SNAPSHOT.jar
```

as mentioned earlier this will run with the `standalone` profile, the app will listen to port `8082`,
2) `distributed` profile: To activate distributed profile, Redis must be running and accessible by the app, then run
this command

```
       java -jar -Dspring.profiles.active=distributed ./target/api-rate-limiter-0.0.1-SNAPSHOT.jar
```

## How to test

The app is a tenant based, that is only request with valid tenant will be accepted, we have a very simplistic way to
identify the tenant, an HTTP header with key `X-tenant-id`,
we have three valid hypothetical tenants, `irembo`, `google` and `meta`, The app will process any endpoint that
matches `/api/v1/**` although the only endpoint with a valid response is `/api/v1/hello`
To send an HTTP request and test user the following `curl` command

```
       curl -X GET --location "http://localhost:${PORT}/api/v1/hello" \
    -H "X-tenant-id: irembo"
```

PORT value will be `8082` for the `standalone` profile and `8081` for the `distributed` one, the tenant id can also be
changed to either `meta` or  `google`

## Valid Tenant and their limits
we have a system-wide limit that is applicable to all tenant, bellow table gives details

| Number of requests allowed | Time span |
|----------------------------|-----------|
| 5                          | 15 sec    |

Below table for valid tenants and their limits

| Tenant Id | Monthly limit | Duration for time<br/> window | Allowed requests for<br/> time window limit |
|-----------|---------------|-------------------------------|---------------------------------------------|
| _irembo_  | 100           | 1 millisecond                 | 1                                           |
| _google_  | 100           | 2 seconds                     | 1                                           |
| _meta_    | 100           | 1 minute                      | 1                                           |
