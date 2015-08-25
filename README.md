# Gameover framework #
This framework can be used to develop games or ui application based on [LibGDX](https://libgdx.badlogicgames.com)
The code is developed with scala 2.1.x and has dependencies with LibGDX 1.5.x
The code is available on [Github](https://github.com/PixelDuck/gameover-game-framework)

## Build framework and deploy on local repository ##
To build the framework, just run gradle task:
```
gradlew clean build
```

to publish on a local Maven repository, run:
```
gradlew publishToMavenLocal
```

## Use the framework ##
Jar and source are available on [Sonatype](http://www.sonatype.org/) repository.
With maven, add dependency:
```
<dependency>
  <groupId>io.github.gameover-fwk</groupId>
  <artifactId>gameover-fwk</artifactId>
  <version>${gameoverFwkVersion}</version>
</dependency>
```

With gradle, add dependency:
```
dependencies {
    compile "io.github.gameover-fwk:gameover-fwk:$gameoverFwkVersion"
}
```

With SBT, add dependency:
```
libraryDependencies += "io.github.gameover-fwk" % "gameover-fwk" % gameoverFwkVersion
```

## Features
Check website: http://gameover-fwk.github.io/gameover-fwk/

## License ##
The framework is based on The [MIT License (MIT)](https://en.wikipedia.org/wiki/MIT_License)
The framework was created by [Olivier MARTIN](http://gameover.co.in) <pixelduck.game@gmail.com>.
Authors and contributors are listed in file [contributors.txt](contributors.txt) in the root of the project.
Feel free to submit your Pull Request!
