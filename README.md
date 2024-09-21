A rate limitter application written in Java SpringBoot

- Two types of rate limitters are implemented. 1) Fixed Window Rate Limitter & 2) Sliding Window Rate Limitter
- Fixed Window Rate Limitter operates with the following Algorithm 

    - ```
      For a given user
        if the user is putting a first time request
            set current time is the first served reuqest's time for the user and set the request is allowed
        else if the gap between the user's first served request and current request is less than WINDOW SIZE
            if max number of allowed request is greater than the number of requests served for that WINDOW SIZE
                increase the count of request for the particular user by 1
                set the request is allowed
            else
                set the request is not allowed
        else
            set current time is the first served reuqest's time for the user and set the request is allowed
      ```
      
- Sliding Window Rate Limitter operates with the following Algorithm

    - ```
      For a given user
        if the user is putting a first time request
            cache the request time and the request is allowed
        else
            get all the request timestamps cached for the user
            remove the timestamps which are out of WINDOW SIZE considering the current request timestamp
            if max number of requests permitted for that WINDOW SIZE are still greater than the number of already served requests
                permit the current request and add its timestamp to cache
            else
                set the request is not allowed
      ```
      
To run the application

- Install `Java 22`
- Run Docker Desktop & start redis server with the following command
- `docker run --name redis-server -d -p 6379:6379 redis`
- Mark either `com.ratelimitter.limit.util.FixedWindowRateLimitter` or `com.ratelimitter.limit.util.SlidingWindowRateLimitter`as `@Component`
- Run the Spring Boot App
- ```
  curl --location 'localhost:8080/sample/api' \
  --header 'userid: djdhar'
  ```