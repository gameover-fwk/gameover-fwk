# Gameover framework release notes

## 0.2.5

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
