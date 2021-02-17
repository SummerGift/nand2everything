# 编译器实现

## ANTLR 安装配置方法

### Windows

安装 java 开发环境，修改 antlr_jar 文件夹下的 bat 文件，将 jar 文件的指定路径修改正确，
然后将 bat 文件所在目录加入系统环境变量，即可使用 antlr 与 grun 命令开发与验证语法分析应用。

## ANTLR 使用方法

### 利用 ANTLR 进行词法解析

- 编译词法规则

```
antlr Hello.g4
```
- 编译规则文件，生成词法分析器

```
javac *.java
```

- 调用生成的词法分析器，打印出对 hello.txt 的词法分析结果

```shell
grun Hello tokens -tokens hello.txt
```

### 利用 ANTLR 进行语法解析

在语法解析前需要先进行词法解析，将输入的文本提取为一个个 token，然后再进行语法分析，生成抽象语法树即 AST。所以语法解析一般都会引用先前编写的词法解析规则，然后在此基础上扩展语法规则。

一般情况下，如果词法解析文件比较复杂，那么会将词法解析规则单独放置在一个文件中，然后在语法解析规则文件中引用词法解析规则文件。当然如果词法解析非常简单，那么自然可以将词法解析与语法解析放置在一个文件中。

- 生成解析器与词法分析器

  ```
  antlr Hello.g4
  ```

- 编译规则文件，生成语法分析器

  ```
  javac *.java
  ```

- 利用 `grun` 验证规则文件

  ```shell
  grun Hello r -tokens
  ```

  ```
  grun Hello r -tree
  ```

  ```shell
  grun Hello r -tree -gui # 图形化的方式展示语法树
  ```
