# TrafficInsights

An automated traffic counter sits by a road and counts the number of cars that go past. Every half-hour the counter outputs the number of cars seen and resets the counter to zero. You are part of a development team that has been asked to implement a system to manage this data - the first task required is as follows :
Write a program that reads a file, where each line contains a timestamp (in yyyy-mm-dd T hh:mm:ss  format, i.e. ISO 8601) for the beginning of a half-hour and the number of cars seen that half hour. An example file is included on page 2. You can assume clean input, as these files are machine-generated.
The program should output:

* The number of cars seen in total

* A sequence of lines where each line contains a date (in  yyyy-mm-dd  format) and the
number of cars seen on that day (eg. 2016-11-23 289) for all days listed in the input file.

* The top 3 half hours with most cars, in the same format as the input file
* The 1.5 hour period with  least  cars (i.e. 3 contiguous half hour records)


## Design decisions

The program uses Java 8 streams for processing the input. The interface methods expect a stream to be passed in and the stream handling is the caller's concern. For the scope of this test, the program uses a `streamSupplier` to supply streams.

The functions make use of map reduce paradigms whereever necessary and use parallelism to leverage non-sequential nature of data.


## Getting started
### Setup instructions & system requirements
```
Java8
Joda-time
AssertJ
```

#### Running the program
It is recommended to import the codebase into intelliJ editor and run the program directly.

The first argument to the program is the path to the file containing the timestamp data, passed in as 'program arguments' directly from the IDE.

#### Unit tests
The tests are written using JUnit4 and AssertJ for assertions.

## Assumptions

Minimum data size is equal to WINDOW size which defined by a program constant `INTERVAL_WINDOW`
The timestamps are ordered in date and time sequentially.
The timestamps are in the interval of 0.5 hrs.
The timestamps are in correct ISO8601 format only.


## Seed/Test data

```
2016-12-01T05:00:00 5
2016-12-01T05:30:00 12
2016-12-01T06:00:00 14
2016-12-01T06:30:00 15
2016-12-01T07:00:00 25
2016-12-01T07:30:00 46
2016-12-01T08:00:00 42
2016-12-01T15:00:00 9
2016-12-01T15:30:00 11
2016-12-01T23:30:00 0
2016-12-05T09:30:00 18
2016-12-05T10:30:00 15
2016-12-05T11:30:00 7
2016-12-05T12:30:00 6
2016-12-05T13:30:00 9
2016-12-05T14:30:00 11
2016-12-05T15:30:00 15
2016-12-08T18:00:00 33
2016-12-08T19:00:00 28
2016-12-08T20:00:00 25
2016-12-08T21:00:00 21
2016-12-08T22:00:00 16
2016-12-08T23:00:00 11
2016-12-09T00:00:00 4
```
