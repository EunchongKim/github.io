#include <stdio.h>
#include <stdlib.h>
#include "teletext.h"

int main(int argc, char **argv) {

   SDL_Simplewin sw;
   Control ctrl;
   FILE *ftest;
   unsigned char words[LENG];
   fntrow fontdata[FNTCHARS][FNTHEIGHT];
   int i=0;
   if(argc != ARGV) {
      fprintf(stderr, "%s", "Please type the filename.\n");
   }

   ftest = fopen(argv[IN], "rb");
   while(i<LENG && (fscanf(ftest, "%c", &words[i])) != EOF) {i++;}
   Neill_SDL_Init(&sw);
   Neill_SDL_ReadFont(fontdata, "m7fixed.fnt");
   Control_Init(&ctrl);

   do {
      Control_Code(&sw, fontdata, words, &ctrl);
      Neill_SDL_UpdateScreen(&sw);
      Neill_SDL_Events(&sw);
   }while(!sw.finished);
   fclose(ftest);
   atexit(SDL_Quit);
   return 0;
}
