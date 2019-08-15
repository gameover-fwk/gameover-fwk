# Gameover framework release notes

## 1.0.0
* Moving framework to kotlin. Old scala is still available in a branch.
* Upgrade version of libgdx to 1.9.10
TODO

## 0.2.6 (2018-06-23)
* Enhances smart smooth path finding to handle an origin area with a center in the void

## 0.2.5 (2018-04-13)
* Fix graphics loader for a strange bug when loading data from internal storage on android
* Fix AStar
* Fix direct view near wall
* Fix straight move check and use it for SmartPathFinding
* hasDirectView and findSmoothPath are taking now as origin and tarrget an area instead of a points.

## 0.2.4 (2018-02-07)
* change Perspective layer to handle three layers: a floor layer, a walls layer and a top layer. Use property "level" on the map to know where to render the layer.
* add the availability to disable a layer for collision detector

## 0.2.3 (2016-09-14)

* Upgrade libgdx to 1.9.3
* Add scala version to jar name in order to follow scala library practices

## 0.2.2 (2016-02-23)

* Add a simple font files and a font viewer class utility
* Animation can now have a collision area definition
* Change the graphic loader to return an option to return None when a graphics is not found

## 0.2.1 (2015-08-25)

* Change from explicit wrapper for GdxArray to implicit class through trait LibGdxHelper
* Fix smooth path finding
* Fix isDirect in collision detector
* Implements GdxArray as a scala collection

