同样的功能，使用 visitor 模式来实现。

```
antlr -visitor PropertyFile.g4
javac PropertyFile*.java TestPropertyFileVisitor.java
javaTestPropertyFileVisitort.properties
```