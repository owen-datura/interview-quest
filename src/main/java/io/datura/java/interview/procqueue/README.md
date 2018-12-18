This is based on the interview question ["Build hierarchy with stack"][1] presented on the [La Vivien][2] YouTube channel.

The goal of this exercise is to read a text file that contains an execution dump of some programs. We're to read this input, determine the execution time (as defined as the difference between the start and end timestamps) and output the result, arranged in order of execution.

## Initial Changes
* Their implementation used a `Stack` which, while not deprecated per se, is now supplanted by the classes that implement the `Deque` interface.
* Didn't opt to use try-with-resources.
* Keep the output in its native, non-string format until the output's actually generated.
* Instead of calling `String#split` within a loop, precompile the Pattern.
* There's never a reason to modify attributes like the executable's name or starting timestamp, so make them immutable and set via the constructor.

## Larger Changes
* Isn't implemented to allow for testability (setup, processing and operations are all done in `main()`.), so refactor to output objects rather than putting the processing and output generation in the same monolithic method.
* Remove the need for the 'output' and 'temp' stacks. We'd created a `Process` object already and need to track the order of execution, so leverage OO by linking the child processes to the parent and handle them recursively.

[1]: https://www.youtube.com/watch?v=GWNBtCq3yW4
[2]: https://www.youtube.com/channel/UCh6kNOJMNgbgZPgeKGB-Gww
