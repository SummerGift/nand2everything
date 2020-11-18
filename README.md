# Nand2Everything

受到国科大 16 级毕业生参与的 **一生一芯** 计划的启发与鼓舞，我决定也要制作一款属于自己的芯片，并且在其之上实现 **汇编编译器**、**虚拟机**、**高级语言**、**高级语言编译器** 以及一款属于自己的 **操作系统**。这听起来很疯狂 ：），但其实搞得定，在软件的世界里有什么做不到的呢，我们就造一个新的出来。

这件事最初的想法来自于一个周末的午后，我第一次了解到人工神经网络（深度学习）的起源理论，也就是感知机。在随后的探索过程中我了解到，理论上两层的感知机就能构建计算机。这是因为，已有研究证明 2 层感知机(严格地说是激活函数使用了非线性的 sigmoid 函数的感知机)可以表示任意函数。

这背后的原理可以简单这样理解，只要通过与非门的组合，就能再现计算机，而与非门可以使用感知机实现。也就是说，如果通过组合与非门可以实现计算机的话，那么通过组合感知机也可以表示计算机，感知机的组合可以通过叠加多层的单层感知机来表示。

这个发现大大点燃了我的好奇心，我决定行动起来，做一款属于自己的计算机系统，这真的很赞，我已经迫不及待了！

为什么起名叫 nand2everything，意思是，这一切从最简单的与非门开始，然后构建整个计算机系统的一切。当然，完全出于强烈的好奇心，在最后的阶段我一定会尝试将 RT-Thread 也在自己的芯片上跑起来（后续我突然意识到这是一件很难办到的事情，似乎需要重写一个 C 语言编译器），不过也许能在其中加入一些抽象层来实现这些。

## First Stage

The projects that have been completed are as follows:

### [Project 1 Boolean Logic](01)
- [x] And16 Or16 Not16
- [x] MUX16
- [x] DMUX16

### [Project 2 Boolean Arithmetic](02)
- [x] HalfAdder
- [x] FullAdder
- [x] Add16
- [x] ALU

### [Project 3 Sequential Logic](03)
- [x] 16-bit Register
- [x] Program Counter
- [x] RAM8 - RAM16K

### [Project 4 Machine Language](04)
- [x] Machine Language
- [x] Input/Output

### [Project 5 Computer Architecture](05)
- [x] Memory
- [x] CPU
- [x] Controller
- [x] Computer

### [Project 6 Assembler](06)
- [x] Assembler

Now I have completed the first phase of the construction process from the NAND gate to a complete computer, and wrote an assembler for it, which greatly satisfied my curiosity. It is really exciting :)

## Second Stage

From now on, I plan using java to develop the following projects, first to develop the VM, next is a high-level language compiler targeting for VM code, and finally develop a operation system for the first stage computer.  

### [Project 7 VM Ⅰ: Stack Arithemetic](07)
- [x] Stack Arithmetic
- [x] Memory Access

I start using Java to develop program since project7, add more fun and challenge to the jouney :) The project7 has been finished now, even the code writting with java is not so elegant，but it still open a new window for me, cool!

### [Project 8 VM II: Program Control](08)

This project is about design stack-based machanisms for handling nested subroutine calls of procedural or object-oriented languages.

- [x] Program Flow
- [x] Function Calls

Now project 8 has been finished, it takes me a lot of effort to handle recursion call. The problem is about -1 is great than 0 or not. Human will think -1 is less than zero, but computer won't, computer uses complement to represent numbers, so it will think -1 is great than 0, so when the recursion meet -1, it will go on loop but not exit the recursion. So, this point shoud be noticed when implement VM translator.

All right, let's go on next project.

### [Project 9 High-Level Language](09)
