# Life simulation

Map-based simulation of animals' behavior. This program allows users to observe animals moving, feeding, and reproducing, while presenting statistical data through charts. The project was designed for the UST AGH object-oriented programming class.
### Game rules

* Animals have to move each day. The position change costs energy.
* Consuming grass allows gathering some energy for the future.
* Green fields at the center of the map give more energy.
* A pair of animals on the same field can reproduce if sufficient resources are available.
* Way of moving is based on the parent's genome and random factors.
* Animals in energy deficit die.

![](https://github.com/olgierdjw/darwin-simulation/blob/main/gif/example.gif?raw=true)

### Development Technologies
* Java with object-oriented concepts
* JavaFX for the user interface
* functional programming techniques to work with streams
* basic design patterns


### How to run
```sh
./gradlew run
```


