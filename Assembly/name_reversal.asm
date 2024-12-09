    .data
prompt:    .asciiz "Enter full name: "
output:    .ascii "reversed is "
newLine:   .asciiz "\n"
name:      .space 101
finalName: .space 101

    .text
    .globl main
main:    
    la $a0, prompt
    li $v0, 4
    syscall
    
    la $a0, name
    li $a1, 101
    li $v0, 8
    syscall
    
    la $s0, name
    
findE:    
    lb $t1, 0($s0)
    beqz $t1, revrse
    addi $s0, $s0, 1
    j findE
    
revrse:    
    addi $s0, $s0, -1
revrseloop:
    lb $t1, 0($s0)
    beq $t1, ' ', align1
    addi $s0, $s0, -1
    j revrseloop

align1:
    move $s4, $s0
    addi $s0, $s0, 1
    la $s2, finalName


copyL:
    lb $t1, 0($s0) 
    beqz $t1, align2
    sb $t1, 0($s2)
    addi $s0, $s0, 1
    addi $s2, $s2, 1
    j copyL

align2:    
    li $t7, ','
    sb $t7, -1($s2)

    li $t6, ' '
    sb $t6, 0($s2)
    addi $s2, $s2, 1
    
    la $t3, name
    
copyF:    
    lb $t5, 0($t3)
    beq $t3, $s4, align3
    sb $t5, 0($s2)
    addi $s2, $s2, 1
    addi $t3, $t3, 1
    j copyF

align3:    
    sb $zero, 0($s2)
    
    li $v0, 4
    la $a0, name
    syscall
    
    li $v0, 4
    la $a0, output
    syscall
    
    li $v0, 4
    la $a0, finalName
    syscall

    li $v0, 10
    syscall
