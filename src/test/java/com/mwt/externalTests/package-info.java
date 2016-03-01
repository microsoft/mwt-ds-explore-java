/**
 * The point of these tests is to make sure any language-specific interface produces same output as other versions of
 * the exploration libraries. For example, the <a href="https://github.com/multiworldtesting/explore-cpp">C++</a> and
 * <a href="https://github.com/multiworldtesting/explore-csharp">C#</a> code are doing this in appveyor to make sure
 * their exploration is correct.
 * The steps needed in the build server are:
 * 1. Clone the BlackBox test project and either the C++ or C# version and build them.
 * 2. Trigger the BlackBox test project which will automatically run & compare outputs.
 */
package com.mwt.externalTests;
