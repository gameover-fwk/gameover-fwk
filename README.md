[![build](https://travis-ci.org/gameover-fwk/gameover-fwk.svg?branch=master)](https://travis-ci.org/gameover-fwk/gameover-fwk) [![codecov.io](https://codecov.io/github/gameover-fwk/gameover-fwk/coverage.svg?branch=master)](https://codecov.io/github/gameover-fwk/gameover-fwk?branch=master)

to read: https://phauer.com/2018/best-practices-unit-testing-kotlin/
to read: https://junit.org/junit5/docs/current/user-guide/#migrating-from-junit4


# Gameover framework
This framework can be used to develop games or ui application based on [LibGDX](https://libgdx.badlogicgames.com)
The code is developed with kotlin and has dependencies with LibGDX 1.9.10
The code is available on [Github](https://github.com/gameover-fwl/gameover-fwk)

## Use the framework
Jar and source are available on [Sonatype](http://www.sonatype.org/) repository.
With maven, add dependency:
```
<dependency>
  <groupId>io.github.gameover-fwk</groupId>
  <artifactId>gameover-fwk</artifactId>
  <version>1.0.0</version>
</dependency>
```

With gradle, add dependency:
```
dependencies {
    compile "io.github.gameover-fwk:gameover-fwk:1.0.0"
}
```

## Features
* A definition of A* path-finding algorithm and a simple implementation for 2D games
* A graphic resources loader which support reloading on the fly
* A Shader memory helper to easily activate/disabled shaders
* An extension of TileMapRenderer to enabled fake perspective in 2D games
* An action holder trait with several implementation to manage input action from keyboard or from UI
* An object pool trait and some implementation for most used libgdx class (Vector2, Rectangle...)
* Some missing math, gfx, polygons utilities

See [Release notes](RELEASE_NOTES.md) for a list of changes between version

## Build framework and deploy on local repository
To build the framework, just run gradle task:
```
gradlew clean build
```

to publish on a local Maven repository, run:
```
gradlew publishToMavenLocal
```

## scala version

Notice that an old not maintained version was developed with scala 2.11.
The code is available on branch `scala` and publish to sonatype until version 0.2.6.
Starting from branch 1.x.x, only the kotlin version is available 

## License
The framework is under [MIT License (MIT)](https://en.wikipedia.org/wiki/MIT_License)
The framework was created by [Olivier MARTIN](http://gameover.co.in) <pixelduck.game@gmail.com>.
Authors and contributors are listed in file [contributors.txt](contributors.txt) in the root of the project.
Feel free to submit your Pull Request!
