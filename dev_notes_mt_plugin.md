## Feedback update:

### Remove conditionals:
* remove conditional - replaced equality check with false
 -> Logical Expression Check - Replaced equality check with false
 -> Logical Expression - Replaced equality check with false
* remove conditional - replaced equality check with true
 -> Logical Expression Check - Replaced equality check with true
 -> Logical Expression - Replaced equality check with true

### Arithmetic operator deletion:
* Replaced integer operation with first member
 -> Arithmetic Operation Check - Replaced integer operation with first member
 -> Arithmetic Operation - Replaced integer operation with first member
* Replaced integer operation by second member
 -> Arithmetic Operation Check - Replaced integer operation by second member
 -> Arithmetic Operation - Replaced integer operation with second member


## Tasks:
### PitClipse 
[√] Rename PIT 
  [√]  PIT Mutations -> Mutation List
  [√]  PIT Summary -> Mutation Summary
  [√]  PIT Mutation Test -> Mutation Test
[√] Set default mutator set VT Defaults RC and AOD
 - [√] Replace "Old Defaults" with "VT Defaults"
 - [√] Set "VT Defaults" as default mutator set
 - [√] Set "VT Defaults" to mutator set AOD and Remove Conditionals
 - [] Comment out Test cases for testing Old Defaults

[√] Remove extra Mutation Groups in the Mutation List tab
 - [√] Change SURVIVED -> LINES_NEEDING_BETTER_TESTING
 - [√] Change NO_COVERAGE -> LINES_NOT_TESTED

[√] Show only actionable info under Mutation List tab
[√] Option to Expand All in the beginning
    [√] Currently set to false to avoid UI glitches

[√] Create packages
    [] (not needed) Comment out all failing test cases

[√] Add VT Defaults intro text after:
    - "Select the mutators used to alter the code"

### Pit
[√] Remove Line coverage, Test strength
    Pit Test Coverage Report
    Report generated by PIT 1.6.8
[√] Update feedback 
    [√] Remove conditional
    [√] AOD

[√] Update MT Group names
    [√] SURVIVE -> LINES_NEEDING_BETTER_TESTING
    [√] NO_COVERAGE -> LINES_NOT_TESTED


* [Maven build goals](https://www.baeldung.com/maven-goals-phases):
 - `mvn clean` - removes all files generated by the previous build
 - `mvn install` - compiles, tests, packages and installs the project's artifact into the local repository
 - `mvn package` - compiles, tests and packages the project's artifact into the local repository
 - √ `mvn clean package` - removes all files generated by the previous build, compiles, tests and packages the project's artifact into the local repository

## Troubleshooting techniques:
* If there is any compilation problem, delete the project from the Eclipse workspace and re-open it again. 
    - This solved PitView finding issue 


  public static Collection<MethodMutatorFactory> oldDefaults() {
    return group(InvertNegsMutator.INVERT_NEGS_MUTATOR,
        ReturnValsMutator.RETURN_VALS_MUTATOR, MathMutator.MATH_MUTATOR,
        VoidMethodCallMutator.VOID_METHOD_CALL_MUTATOR,
        NegateConditionalsMutator.NEGATE_CONDITIONALS_MUTATOR,
        ConditionalsBoundaryMutator.CONDITIONALS_BOUNDARY_MUTATOR,
        IncrementsMutator.INCREMENTS_MUTATOR);
  }

* Issues Building Pitclipse using `mvn clean package`

[ERROR] Caused by: Type org.eclipse.tycho.build.TychoGraphBuilder not present
[ERROR] Caused by: org/eclipse/tycho/build/TychoGraphBuilder has been compiled by a more recent version of the Java Runtime (class file version 55.0), this version of the Java Runtime only recognizes class file versions up to 52.0

 - here, 52.0 means Java 8
    - 55.0 means Java 11
    - I had to change the Java version in the pom.xml file to 11
 = [Link to understand issue](https://community.sonarsource.com/t/java-runtime-class-file-version-55-0-this-version-of-the-java-runtime-only-recognizes-class-file/76290)
 = [YouTube video of solution](https://www.youtube.com/watch?v%253D6h2jdDdmU3Y)
  - Create new configuration for Maven build
  - set project 
  - set JRE to 11
  - set goals to `package`

 Resolving target definition file:/Users/ri/git/pitclipse%20v3.1/bundles/org.pitest/../../releng/org.pitest.pitclipse.target/org.pitest.pitclipse.target.target for environments=[linux/gtk/x86_64, win32/win32/x86_64, macosx/cocoa/x86_64], include source mode=honor, execution environment=StandardEEResolutionHints [executionEnvironment=OSGi profile 'JavaSE-11' { source level: 11, target level: 11}], remote p2 repository options=org.eclipse.tycho.p2.remote.RemoteAgent@7730da00...

 - This creates new target folders under each project
  - The new JARs are under the target folders
 Example: Building jar: /Users/ri/git/pitclipse v3.1/bundles/org.pitest/target/org.pitest-1.6.8-SNAPSHOT.jar

 - Issue cannot run `mvn clean`
    - this replaces the modified PIT JARs with the original PIT JARs

 - Need to find a way to create executable JARs for PitClipse
  = [Different ways to build JARs](https://stackoverflow.com/questions/574594/how-can-i-create-an-executable-runnable-jar-with-dependencies-using-maven)
   - tried the `maven-assembly-plugin` but only generates a Pitest JAR file with the dependencies

   org.pitest.pitclipse.runner.PitRunnerMain


Fixed failing test cases:
* [√] org.pitest.mutationtest.build.intercept.logging.LoggingCallsFilterTest
* [√] org.pitest.mutationtest.build.intercept.javafeatures.TryWithResourcesFilterTest
