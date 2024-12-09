.data
chars:	.ascii "ABCDEFGHIJKLMNOPQRSTUVWXYZ .,!-'"
msg1:	.word 0x93EA9646, 0xCDE50442, 0x34D29306, 0xD1F33720
	.word 0x56033D01, 0x394D963B, 0xDE7BEFA4
msglen:	.word 28
	
.text
main:
	la $s0, chars	#address of char bank
	la $s1, msg1	#address of array
	
	lw $t0, 0($s1)	#initial loading of array
	li $t5, 0	#buffer
	li $t6, 0	#bit counter
	lw $t7, msglen	#byte counter | exit condition
	
extract_bits:
	
	bgt $t6, 5, skip	#load next byte condition
	andi $t1, $t0, 0xFF	#grab the next byte in the 32bit hex
	srl $t0, $t0, 8		#remove the byte taken from original hex
	addi $t6, $t6, 8	#add to the bit counter a byte
	sll $t5, $t5, 8		#shift the buffer left one byte
	or $t5, $t5, $t1	#load the byte into the buffer
	subi $t7, $t7, 1	#remove from msglen for each loaded byte
skip:
	subi $t6, $t6, 5	#remove 5 bits from the bit counter that will be printed
	srlv $t1, $t5, $t6	#shift over by the bit counter
	andi $t1, $t1, 0x1F	#mask off for 5 bits
	
	add $t2, $t1, $s0	#create index from the 5 bits
	lb $a0, 0($t2)		#index to a character
	li $v0, 11		#print
	syscall
	
	beqz $t7, exit		#exit condition
	
	bnez $t0, no_index	#condition to index next 32 bit word
	addi $s1, $s1, 4
	lw $t0, 0($s1)
no_index:
	j extract_bits
	
exit:
	li $v0, 10
	syscall
	
