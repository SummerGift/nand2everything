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

![image-20230820195300386](./figures/image-20230820195300386.png)

![image-20230820195328233](./figures/image-20230820195328233.png)

使用 pointer 0 来设置想要访问的对象的基地址，然后使用 this 0 来访问这些数据，如果是想要访问数组，则使用 that 代替 this。

### Handling Objects Construction

![image-20230820195621211](./figures/image-20230820195621211.png)



![image-20230820195723143](./figures/image-20230820195723143.png)

![image-20230820195737999](./figures/image-20230820195737999.png)

![image-20230820195814633](./figures/image-20230820195814633.png)

![image-20230820195839376](./figures/image-20230820195839376.png)

计算对象存放所需要的内存空间后（包括私有数据），使用 `Memory.alloc` 申请用于存放内核对象的地址空间），在 constructor 的最后，将对象的基地址返回。

## Testing

###  Seven

这个测试用于检查编译器处理简单的程序，包括整数的数学计算，一个 do 语句，还有一个 return 语句。

### Conversion to binary

测试编译器处理 jack 语言的过程调用元素，比如表达式，函数和所有的语句类型（if、let、while 等）。

### Square Dance

测试编译器处理面向对象的构建元素，例如 constructors, methods, fields and expressions that include method calls.



