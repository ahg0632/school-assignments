#include <bitset>
#include <iostream>
#include <cmath>
#include <stdlib.h>
#include <cstdint>
#include <regex>


//  Print IEEE 32 bits
void printIEEE(float arg1, float arg2 = 0) {
    union {
        float f;
        uint32_t i;
    } u, g;
    u.f = arg1;
    g.f = arg2;
    
    uint32_t sign = (u.i >> 31) & 1;
    uint32_t exponent = (u.i >> 23) & 0xFF;
    uint32_t mantissa = u.i & 0x7FFFFF;

    if(arg2 != 0) {
        std::cout << "Loop bound:\t";
        std::cout << "\033[36m" << sign << "\033[0m " << "\033[31m" << std::bitset<8>(exponent) << "\033[0m " << "\033[32m" << std::bitset<23>(mantissa) << "\033[0m\n";

        sign = (g.i >> 31) & 1;
        exponent = (g.i >> 23) & 0xFF;
        mantissa = g.i & 0x7FFFFF;
        std::cout << "Loop counter:\t";
        std::cout << "\033[36m" << sign << "\033[0m " << "\033[31m" << std::bitset<8>(exponent) << "\033[0m " << "\033[32m" << std::bitset<23>(mantissa) << "\033[0m\n";
    } else{
        std::cout << "\t" << arg1 << "\n";
        std::cout << "\t\033[36m" << sign << "\033[0m " << "\033[31m" << std::bitset<8>(exponent) << "\033[0m " << "\033[32m" << std::bitset<23>(mantissa) << "\033[0m\n";
    }
    std::cout << "\n";
}

float Overflow() {
    return pow(2, 23.99999);
}

bool isFloat(const std::string& str) {
    std::regex floatRegex(R"(^\d+(\.\d+)?([eE][+]?\d+)?$)");
    return std::regex_match(str, floatRegex);
}

int main(int argc, char* argv[]) {
    if((argc != 3) && (!isFloat(argv[1]) || !isFloat(argv[2]))) {
        std::cout << "usage:\n";
        std::cout << "\t" << argv[0] << " loop_bound " << "loop_counter\n";
        std::cout << "\t" << "loop_bound is a positive floating-point value\n";
        std::cout << "\t" << "loop_counter is a positive floating-point value" << std::endl;
        return 1;
    }

    float boundNum = std::stof(argv[1]);
    float counter = std::stof(argv[2]);
    printIEEE(boundNum, counter);

    float threshold = Overflow();

    //  Early check
    if(!(std::log2(boundNum) > 24) && !(std::log2(counter) > 24)) {
        std::cout << "There is no overflow!" << std::endl;
    } else {
        std::cout << "\033[33m" << "Warning: Possible overflow!" << "\033[0m\n"
                  << "Overflow threshold:\n";
        printIEEE(threshold);
    }

    return 0;
}