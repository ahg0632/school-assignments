#include <WinSock2.h>
#include <WS2tcpip.h>
#include <cstdint>
#include <iostream>
#include <fstream>
#include <string>

#pragma comment(lib, "ws2_32.lib")

//Change to desired IP Address to connect to
#define IPADDR "127.0.0.1"
//Change port number to whatever other port connecting to
#define PORT 8888

int main() {
    //Windows Socket
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

    //Create TCP Socket
    SOCKET clientSocket = socket(AF_INET, SOCK_STREAM, 0);
    if(clientSocket == INVALID_SOCKET) {
        std::cerr << "Socket creation failed.\n";
        return 1;
    }

    //Setup server connection
    sockaddr_in serverAddr{};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    serverAddr.sin_addr.s_addr = inet_addr(IPADDR); //Change static IP
    if(connect(clientSocket, (sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        std::cerr << "Connection failed...\n";
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }
    std::cout << "Connected to server...\n";

    //Open image
    std::string chosenImage;
    std::cout << "Type image filename to send: ";
    std::cin >> chosenImage;
    std::ifstream imageFile("test.jpg", std::ios::binary | std::ios::ate);
    if(!imageFile) {
        std::cerr << "Failed to open image...\n";
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }
    std::streamsize fileSize = imageFile.tellg();
    imageFile.seekg(0, std::ios::beg);

    //Send Image Size
    uint64_t sizeToSend = static_cast<uint64_t>(fileSize);
    send(clientSocket, (char*)&sizeToSend, sizeof(sizeToSend), 0);

    //Send Image
    char buffer[4096];
    while(imageFile) {
        imageFile.read(buffer, sizeof(buffer));
        std::streamsize bytesRead = imageFile.gcount();
        send(clientSocket, buffer, static_cast<size_t>(bytesRead), 0);
    }
    imageFile.close();
    std::cout <<"Image Successfully Sent\n";

    //Cleanup
    closesocket(clientSocket);
    WSACleanup();
    return 0;
}