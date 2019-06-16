#include <iostream>
#include <math.h>
#include <random>
#include "jni.h"
#include <string>
#include "com_mjasinski_tictactoe_engine_Main.h"

JNIEXPORT void JNICALL Java_com_mjasinski_tictactoe_engine_Main_sayHello(JNIEnv* env, jobject thisObject){
    std::cout << "Hello, world!" << std::endl;
}