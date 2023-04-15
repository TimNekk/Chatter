<h1 align="center">
  <br>
  <img src="https://play-lh.googleusercontent.com/Baek0GK58uDk6j1wRQQEDkrXcPXIVD2wypZqhg_Q4WXc1QE23DEl4GX1s7XJfj666FQ" alt="Gigapixel" height="240">
  <br>
  Chatter
  <br>
</h1>

<h4 align="center">CLI Chatter client</h4>

## Overview

Java CLI client based on sockets.
Supports authorization with username.


## Usage

Install client using Maven

```
mvn package
```

And start it on host and port of running <a href="https://github.com/TimNekk/Chatter">Chatter Server</a>

```
java -jar target\chatter-1.0.jar -H localhost -P 9999
```