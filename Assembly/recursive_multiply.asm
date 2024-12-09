.data
prompt:		.asciiz "Enter the multiplicand: "
multiplicand:	.word 0
prompt1:	.asciiz "Enter the multiplier: "
multiplier:	.word 0

operandString:	.asciiz " * "
equalsString:	.asciiz " = "

.text
main:
	li $v0, 4
	la $a0, prompt
	syscall
	li $v0, 5
	syscall
	sw $v0, multiplicand
	
	li $v0, 4
	la $a0, prompt1
	syscall
	li $v0, 5
	syscall
	sw $v0, multiplier
	
	lw $a0, multiplicand
	li $v0, 1
	syscall
	li $v0, 4
	la $a0, operandString
	syscall
	lw $a0, multiplier
	li $v0, 1
	syscall
	li $v0, 4
	la $a0, equalsString
	syscall
	
	lw $a0, multiplicand
	lw $a1, multiplier
	jal recursiveCall
	
	move $a0, $v0
	li $v0, 1
	syscall
	
	li $v0, 10
	syscall
	
recursiveCall:
	addi $sp, $sp, -12
	sw $ra, 0($sp)
	sw $a0, 4($sp)
	sw $a1, 8($sp)
	beq $a1, $zero, S1
	addi $a1, $a1, -1
	jal recursiveCall
	
	lw $a0, 4($sp)
	lw $a1, 8($sp)
	lw $ra, 0($sp)
	add $v0, $a0, $v0
	addi $sp, $sp, 12
	jr $ra
S1:
	move $v0, $zero
	lw $ra, 0($sp)
	addi $sp, $sp, 12
	jr $ra