#include <stdio.h>
#include <stdint.h>

typedef FILE java_io_PrintStream;

void java_io_PrintStream_println__Ljava_lang_String_V(java_io_PrintStream* this, const char* str) {
  fprintf(this, "%s\n", str);
}
void java_lang_Object__init____V(void* this) {}

java_io_PrintStream* java_lang_System_out_Ljava_io_PrintStream;

void app_cleancode_bytecode_to_asm_Test__clinit____V();

void app_cleancode_bytecode_to_asm_Test_main___arr_Ljava_lang_String_V(const char** args);

int main(int argc, const char** argv) {
  java_lang_System_out_Ljava_io_PrintStream = stdout;
  app_cleancode_bytecode_to_asm_Test__clinit____V();
    app_cleancode_bytecode_to_asm_Test_main___arr_Ljava_lang_String_V(argv);
}
