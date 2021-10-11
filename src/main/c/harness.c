#include <stdio.h>

typedef FILE java_io_PrintStream;

void java_io_PrintStream_println(java_io_PrintStream* this, const char* str) {
  fprintf(this, "%s\n", str);
}
void java_lang_Object__init_(void* this) {}

java_io_PrintStream* java_lang_System_out;

void app_cleancode_bytecode_to_asm_Test_main(const char** args);

int main(int argc, const char** argv) {
  java_lang_System_out = stdout;
  app_cleancode_bytecode_to_asm_Test_main(argv);
}
