#include "com_mjasinski_tictactoe_engine_Main.h"
#include <jni.h>
#include <iostream>

JNIEXPORT void JNICALL Java_com_mjasinski_tictactoe_engine_Main_sayHello
  (JNIEnv* env, jobject thisObject) {
    std::cout << "Hello from C++ !!" << std::endl;
}