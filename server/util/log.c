#include "log.h"
#include <stdio.h>
#include <time.h>

void log_info(const char *mensaje) {
    time_t t = time(NULL);
    struct tm *tm_info = localtime(&t);
    printf("[INFO][%02d:%02d:%02d] %s\n",
           tm_info->tm_hour, tm_info->tm_min, tm_info->tm_sec, mensaje);
}

void log_error(const char *mensaje) {
    time_t t = time(NULL);
    struct tm *tm_info = localtime(&t);
    fprintf(stderr, "[ERROR][%02d:%02d:%02d] %s\n",
            tm_info->tm_hour, tm_info->tm_min, tm_info->tm_sec, mensaje);
}
