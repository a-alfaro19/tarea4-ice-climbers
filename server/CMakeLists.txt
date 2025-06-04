cmake_minimum_required(VERSION 3.31)
project(server C)

set(CMAKE_C_STANDARD 11)

add_executable(server
        red/SocketServer.c
        red/mensajes.c
#        red/clientes.c
        juego/Tile.h
        juego/Map.h
        juego/Map.c
#        juego/juego.c
#        juego/juego.h
#        juego/jugador.c
#        juego/jugador.h
#        juego/acciones.c
#        juego/acciones.h
)


# Include Socket lib
if(WIN32)
    target_link_libraries(server wsock32 ws2_32)
endif()
