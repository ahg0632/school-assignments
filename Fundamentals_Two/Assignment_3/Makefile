
output: PasswordDriver.o PasswordManager.o
	g++ PasswordDriver.o PasswordManager.o -o output

PasswordDriver.o: PasswordDriver.cpp
	g++ -c PasswordDriver.cpp

PasswordManager.o: PasswordManager.cpp PasswordManager.h
	g++ -c PasswordManager.cpp

clean:
	rm *.o output