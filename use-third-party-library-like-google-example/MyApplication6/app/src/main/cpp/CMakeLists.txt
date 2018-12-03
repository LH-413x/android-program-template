cmake_minimum_required(VERSION 3.4.1)

set(cpp_DIR ${CMAKE_SOURCE_DIR})

add_library(libcutils SHARED IMPORTED)

set_target_properties(libcutils PROPERTIES IMPORTED_LOCATION ${cpp_DIR}/cutils_lib/arm64-v8a/libcutils.so)


add_library(native-lib SHARED native-lib.cpp)

target_include_directories(native-lib PRIVATE ${cpp_DIR}/cutils)

target_link_libraries(native-lib
                      android
                      libcutils
                      log)