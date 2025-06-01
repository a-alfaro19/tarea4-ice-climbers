cmake_minimum_required(VERSION 3.31)
project(server C)

set(CMAKE_C_STANDARD 11)

add_executable(server main.c
        red/SocketServer.h
        red/SocketServer.c
#        juego/juego.c
#        juego/juego.h
#        juego/jugador.c
#        juego/jugador.h
#        juego/obstaculo.c
#        juego/obstaculo.h
#        juego/fruta.c
#        juego/fruta.h
#        juego/nivel.c
#        juego/nivel.h
        util/log.c
        util/log.h
#        util/constantes.h
#        util/tipos.h
#        red/mensajes.c
#        red/mensajes.h
#        juego/bloque.c
#        juego/bloque.h
#        red/clientes.c
#        red/clientes.h
#        red/observer.c
#        red/observer.h
)

# Include Socket lib
if(WIN32)
    target_link_libraries(server wsock32 ws2_32)
endif()
