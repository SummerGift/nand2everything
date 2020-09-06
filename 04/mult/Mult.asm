// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)


// set R0 to varible number and set R1 to varible times
    @0
    D=M

    @number
    M=D

    @1
    D=M

    @times
    M=D

    @result
    M=0

    @times
    D=M
    @OUTPUT
    D;JEQ

(LOOP)
    @result
    D=M
    @number
    D=D+M
    @result
    M=D

    @times
    M=M-1
    D=M
    @LOOP
    D;JNE

(OUTPUT)
    @result
    D=M
    @2
    M=D

(END)
    @END
    0;JMP
