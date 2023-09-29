# Project 12

## Math

![image-20230924113142758](./figures/image-20230924113142758.png)

### Multiplication

首先实现乘法运算，本质上乘法就是 y 个 x 相加：

![image-20230924113338503](./figures/image-20230924113338503.png)

下面这个算法使用位运算来实现乘法操作，相当于每次执行加法计算前将 x 的值左移一位作为 `shiftedX`，然后根据 `i'th bit of y` 是否为 1 决定是否将 `shiftedX` 加入 sum。

![image-20230924113839053](./figures/image-20230924113839053.png)

第一种实现方式，在运行时为线性复杂度，第二种实现方式为 log，其计算量增长速度远比第一种要低得多。

![image-20230924113852967](./figures/image-20230924113852967.png)

### Division

除法的基本思路是看看被除数可以被减掉多少次除数（repetitive subtraction），这种算法的算法复杂度和 x 成正比，计算量很大。

![image-20230924120512452](./figures/image-20230924120512452.png)

下面算法的思路是每次让被除数减去一个除数在指定位置的最大倍数。

![image-20230924120847416](./figures/image-20230924120847416.png)

![image-20230924120933084](./figures/image-20230924120933084.png)

如下实现思路，使用递归算法，每次让 y * 2^N 记作 temp_y，最终找到 y * (2^N) > x > y * 2^(N-1) 时，使得 x - y * 2^(N-1)，依次递归下去直到剩余的数字小于 y。

![image-20230924122231487](./figures/image-20230924122231487.png)

## Memory

![image-20230929154917003](./figures/image-20230929154917003.png)

### Heap management

最简单的做法找到空闲的内存，只申请但不释放。

![image-20230929155149343](./figures/image-20230929155149343.png)

还可以使用 linked list 来追踪可用的内存片段：

![image-20230929155403014](./figures/image-20230929155403014.png)

![image-20230929155422083](./figures/image-20230929155422083.png)

![image-20230929155455169](./figures/image-20230929155455169.png)

![image-20230929155548592](./figures/image-20230929155548592.png)

![image-20230929155840702](./figures/image-20230929155840702.png)







![image-20230929155901594](./figures/image-20230929155901594.png)

## Screen

![image-20230929170037059](./figures/image-20230929170037059.png)

![image-20230929170102854](./figures/image-20230929170102854.png)

### Pixel drawing

![image-20230929170117347](./figures/image-20230929170117347.png)

### line drawing

![image-20230929170515233](./figures/image-20230929170515233.png)

![image-20230929170537104](./figures/image-20230929170537104.png)

![image-20230929170548910](./figures/image-20230929170548910.png)

### circle drawing

![image-20230929170727429](./figures/image-20230929170727429.png)

![image-20230929170852496](./figures/image-20230929170852496.png)

## Output

在当前平台上，总共有 23 行，每一行可以写下 64 个字符。

![image-20230929180016969](./figures/image-20230929180016969.png)

### character set

![image-20230929173942073](./figures/image-20230929173942073.png)

![image-20230929174010265](./figures/image-20230929174010265.png)

![image-20230929174153751](./figures/image-20230929174153751.png)

![image-20230929174406658](./figures/image-20230929174406658.png)

## Keyboard

![image-20230929181222706](./figures/image-20230929181222706.png)
