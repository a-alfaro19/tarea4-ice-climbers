cmake_minimum_required(VERSION 3.31)
project(server C)

set(CMAKE_C_STANDARD 11)

add_executable(server
        red/SocketServer.c
        red/mensajes.c
        red/clientes.c
        juego/bloque.c
        juego/bloque.h
        juego/nivel.c
        juego/nivel.h
        juego/mapa.h
        juego/mapa.c
        juego/juego.c
        juego/juego.h
        juego/jugador.c
        juego/jugador.h
        juego/acciones.c
        juego/acciones.h
        juego/Obstacle.c
        juego/Obstacle.h

)


# Include Socket lib
if(WIN32)
    target_link_libraries(server wsock32 ws2_32)
endif()
