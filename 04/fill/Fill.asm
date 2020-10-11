// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

(START)
    @24576
    D=M

    @TURNBLACK
    D;JNE

    @TURNCLEAR
    D;JEQ

(TURNBLACK)
    @SCREEN
    D=A
    @arr
    M=D

    @8192
    D=A
    @n
    M=D

    @i
    M=0

(DOBLACK_LOOP)
    // if i == n, then return to start
    @i
    D=M
    @n
    D=D-M
    @START
    D;JEQ

    @arr
    D=M
    @i
    A=D+M
    M=-1

    // i++
    @i
    M=M+1

    @DOBLACK_LOOP
    0;JMP

(TURNCLEAR)
    @SCREEN
    D=A
    @arr
    M=D

    @8192
    D=A
    @n
    M=D

    @i
    M=0

(DOCLEAR_LOOP)
    // if i == n, then return to start
    @i
    D=M
    @n
    D=D-M
    @START
    D;JEQ

    @arr
    D=M
    @i
    A=D+M
    M=0

    // i++
    @i
    M=M+1

    @DOCLEAR_LOOP
    0;JMP
