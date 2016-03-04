[![build](https://travis-ci.org/gameover-fwk/gameover-fwk.svg?branch=master)](https://travis-ci.org/gameover-fwk/gameover-fwk) [![codecov.io](https://codecov.io/github/gameover-fwk/gameover-fwk/coverage.svg?branch=master)](https://codecov.io/github/gameover-fwk/gameover-fwk?branch=master)


# Gameover framework
This framework can be used to develop games or ui application based on [LibGDX](https://libgdx.badlogicgames.com)
The code is developed with scala 2.1.x and has dependencies with LibGDX 1.5.x
The code is available on [Github](https://github.com/gameover-fwl/gameover-fwk)

## Use the framework
Jar and source are available on [Sonatype](http://www.sonatype.org/) repository.
With maven, add dependency:
```
<dependency>
  <groupId>io.github.gameover-fwk</groupId>
  <artifactId>gameover-fwk</artifactId>
  <version>0.2.2</version>
</dependency>
```

With gradle, add dependency:
```
dependencies {
    compile "io.github.gameover-fwk:gameover-fwk:0.2.2"
}
```

With SBT, add dependency:
```
libraryDependencies += "io.github.gameover-fwk" % "gameover-fwk" % "0.2.2"
```

## Features
* A trait LibGDXHelper to help the usage of libgdx in classes. The trait contains a class for implicit conversion from LibGDX Array ti a class supporting scala functional programming (map, flatMap, filter...)
* A definition of A* path-finding algorithm and a simple implementation for 2D games
* A graphic resources loader which support reloading on the fly
* A Shader memory helper to easily activate/disabled shaders
* An extension of TileMapRenderer to enabled fake perspective in 2D games
* An action holder trait with several implementation to manage input action from keyboard or from UI
* An object pool trait and some implementation for most used libgdx class (Vector2, Rectangle...)
* Some missing math, gfx, polygons utilities

## Build framework and deploy on local repository
To build the framework, just run gradle task:
```
gradlew clean build
```

to publish on a local Maven repository, run:
```
gradlew publishToMavenLocal
```

## License
The framework is under [MIT License (MIT)](https://en.wikipedia.org/wiki/MIT_License)
The framework was created by [Olivier MARTIN](http://gameover.co.in) <pixelduck.game@gmail.com>.
Authors and contributors are listed in file [contributors.txt](contributors.txt) in the root of the project.
Feel free to submit your Pull Request!
