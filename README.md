# Nand2Everything

Inspired by the **One Life One Chip** program, in which NUST 16 graduates participated, I decided to make a chip of my own and implement **Assembler**, **Virtual Machine**, **High Level Language**, **High Level Language Compiler** and an **Operating System** on top of it. It sounds crazy :), but it works, what can't be done in the software world, let's build a new one.

The original idea for this came to me one weekend afternoon when I first learned about the theory of origin of artificial neural networks (deep learning), also known as perceptual machines. In my subsequent explorations, I learned that two layers of perceptual machines could theoretically be used to build a computer. This is because it has been shown that a two-layer perceptron (strictly speaking, a perceptron that uses a nonlinear sigmoid function for the activation function) can represent an arbitrary function.

The principle behind this can be understood simply as follows: a computer can be reproduced simply by combining it with a non-gate, and a non-gate can be implemented using a perceptron. In other words, if a computer can be realized by combining with non-gates, then a computer can be represented by combining perceptual machines, and the combination of perceptual machines can be represented by superimposing multiple layers of single-layer perceptual machines.

This discovery sparked my curiosity so much that I decided to take action and make my own computer system, which is really awesome and I can't wait!

Why the name nand2everything, meaning, it all starts with the simplest of gates and non-gates, and then builds the entire computer system of everything. Of course, purely out of intense curiosity, in the final stage I will definitely try to run RT-Thread on my own chip as well (and then I suddenly realize that this is a very difficult thing to do, and seems to require rewriting a C compiler), but maybe I can add some abstraction layers to it to achieve this.

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

From now on, I plan to use java to develop the following projects, first to develop the VM, next is a high-level language compiler targeting VM code, and finally, develop an operating system for the first stage computer.  

### [Project 7 VM Ⅰ: Stack Arithemetic](07)
- [x] Stack Arithmetic
- [x] Memory Access

I start using Java to develop a program for project7, add more fun and challenge to the journey :) The project7 has been finished now, even the code writing with java is not so elegant，but it still open a new window for me, cool!

### [Project 8 VM II: Program Control](08)

This project is about design stack-based mechanisms for handling nested subroutine calls of procedural or object-oriented languages.

- [x] Program Flow
- [x] Function Calls

Now project 8 has been finished, it takes me a lot of effort to handle the recursion call. The problem is about -1 is great than 0 or not. Human will think -1 is less than zero, but the computer won't, the computer uses complement to represent numbers, so it will think -1 is great than 0, so when the recursion meet -1, it will go on the loop but not exit the recursion. So, this point should be noticed when implementing VM translator.

All right, let's go to the next project.

### [Project 9 High-Level Language](09)
