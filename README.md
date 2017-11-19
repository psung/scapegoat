Scapegoat
=========

[![travis image](https://travis-ci.org/sksamuel/scapegoat.svg?branch=master)](https://travis-ci.org/sksamuel/scapegoat)
[<img src="https://img.shields.io/maven-central/v/com.sksamuel.scapegoat/scalac-scapegoat-plugin_2.11.svg?label=latest%20release%20for%202.11"/>](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22scalac-scapegoat-plugin_2.11%22)
[<img src="https://img.shields.io/maven-central/v/com.sksamuel.scapegoat/scalac-scapegoat-plugin_2.12.svg?label=latest%20release%20for%202.12"/>](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22scalac-scapegoat-plugin_2.12%22)

Scapegoat is a Scala static code analyzer, what is more colloquially known as a code lint tool or linter. Scapegoat works in a similar vein to Java's [FindBugs](http://findbugs.sourceforge.net/) or [checkstyle](http://checkstyle.sourceforge.net/), or Scala's [Scalastyle](https://github.com/scalastyle/scalastyle).

A static code analyzer is a tool that flag suspicious language usage in code. This can include behavior likely to lead or bugs, non idiomatic usage of a language, or just code that doesn't conform to specified style guidelines.

**What's the difference between this project and Scalastyle (or others)?**

Scalastyle is a similar linting tool which focuses mostly on enforcing style/code standards. There's no problems running multiple analysis tools on the same codebase. In fact it could be beneficial as the total set of possible warnings is the union of the inspections of all the enabled tools. The worst case is that the same warnings might be generated by multiple tools.

### Usage
Scapegoat is developed as a scala compiler plugin, which can then be used inside your build tool.

#### SBT
See: [sbt-scapegoat](https://github.com/sksamuel/sbt-scapegoat) for SBT integration.

#### Maven

Firstly you need to add scapegoat plugin as a dependency:

```xml
<dependency>
    <groupId>com.sksamuel.scapegoat</groupId>
    <artifactId>scalac-scapegoat-plugin_${scala.binary.version}</artifactId>
    <version>1.3.3</version>
</dependency>
```

Then configure `scala-maven-plugin` by adding `compilerPlugin`

```xml
<plugin>
    <groupId>net.alchim31.maven</groupId>
    <artifactId>scala-maven-plugin</artifactId>
    <configuration>
        <args>
            <arg>-P:scapegoat:dataDir:./target/scapegoat</arg>
        </args>
        <compilerPlugins>
            <compilerPlugin>
                <groupId>com.sksamuel.scapegoat</groupId>
                <artifactId>scalac-scapegoat-plugin_${scala.binary.version}</artifactId>
                <version>1.3.3</version>
            </compilerPlugin>
        </compilerPlugins>
    </configuration>
</plugin>
```
The only required parameter is `dataDir` (where report will be generated):

`<arg>-P:scapegoat:dataDir:./target/scapegoat</arg>`

You can pass other configuration flags same way, e.g.

`<arg>-P:scapegoat:disabledInspections:FinalModifierOnCaseClass</arg>`

Note: You may use a separate maven profile, so that the dependency doesn't go to you runtime classpath.

### Reports

Here is sample output from the console during the build for a project with warnings/errors:

```
[warning] [scapegoat] Unused method parameter - org.ensime.util.ClassIterator.scala:46
[warning] [scapegoat] Unused method parameter - org.ensime.util.ClassIterator.scala:137
[warning] [scapegoat] Use of var - org.ensime.util.ClassIterator.scala:22
[warning] [scapegoat] Use of var - org.ensime.util.ClassIterator.scala:157
[   info] [scapegoat]: Inspecting compilation unit [FileUtil.scala]
[warning] [scapegoat] Empty if statement - org.ensime.util.FileUtil.scala:157
[warning] [scapegoat] Expression as statement - org.ensime.util.FileUtil.scala:180

```

And if you prefer a prettier report, here is a screen shot of the type of HTML report scapegoat generates:

![screenshot](https://raw.githubusercontent.com/sksamuel/scapegoat/master/screenshot1.png)

### Configuration

For instructions on suppressing warnings by file, by inspection or by line see [the sbt-scapegoat README](https://github.com/sksamuel/sbt-scapegoat).

### False positives

Please note that scapegoat is a new project. While it's been tested on some common open source projects, there is still a good chance you'll find false positives. Please open up issues if you run into these so we can fix them.

### Changelog

* **0.94.0** - Fixed some more false positives. Added MethodNames inspection, StripMarginOnRegex inspection, and VariableShadowing inspection (the latter being a work in progress, please report feedback).

* **0.93.2** - Fixed false positives.

* **0.93.1** - #67 fixed var args in duplicate map check, #66 ignoring methods returning nothing when checking for unused params, #69 fixed extended classes false pos, #73 Removed incorrect inspection, #64 updated suppression to use tree.symbol.isSynthetic instead of mods.synth, Merge pull request #77 from paulp/psp, Give access to the inspection logic through the sbt console, #76 Improve the contains test.

* **0.93.0** - Added ability to define multiple traversers that run in separate phases of the compiler, #58 Updated suppression to work on objects and classes, #60 handling case objects in suspicious match on class object, Allow all inspections to be disabled, other fixes

* **0.92.2** - Added debug option, Made summary optional and disabled in tests, Improved var could be val #54, Split null inspections into assignment and invocation #53, Bumped count on operators to > 2, loads of fixes, loads of verboseness removed.

* **0.92.1** - Fixed a load of false positives.

* **0.92.0** - Added swallowed exception inspection, Added public finalizer inspection, Added use expm1(x) instead of exp(x) - 1 inspection, Added use log1p(x) instead of log(x + 1) inspection, Added use log10(x) instead of log(x)/log(10) inspection, Added use cbrt inspection

* **0.91.0** - Updated logging format to include less [scapegoat] everywhere, Added scala.math and java.StrictMath to useSqrt, Added ignored files patterns option, Added wildcard import inspection, Added comparison to empty set inspection

* **0.90.17** - Added looks like interpolated string inspection, Added SuspiciousMatchOnClassObject inspection, Updated varuse to not warn on vars in actors #46, Added comparison to empty list inspection, #37 Changed emptyinterpolated string to error, #37 Fixed warning on max parameters

* **0.90.14** - Bunch of bug fixes for false positives. No new inspections.

* **0.90.13** - Fixed NPE in VarClosure inspection, Added Object Names inspection, Added classnames inspection, Added avoid to minus one inspection.

* **0.90.12** - New inspections: unnecessary override, duplicate import, pointless type bounds, max parameters, var closure, method returning any. Updated repeated case body to ignore bodies with two or less statements #28. Removed false positives on getter/setter #27.

* **0.90.11** - Added empty for inspection, AnyUse inspection, Added ArrayEquals inspection, Added double negation inspection, Disabled expression as statement inspection by default, Added avoid operator overload inspection, #25 improving repeated case bodies, Added lonely sealed trait. Added postInspection call to inspections

* **0.90.10** - Added type shadowing inspection, var could be val inspection, unreachable catch inspection and unnecessary toString inspection

* **0.90.09** - Added new inspections: bounded by final type, empty while block, prefer vector empty, finalizer without super, impossible option size condition, filter dot head, repeated case body. Added `infos` to HTML output header

* **0.90.8** - Fixed erroneous partial functions inspection. Added inspection for empty case class. Changed levels in output to lowercase. Added console output option. Fixed seq empty on non empty seq. Changed return usage to info. Fixed odd issue with empty tree. Changed unused parameter in override to be info. Ignoring all synthetic method added to case classes. Fixed while(true) being detected by ConstantIf

### Inspections

There are currently 116 inspections. An overview list is given, followed by a more detailed description of each inspection after the list (todo: finish rest of detailed descriptions)

| Name | Brief Description | Default Level |
|------|-------------------|---------------|
| ArrayEquals | Checks for comparison of arrays using `==` which will always return false | Info |
| ArraysInFormat | Checks for arrays passed to String.format | Error |
| ArraysToString | Checks for explicit toString calls on arrays | Warning |
| AsInstanceOf | Checks for use of `asInstanceOf` | Warning |
| AvoidOperatorOverload | Checks for mental symbolic method names | Info |
| AvoidSizeEqualsZero | Traversable.size can be slow for some data structure, prefer .isEmpty | Warning |
| AvoidSizeNotEqualsZero | Traversable.size can be slow for some data structure, prefer .nonEmpty | Warning |
| AvoidToMinusOne | Checks for loops that use `x to n-1` instead of `x until n` | Info |
| BigDecimalDoubleConstructor | Checks for use of `BigDecimal(double)` which can be unsafe | Warning |
| BigDecimalScaleWithoutRoundingMode | `setScale()` on a `BigDecimal` without setting the rounding mode can throw an exception | Warning |
| BoundedByFinalType | Looks for types with upper bounds of a final type | Warning |
| BrokenOddness | checks for a % 2 == 1 for oddness because this fails on negative numbers | Warning |
| CatchException | Checks for try blocks that catch Exception | Warning |
| CatchFatal | Checks for try blocks that catch fatal exceptions: VirtualMachineError, ThreadDeath, InterruptedException, LinkageError, ControlThrowable | Warning |
| CatchNpe | Checks for try blocks that catch null pointer exceptions | Error |
| CatchThrowable | Checks for try blocks that catch Throwable | Warning |
| ClassNames | Ensures class names adhere to the style guidelines | Info |
| CollectionIndexOnNonIndexedSeq | Checks for indexing on a Seq which is not an IndexedSeq | Warning |
| CollectionNamingConfusion | Checks for variables that are confusingly named | Info |
| CollectionNegativeIndex | Checks for negative access on a sequence eg `list.get(-1)` | Warning |
| CollectionPromotionToAny | Checks for collection operations that promote the collection to `Any` | Warning |
| ComparingFloatingPointTypes | Checks for equality checks on floating point types | Error |
| ComparingUnrelatedTypes | Checks for equality comparisons that cannot succeed | Error |
| ComparisonToEmptyList | Checks for code like `a == List()` or `a == Nil` | Info |
| ComparisonToEmptySet | Checks for code like `a == Set()` or `a == Set.empty` | Info |
| ComparisonWithSelf | Checks for equality checks with itself | Warning |
| ConstantIf | Checks for code where the if condition compiles to a constant | Warning |
| DivideByOne | Checks for divide by one, which always returns the original value | Warning |
| DoubleNegation | Checks for code like `!(!b)` | Info |
| DuplicateImport | Checks for import statements that import the same selector | Info |
| DuplicateMapKey | Checks for duplicate key names in Map literals | Warning |
| DuplicateSetValue | Checks for duplicate values in set literals | Warning |
| EitherGet | Checks for use of .get on Left or Right | Error |
| EmptyCaseClass | Checks for case classes like `case class Faceman()` | Info |
| EmptyFor | Checks for empty `for` loops | Warning |
| EmptyIfBlock | Checks for empty `if` blocks | Warning |
| EmptyInterpolatedString | Looks for interpolated strings that have no arguments | Error |
| EmptyMethod | Looks for empty methods | Warning |
| EmptySynchronizedBlock | Looks for empty synchronized blocks | Warning |
| EmptyTryBlock | Looks for empty try blocks | Warning |
| EmptyWhileBlock | Looks for empty while loops | Warning |
| ExistsSimplifiableToContains | `exists(x => x == b)` replaceable with `contains(b)` | Info |
| FilterDotHead | `.filter(x => ).head` can be replaced with `find(x => ) match { .. } ` | Info |
| FilterDotHeadOption | `.filter(x =>).headOption` can be replaced with `find(x => )` | Info |
| FilterDotIsEmpty | `.filter(x => ).isEmpty` can be replaced with `!exists(x => )` | Info |
| FilterDotSize | `.filter(x => ).size` can be replaced more concisely with with `count(x => )` | Info |
| FilterOptionAndGet | `.filter(_.isDefined).map(_.get)` can be replaced with `flatten` | Info |
| FinalModifierOnCaseClass | Using Case classes without `final` modifier can lead to surprising breakage | Info |
| FinalizerWithoutSuper | Checks for overridden finalizers that do not call super | Warning |
| FindAndNotEqualsNoneReplaceWithExists | `.find(x => ) != None` can be replaced with `exist(x => )` | Info |
| FindDotIsDefined | `find(x => ).isDefined` can be replaced with `exist(x => )` | Info |
| IllegalFormatString | Looks for invalid format strings | Error |
| ImpossibleOptionSizeCondition | Checks for code like `option.size > 2` which can never be true | Error |
| IncorrectNumberOfArgsToFormat | Checks for wrong number of arguments to `String.format` | Error |
| IncorrectlyNamedExceptions | Checks for exceptions that are not called *Exception and vice versa | Error |
| InvalidRegex | Checks for invalid regex literals | Info |
| IsInstanceOf | Checks for use of `isInstanceOf` | Warning |
| JavaConversionsUse | Checks for use of implicit Java conversions | Warning |
| ListAppend | Checks for List :+ which is O(n) | Info |
| ListSize | Checks for `List.size` which is O(n). | Info |
| LonelySealedTrait | Checks for sealed traits which have no implementation | Error |
| LooksLikeInterpolatedString | Finds strings that look like they should be interpolated but are not | Warning |
| MapGetAndGetOrElse | `Map.get(key).getOrElse(value)` can be replaced with `Map.getOrElse(key, value)` | Error |
| MaxParameters | Checks for methods that have over 10 parameters | Info |
| MethodNames | Warns on method names that don't adhere to the Scala style guidelines | Info |
| MethodReturningAny | Checks for defs that are defined or inferred to return `Any` | Warning |
| ModOne | Checks for `x % 1` which will always return `0` | Warning |
| NanComparison | Checks for `x == Double.NaN` which will always fail | Error |
| NegationIsEmpty | `!Traversable.isEmpty` can be replaced with `Traversable.nonEmpty` | Info |
| NegationNonEmpty | `!Traversable.nonEmpty` can be replaced with `Traversable.isEmpty` | Info |
| NoOpOverride | Checks for code that overrides parent method but simply calls super | Info |
| NullAssignment | Checks for use of `null` in assignments | Warning |
| NullParameter | Checks for use of `null` in method invocation | Warning |
| ObjectNames | Ensures object names adhere to the Scala style guidelines | Info |
| OptionGet | Checks for `Option.get` | Error |
| OptionSize | Checks for `Option.size` | Error |
| ParameterlessMethodReturnsUnit | Checks for `def foo : Unit` | Warning |
| PartialFunctionInsteadOfMatch | Warns when you could use a partial function directly instead of a match block | Info |
| PointlessTypeBounds | Finds type bounds of the form `[A <: Any]` or `[A >: Nothing]`| Warning |
| PreferSeqEmpty | Checks for Seq() when could use Seq.empty | Info |
| PreferSetEmpty | Checks for Set() when could use Set.empty | Info |
| ProductWithSerializableInferred | Checks for vals that have `Product with Serializable` as their inferred type | Warning |
| PublicFinalizer | Checks for overridden finalizes that are public | Info |
| RedundantFinalModifierOnMethod | Redundant `final` modifier on method that cannot be overridden | Info |
| RedundantFinalModifierOnVar | Redundant `final` modifier on var that cannot be overridden | Info |
| RedundantFinalizer | Checks for empty finalizers. | Warning |
| RepeatedCaseBody | Checks for case statements which have the same body | Warning |
| ReverseFunc | `reverse` followed by `head`, `headOption`, `iterator`, or`map` can be replaced, respectively, with `last`, `lastOption`, `reverseIterator`, or `reverseMap` | Info |
| ReverseTailReverse | `.reverse.tail.reverse` can be replaced with `init` | Info |
| ReverseTakeReverse | `.reverse.take(...).reverse` can be replaced with `takeRight` | Info |
| SimplifyBooleanExpression | `b == false` can be simplified to `!b` | Info |
| StripMarginOnRegex | Checks for .stripMargin on regex strings that contain a pipe | Error |
| SubstringZero | Checks for `String.substring(0)` | Info |
| SuspiciousMatchOnClassObject | Finds code where matching is taking place on class literals | Warning |
| SwallowedException | Finds catch blocks that don't handle caught exceptions | Warning |
| SwapSortFilter | `sort.filter` can be replaced with `filter.sort` for performance | Info |
| TraversableHead | Looks for unsafe usage of `Traversable.head` | Error |
| TraversableLast | Looks for unsafe usage of `Traversable.last` | Error |
| TryGet | Checks for use of `Try.get` | Error |
| TypeShadowing | Checks for shadowed type parameters in methods | Warning |
| UnnecessaryIf | Checks for code like `if (expr) true else false` | Info |
| UnnecessaryReturnUse | Checks for use of `return` keyword in blocks | Info |
| UnnecessaryToInt | Checks for unnecessary `toInt` on instances of Int | Warning |
| UnnecessaryToString | Checks for unnecessary `toString` on instances of String | Warning |
| UnreachableCatch | Checks for catch clauses that cannot be reached | Warning |
| UnsafeContains | Checks for `List.contains(value)` for invalid types | Error |
| UnusedMethodParameter | Checks for unused method parameters | Warning |
| UseCbrt | Checks for use of `math.pow` for calculating `math.cbrt` | Info |
| UseExpM1 | Checks for use of `math.exp(x) - 1` instead of `math.expm1(x)` | Info |
| UseLog10 | Checks for use of `math.log(x)/math.log(10)` instead of `math.log10(x)` | Info |
| UseLog1P | Checks for use of `math.log(x + 1)` instead of `math.log1p(x)` | Info |
| UseSqrt | Checks for use of `math.pow` for calculating `math.sqrt` | Info |
| VarClosure | Finds closures that reference var | Warning |
| VarCouldBeVal | Checks for `var`s that could be declared as `val`s | Warning |
| WhileTrue | Checks for code that uses a `while(true)` or `do { } while(true)` block. | Warning |
| ZeroNumerator | Checks for dividing by 0 by a number, eg `0 / x` which will always return `0` | Warning |

##### Arrays to string

Checks for explicit toString calls on arrays. Since toString on an array does not perform a deep toString, like say scala's List, this is usually a mistake.

##### CollectionIndexOnNonIndexedSeq

Checks for calls of `.apply(idx)` on a `Seq` where the index is not a literal and the `Seq` is not an `IndexedSeq`.

*Rationale* If code which expects O(1) positional access to a Seq is given a non-IndexedSeq (such as a List, where indexing is O(n)) then this may cause poor performance.

##### ComparingUnrelatedTypes

Checks for equality comparisons that cannot succeed because the types are unrelated. Eg `"string" == BigDecimal(1.0)`. The scala compiler has a less strict version of this inspection.

##### ConstantIf

Checks for if statements where the condition is always true or false. Not only checks for the boolean literals, but also any expression that the compiler is able to turn into a constant value. Eg, `if (0 < 1) then else that`

##### IllegalFormatString

Checks for a format string that is not invalid, such as invalid conversions, invalid flags, etc. Eg, `"% s"`, `"%qs"`, `%.-4f"`

##### IncorrectNumberOfArgsToFormat

Checks for an incorrect number of arguments to String.format. Eg, `"%s %s %f".format("need", "three")` flags an error because the format string specifies 3 parameters but the call only provides 2.

##### InvalidRegex

Checks for invalid regex literals that would fail at compile time. Either dangling metacharacters, or unclosed escape characters, etc that kind of thing.

##### List size

Checks for .size on an instance of List. Eg, `val a = List(1,2,3); a.size`

*Rationale* List.size is O(n) so for performance reasons if .size is needed on a list that could be large, consider using an alternative with O(1), eg Array, Vector or ListBuffer.

##### Redundant finalizer

Checks for empty finalizers. This is redundant code and should be removed. Eg, `override def finalize : Unit = { }`

##### PreferSetEmpty

Indicates where code using Set() could be replaced with Set.empty. Set() instantiates a new instance each time it is invoked, whereas Set.empty returns a pre-instantiated instance.

##### UnnecessaryReturnUse

Checks for use of return in a function or method. Since the final expression of a block is always the return value, using return is unnecessary. Eg, `def foo = { println("hello"); return 12; }`

##### UnreachableCatch

Checks for catch clauses that cannot be reached. This means the exception is dead and if you want that exception to take precedence you should move up further up the case list.

##### UnsafeContains

Checks for `List.contains(value)` for invalid types. The method for contains accepts any types. This inspection finds situations when you have a list of type A and you are checking for contains on type B which cannot hold.

##### While true

Checks for code that uses a `while(true)` or `do { } while(true)` block.

*Rationale*: This type of code is usually not meant for production as it will not return normally. If you need to loop until interrupted then consider using a flag.

### Other static analysis tools:

* ScalaStyle (Scala) - https://github.com/scalastyle/scalastyle/wiki
* Linter (Scala) - https://github.com/HairyFotr/linter
* WartRemover (Scala) - https://github.com/puffnfresh/wartremover
* Findbugs (JVM) - http://findbugs.sourceforge.net/bugDescriptions.html
* Fb-contrib (JVM) - http://fb-contrib.sourceforge.net/
* CheckStyle (Java) - http://checkstyle.sourceforge.net/availablechecks.html
* PMD (Java) - http://pmd.sourceforge.net/pmd-5.0.3/rules/index.html
* Error-prone (Java) - https://github.com/google/error-prone
* CodeNarc (Groovy) - http://codenarc.sourceforge.net/codenarc-rule-index.html
* PVS-Studio (C++) - http://www.viva64.com/en/d/
* Coverity (C++) - http://www.slideshare.net/Coverity/static-analysis-primer-22874326 (6,7)
* CppCheck (C++) - http://cppcheck.sourceforge.net/
* OCLint (C++/ObjC) - http://docs.oclint.org/en/dev/rules/index.html
* JSHint (Javascript) - http://jshint.com/
* JavascriptLint (Javascript) - http://www.javascriptlint.com/
* ClosureLinter (Javascript) - https://developers.google.com/closure/utilities/
