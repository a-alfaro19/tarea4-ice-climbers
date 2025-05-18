cmake_minimum_required(VERSION 3.31)
project(server C)

set(CMAKE_C_STANDARD 11)

add_executable(server main.c
        SocketServer.h
        SocketServer.c)

# Include Socket lib
if(WIN32)
    target_link_libraries(server wsock32 ws2_32)
endif()
