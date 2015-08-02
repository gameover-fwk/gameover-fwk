# Gameover framework #
This framework can be used to develop games or ui application based on [LibGDX](https://libgdx.badlogicgames.com)
The code is developed with scala 2.1.x and has dependencies with LibGDX 1.5.x
The code is available on [Github](https://github.com/PixelDuck/gameover-game-framework)

## Build framework and deploy on local repository ##
To build the framework, just run gradle task:
```
gradle clean build
```

to publish on a local Maven repository, run:
```
gradle publishToMavenLocal
```

## Features ##
* A version of LibGDX Array class to support scala functional programming (map, flatMap, filter)
* A definition of A* path-finding algorithm and a simple implementation for 2D games
* A graphic resources loader which support reloading on the fly
* A Shader memory helper to easily activate/disabled shaders
* An extension of TileMapRenderer to enabled fake perspective in 2D games
* An action holder trait with several implementation to manage input action from keyboard or from UI
* An object pool trait and some implementation for most used libgdx class (Vector2, Rectangle...)
* Some missing math, gfx, polygons utilities
* A trait LibGDXHelper to help the usage of libgdx in classes

## License ##
The framework is based on The [MIT License (MIT)](https://en.wikipedia.org/wiki/MIT_License)
The framework was created by [Olivier MARTIN](http://gameover.co.in) <pixelduck.game@gmail.com>.
Authors and contributors are listed in file [contributors.txt](contributors.txt) in the root of the project.
Feel free to submit your Pull Request!

## TODO ##
* fix path finding
* add some test for A*