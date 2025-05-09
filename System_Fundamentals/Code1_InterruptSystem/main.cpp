//
//  main.cpp
//  CS3360Code01
//
//  Created by Manouchehr Mohandesi on 1/19/25.
//  Name: Hector Guzman
//  ** Task to do: rewrite the following code by yourself and add your own comments and include what you have learned on each module.
#include <iostream>
#include <thread>
#include <atomic>
#include <chrono>

// IRQ codes
const char IRQ_Exit_Upper = 'E';
const char IRQ_Exit_Lower = 'e';
const char IRQ_Timer_Upper = 'T';
const char IRQ_Timer_Lower = 't';
const char IRQ_Keyboard_Upper = 'K';
const char IRQ_Keyboard_Lower = 'k';
const char IRQ_Network_Upper = 'N';
const char IRQ_Network_Lower = 'n';

// Intrupt Vector Table (Map IRQ to interrupt vector)
// Shouldn't this be a CONST since it's a global vector?
int interruptVectorTable[] = {10, 11, 12};

// Look up Table for ISR
// This is a new format I learned from this module
void (*interruptLookupTable[3])() = {nullptr};

//ISR function protoypes
void ISR_Timer();
void ISR_Keyboard();
void ISR_Network();

// function to simulate raising an intrrupt
void raiseInterupt(char irqKey){
    int irq =- 1;
    
    //Map IRQ to vector index (0, 1, or 2)
    if (irqKey == IRQ_Timer_Upper || irqKey == IRQ_Timer_Lower){
        irq = 0;
        
    }else if (irqKey == IRQ_Keyboard_Upper || irqKey == IRQ_Keyboard_Lower){
        irq = 1;
        
    }else if (irqKey == IRQ_Network_Upper || irqKey == IRQ_Network_Lower){
        irq = 2;
    }
    
    // Handle vaild IRQ
    if (irq != -1){
        int interruptVector = interruptVectorTable[irq];
        std::cout << "\n Interrupt raised! Vector:" << interruptVector << std::endl;
        if (interruptLookupTable[irq] != nullptr){
            interruptLookupTable[irq](); // Call the appropritate ISR
        } else{
            // Invalide key handler
            std::cout << "\n Invalide intrrupt key!, \n Valide keys 'T: Timer', 'K: Keyboard', 'N: Network'\n";
        }
        
    }
}

// Timer Interrupt ISR
void ISR_Timer(){
    std::cout << " Performing Timer Interrupt ...\n";
}

// Keyboard Interrupt ISR
void ISR_Keyboard(){
    std::cout << " Performing Keyboard Interrupt ...\n";
}

// Network Interrupt ISR
void ISR_Network(){
    std::cout << " Performing Network Interrupt ...\n";
}

// Function to check for keyboard input (Asynchronosly)
void checkForInput(std::atomic<bool>& interruptSignal, char& inputKey){
    while (!interruptSignal){
        if(std::cin >> inputKey){
            interruptSignal = true;
        }
    }
}

int main(){
    // intialize the interrupt look up table
    interruptLookupTable[0]=ISR_Timer;
    interruptLookupTable[1]=ISR_Keyboard;
    interruptLookupTable[2]=ISR_Network;
    
    //variable to store the key pressed
    char key;
    
    // Atomic flag to detect when an interrupt signal has been raised
    std::atomic<bool> interruptSignal(false);
    
    //Main loop to simulate normal system operation
    while (true){
        // create a thread to check for keyboard input asynchronosly
        std::thread inputThread(checkForInput, std::ref(interruptSignal), std::ref(key));
        
        // loop to perform normal tasks unitl interrupt is signaled
        while (!interruptSignal){
            // perform normal tasks while waiting for user input
            std::cout << "-=- System is performing normal tasks... -=- \n";
            std::this_thread::sleep_for(std::chrono::seconds(2));
        }

        // wait for the input thread to finish
        inputThread.join();
        
        // Raise an interupt base on the key pressed
        if(key == IRQ_Exit_Upper || key == IRQ_Exit_Lower){
            std::cout << " Exit key pressed. Ending the program! \n";
            break;
        }else{
            raiseInterupt(key);
            // Reset the interrupt signal to the continue normal operations
            interruptSignal = false;
        }
    }
    return 0;
}
