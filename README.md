[![Build Status](https://travis-ci.org/multiworldtesting/explore-java.png)](https://travis-ci.org/jmorra/explore-java)
[![Windows Build Status](https://ci.appveyor.com/api/projects/status/github/MultiWorldTesting/explore-java?branch=master&svg=true)](https://ci.appveyor.com/project/lhoang29/explore-java)

Exploration Library
=======

The exploration library addresses the ‘gathering the data’ aspect of machine learning rather than the ‘using the data’ aspect we are most familiar with. The primary goal here is to enable individuals (i.e. you) to gather the right data for using machine learning for interventions in a live system based on user feedback (click, dwell, correction, etc…). Empirically, gathering the right data has often made a substantial difference. Theoretically, we know it is required to disentangle causation from correlation effectively in general.

First version of client-side exploration library that includes the following exploration algorithms:
- Epsilon Greedy
- Tau First
- Softmax
- Bootstrap
- Generic (users can specify custom weight for every action)

This release supports Java. For other languages, see: https://github.com/multiworldtesting/.

For sample usage, see: https://github.com/multiworldtesting/explore-java/blob/master/src/main/java/com/mwt/sample/ExploreOnlySample.java
