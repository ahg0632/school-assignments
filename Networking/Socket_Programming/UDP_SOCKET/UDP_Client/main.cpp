#include <WinSock2.h>
#include <WS2tcpip.h>
#include <iostream>
#include <fstream>

#pragma comment(lib, "ws2_32.lib")

//Change "INADDR_ANY" to a hardcoded address if desired
#define IPADDR "127.0.0.1"
//Change number to any desired port number
#define PORT 8888

int main() {
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

    //Create UDP Socket
    SOCKET clientSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if(clientSocket == INVALID_SOCKET) {
        std::cerr << "Socket creation failed.\n";
        return 1;
    }

    //Grab Server
    sockaddr_in serverAddr = {};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    inet_pton(AF_INET, IPADDR, &serverAddr.sin_addr);


    //Choose Image
    std::string chosenImage;
    std::cout << "Type image filename to send: ";
    std::cin >> chosenImage;
    std::ifstream imageFile(chosenImage, std::ios::binary);
    if(!imageFile) {
        std::cerr << "Failed to open image\n";
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }

    //Send Image
    char buffer[2048];
    std::cout << "Sending image....\n";
    while(!imageFile.eof()) {
        imageFile.read(buffer, sizeof(buffer));
        int bytesToRead = static_cast<int>(imageFile.gcount());
        sendto(clientSocket, buffer, bytesToRead, 0, (sockaddr*)&serverAddr, sizeof(serverAddr));
    }
    sendto(clientSocket, "EOF", 3, 0, (sockaddr*)&serverAddr, sizeof(serverAddr));
    std::cout << "Image Sent." << std::endl;

    //Cleanup
    imageFile.close();
    closesocket(clientSocket);
    WSACleanup();
    return 0;
}