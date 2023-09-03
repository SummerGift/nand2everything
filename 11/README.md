# project 11 Code Generation

![image-20230730110149465](./figures/image-20230730110149465.png)

## Variables

![image-20230730110502989](./figures/image-20230730110502989.png)

### Symbol Table

![image-20230730110513759](./figures/image-20230730110513759.png)

![image-20230730110615196](./figures/image-20230730110615196.png)

![image-20230730110739204](./figures/image-20230730110739204.png)

![image-20230730110853624](./figures/image-20230730110853624.png)

### Handling Objects: Low-Level Aspects

![image-20230820195211147](./figures/image-20230820195211147.png)

`pointer 0` 和 `pointer 1` 只是锚点，用于设置某一个对象或者数组的基地址，设置完基地址后，可以用 this 和 that 指针来访问对象或者数组中的数据。可以用变量来存储某个对象或者数组的基地址，在需要操作该对象和数组的时候，将变量的值 pop 到 pointer 0 或者 pointer 1。

![image-20230820195300386](./figures/image-20230820195300386.png)

![image-20230820195328233](./figures/image-20230820195328233.png)

使用 pointer 0 来设置想要访问的对象的基地址，然后使用 this 0 来访问这些数据，如果是想要访问数组，则使用 that 代替 this。

### Handling Objects Construction

当处理对象构建的时候，需要考虑两方面，分别是构建函数的调用方 caller 和被调用的构建函数（callee）如何处理。

![image-20230820195621211](./figures/image-20230820195621211.png)

从 caller 的角度来看，需要将 callee 所需要的数据 push 入栈，callee 会申请好 obj 所需的内存，然后将其基地址返回给 caller。

![image-20230820195723143](./figures/image-20230820195723143.png)

在编译阶段，构建符号表的时候，已经将局部变量映射到栈上，在运行阶段，constructor 的代码会在堆上创建对象。

![image-20230820195737999](./figures/image-20230820195737999.png)

在 constructor 代码开始的地方，需要开始创建 subroutine 的符号表，接下来编译器需要统计这个对象需要占用多大内存，然后调用 `memory.alloc` 来分配，最后将内存的基地址 `pop pointer 0`（这个 pointer 0 只是锚点，用于锚定堆上的一块内存，后续可以通过 this 指针来访问），便于接下来使用 this 指针来访问对象的私有变量。

![image-20230820195814633](./figures/image-20230820195814633.png)

在 constructor 的最后，通过 `push pointer 0` 将对象的首地址返回，便于 caller 将其存放到其他变量中。

![image-20230820195839376](./figures/image-20230820195839376.png)

### Handling Object Manipulation

最终的机器语言是过程式的，因此编译器需要将面向对象的方法转换为过程调用，对象本身总是被当做第一个参数被传入，这种传入是隐式的。所以在编译 method 时，第一步就需要将默认传入的第一个参数压栈，然后执行 `pop pointer 0`，然后就可以用 this 指针来操作该对象中的数据。

![image-20230827101628251](./figures/image-20230827101628251.png)

在 caller 调用时，需要先将 obj 作为第一个（隐式）参数入栈，然后再将其他 argument 入栈， 最后调用 method 来操作相应的 obj。

![image-20230827102432626](./figures/image-20230827102432626.png)

method 被设计用来操作当前对象，因此每一个 method 都会访问对象的私有数据，method 可以使用 `this i-th` 指针来访问 obj 的 field 数据，首先需要将对象的基地址写入到 pointer 指针。 

![image-20230827103121962](./figures/image-20230827103121962.png)

caller code 和 method code 是单独编译的。

编译器会构建 class 和 method 级别的符号表，但是并不生成任何代码。

![image-20230827103543049](./figures/image-20230827103543049.png)

下图我觉得可能有问题，完成 method 调用后应当将返回的结果 `pop d`，而不是 push d。

![image-20230827110159259](./figures/image-20230827110159259.png)

Compiling a method that return void。

![image-20230827111354382](./figures/image-20230827111354382.png)

void method should return a dummy value. Callers of void methods are responsible for removing the returned value from the stack.

![image-20230827111159310](./figures/image-20230827111159310.png)

### Handing Array

#### Creating arrays

![image-20230903174315054](./figures/image-20230903174315054.png)

#### Array Manipulating

将 array 的基地址和偏移量相加，pop 到 pointer 1 指针中，然后通过 that 0 访问数组元素。

![image-20230903174542681](./figures/image-20230903174542681.png)

尝试将访问数组元素的策略通用化：

![image-20230903174835171](./figures/image-20230903174835171.png)

但是当我们想将执行 `a[i] = b[j]` 时，发现需要多次使用 pointer 1 指针，因此会出现错误，需要引入 temp 段来临时存放元素地址：

![image-20230903175115489](./figures/image-20230903175115489.png)

![image-20230903175124795](./figures/image-20230903175124795.png)

通过这种方法，即使是处理复杂的 array 表达式也没有问题。

![image-20230903175150163](./figures/image-20230903175150163.png)



## Testing

###  Seven

这个测试用于检查编译器处理简单的程序，包括整数的数学计算，一个 do 语句，还有一个 return 语句。

### Conversion to binary

测试编译器处理 jack 语言的过程调用元素，比如表达式，函数和所有的语句类型（if、let、while 等）。

### Square Dance

测试编译器处理面向对象的构建元素，例如 constructors, methods, fields and expressions that include method calls.

```
print(f"Args num: {args_num}, Function: {inspect.currentframe().f_code.co_name}, Line number: {inspect.currentframe().f_lineno}")

```
