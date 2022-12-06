# UCS and IDA* Algorithms Implementation

This is an implementation of two algorithms for searching paths in a graph: UCS (Uniform Cost Search) and IDA* (Iterative Deepening A*). This project accepts as input the roads of a city, their cost to traverse under normal traffic and traffic predictions for a specific day and it calculates the less costly path between two points of the city using both algorithms.

You can find more information on the Internet about [UCS](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Practical_optimizations_and_infinite_graphs) and [IDA*](https://en.wikipedia.org/wiki/Iterative_deepening_A*). Note that the heuristic function for IDA* in this implementation is the result of Dijkstra's algorithm using low traffic costs for all roads.

## How to compile

**Requirements**: You need to have *JDK* and *make* installed. 

Execute the following commands to download and compile the code:

```
  $ git clone https://github.com/iapost/ucs-ida
  $ cd ucs-ida
  $ make
```

## How to run

For testing purposes, three files with sample inputs are provided in the [examples](examples) folder. To execute the code with one of the sample inputs execute the following:

```
  $ java Main < examples/sampleInput1.txt
```

## Input format

The implementation reads data from standard input. The format of the data must be the following:

```
<Source>Node1</Source>
<Destination>Node132</Destination>
<Roads>
Road1; Node1; Node2; 44
Road2; Node2; Node5; 9
...
</Roads>
<Predictions>
Road1; normal
Road2; low
Road3; heavy
...
</Predictions>
```

The first two lines of the input specify the source and destination. Then, each line between \<Roads\> and \</Roads\> specifies a road with its name, its two ends and its traversal cost under normal traffic. Note that all roads are considered two-way. Finally, each line between \<Predictions\> and \</Predictions\> specifies the predicted traffic of a road for each day ("low" means 10% reduction and "heavy" means 25% increase in the cost).


## License

Distributed under the GPL-3.0 License. See [`LICENSE`](LICENSE) for more information.
