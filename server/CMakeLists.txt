cmake_minimum_required(VERSION 3.31)
project(server C)

set(CMAKE_C_STANDARD 11)

add_executable(server
        main.c
        red/SocketServer.c
        red/mensajes.c
        red/clientes.c
        red/observer.c
        juego/juego.c
        juego/jugador.c
        juego/obstaculo.c
        juego/fruta.c
        juego/nivel.c
        juego/bloque.c
        util/log.c
)


# Include Socket lib
if(WIN32)
    target_link_libraries(server wsock32 ws2_32)
endif()
