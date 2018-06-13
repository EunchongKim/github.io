#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "teletext.h"

int main(void) {

   SDL_Simplewin sw;
   FILE *test_text, *test_panda, *test_lfc;
   unsigned char text[LENG], panda[LENG], lfc[LENG];
   fntrow fontdata[FNTCHARS][FNTHEIGHT];

   test_text = fopen("test.m7", "rb");
   test_panda = fopen("panda.m7", "rb");
   test_lfc = fopen("lfc.m7", "rb");
   Hidden_SDL_Init(&sw);
   Neill_SDL_ReadFont(fontdata, "m7fixed.fnt");

   read_file(test_text, text);
   read_file(test_panda, panda);
   read_file(test_lfc, lfc);

   printf("Teletext Tests ... Start\n");

   /* Check the read_file function */
   assert(test_text!=NULL);
   assert(test_panda!=NULL);
   assert(test_lfc!=NULL);
   First_Test(text);
   First_Test(panda);
   First_Test(lfc);

   printf("Teletext Second Tests ... Continue\n");
   Second_Test(&sw, fontdata, text, panda, lfc);

   printf("Teletext Tests ... Stop\n");
   fclose(test_text);
   fclose(test_panda);
   fclose(test_lfc);
   atexit(SDL_Quit);
   return 0;
}

void First_Test(unsigned char words[LENG]) {

   int errors=NONE, times=NONE;
   Control ctrl;

   Control_Init(&ctrl);
   assert(ctrl.color==white);

   /* Pass the indexes of errors and times to each function,
      check all the functions work correctly */
   Test_Control_Init(&errors, &times, &ctrl);
   Test_Control_Code(&errors, &times, words, &ctrl);

   if(errors!=NONE) {
      fprintf(stderr, "Errors are found\n");
      exit(EXIT_ERROR);
   }
   else {
      printf("Test passed %d functions with %d times\n", FUNCTIONS, times);
   }
}

void Test_Control_Init(int *errors, int *times, Control *ctrl) {
   ctrl->color = white;
   ctrl->height = hsingle;
   ctrl->graphic = basic;
   ctrl->backcol = black;
   ctrl->hold = basic;

   /*------Test part------*/
   if(ctrl->color!=white || ctrl->height!=hsingle ||
      ctrl->graphic!=basic || ctrl->backcol!=black ||
      ctrl->hold!=basic) {
      (*errors)++;
      printf("Errors detected in 'Test_Control_Init'\n");
   }
   (*times)++;
}

void Test_Color_Init(int *errors, int *times, Color *rgb) {
   rgb->r=255; rgb->g=255; rgb->b=255; rgb->br=0; rgb->bg=0; rgb->bb=0;

   /*------Test part------*/
   if(rgb->r!=255 || rgb->g!=255 || rgb->b!=255 ||
      rgb->br!=0 || rgb->bg!=0 || rgb->bb!=0) {
      (*errors)++;
      printf("Errors detected in 'Test_Color_Init'\n");
   }

   (*times)++;
}

void Test_Control_Code(int *errors, int *times, unsigned char words[LENG], Control *ctrl) {

   int pos=DEFAULT, ox=DEFAULT, oy=DEFAULT;

   do {
      Test_Read_Input(errors, times, words, pos, ctrl);
      Test_Each_Control(errors, times, words, ctrl, pos, ox, oy);
      pos++;
      ox = pos%LINE*FNTWIDTH;
      /*------Test part------*/
      if(ox!=(int)((pos%LINE)*FNTWIDTH)) {
         (*errors)++;
         printf("Errors detected in 'Test_Color_Code'\n");
      }
      oy = pos/LINE*FNTHEIGHT;
      /*------Test part------*/
      if(oy!=(int)((pos/LINE)*FNTHEIGHT)) {
         (*errors)++;
         printf("Errors detected in 'Test_Color_Code'\n");
      }
   }while(pos<LENG);
   (*times)++;
}

void Test_Read_Input(int *errors, int *times, unsigned char words[LENG], int pos, Control *ctrl) {

   /* I will check this decoding part with provided example files
      in the second test,
      in this time, check if new line has base case */
   if(words[pos]==0x81 || words[pos]==0x91) {ctrl->color=red;}
   else if(words[pos]==0x82 || words[pos]==0x92) {ctrl->color=green;}
   else if(words[pos]==0x83 || words[pos]==0x93) {ctrl->color=yellow;}
   else if(words[pos]==0x84 || words[pos]==0x94) {ctrl->color=blue;}
   else if(words[pos]==0x85 || words[pos]==0x95) {ctrl->color=magenta;}
   else if(words[pos]==0x86 || words[pos]==0x96) {ctrl->color=cyan;}
   else if(words[pos]==0x87 || words[pos]==0x97) {ctrl->color=white;}

   if(words[pos]==0x9d) {
      if(words[pos-PREV]==0x81) {ctrl->backcol=red;}
      if(words[pos-PREV]==0x82) {ctrl->backcol=green;}
      if(words[pos-PREV]==0x83) {ctrl->backcol=yellow;}
      if(words[pos-PREV]==0x84) {ctrl->backcol=blue;}
      if(words[pos-PREV]==0x85) {ctrl->backcol=magenta;}
      if(words[pos-PREV]==0x86) {ctrl->backcol=cyan;}
      if(words[pos-PREV]==0x87 || words[pos-PREV]==0xa0) {
         ctrl->backcol=white;
      }
   }
   if(words[pos]==0x9c) {ctrl->backcol=black;}

   if(words[pos]==0x8d) {
      if(words[pos-LINE]==0x8d) {ctrl->height=hdouble_down;}
      else {ctrl->height = hdouble_up;}
   }
   else if(words[pos]==0x8c) {ctrl->height=hsingle;}

   if(ctrl->height!=hsingle && words[pos-LINE]==0x8d) {
      ctrl->height=hdouble_down; 
   }

   if(words[pos]>=0x90 && words[pos]<=0x97) {
      if(ctrl->hold==basic) {ctrl->graphic=cont;}
   }
   else if(words[pos]==0x99) {ctrl->graphic=cont; ctrl->hold=basic;}
   else if(words[pos]==0x9a) {ctrl->graphic=seper; ctrl->hold=basic;}
   else if(words[pos]==0x9e) {ctrl->hold=held;}
   else if(words[pos]<0x90) {ctrl->graphic=basic; ctrl->hold=basic;}
   else if(words[pos]==0x9f) {ctrl->hold=basic;}
   if(NEWLINE==basic) {ctrl->color=white; ctrl->height=hsingle; 
                       ctrl->graphic=basic; ctrl->backcol=black;
                       ctrl->hold=basic;}

   /*------Test part------*/
   if(NEWLINE==basic) {
      if (ctrl->color!=white || ctrl->height!=hsingle ||
          ctrl->graphic!=basic || ctrl->backcol!=black ||
          ctrl->hold!=basic) {
         (*errors)++;
         printf("Errors detected in 'Test_Read_Input'\n");
      }
   }
   (*times)++;
}

unsigned char Test_Each_Control(int *errors, int *times, unsigned char words[LENG], Control *ctrl, int pos, int ox, int oy) {

   unsigned char chr = words[pos];

   if(words[pos] >= 0x80 && words[pos] <= 0x9f) {chr = 0xa0;}
   if(ctrl->height==hdouble_down) {oy=oy-FNTHEIGHT;}
   if(ctrl->graphic != basic) {
      if(words[pos]>=0xc0 && words[pos]<=0xdf) {
         chr = chr-CONVERT;
         Test_Sub_Tele_DrawChar(errors, times, chr, ox, oy, ctrl);
      }
      else {Test_Sub_Tele_Graphic(errors, times, chr, ox, oy, ctrl);}
   }
   else {
         if(chr >= 0xa0) {chr = chr-CONVERT;}
         Test_Sub_Tele_DrawChar(errors, times, chr, ox, oy, ctrl);
   }

   /*------Test part------*/
   if(ctrl->height!=basic) {
      if(words[pos]>=0xc0 && words[pos]<=0xdf) {
         if(chr>CONVERT) {
            (*errors)++;
            printf("fffffErrors detected in 'Test_Each_Control'\n");
          }
      }
   }
   else {
      if(chr>CONVERT) {
         (*errors)++;
         printf("Errors detected in 'Test_Each_Control'\n");
      }
   }
   (*times)++;
   return chr;
}

void Test_make_rgb(int *times, Control *ctrl, Color *rgb) {

   /* I will check this color part with provided example files
      in the second test */
   if(ctrl->color==red) {rgb->r=255; rgb->g=0; rgb->b=0;}
   else if(ctrl->color==green) {rgb->r=0; rgb->g=255; rgb->b=0;}
   else if(ctrl->color==yellow) {rgb->r=255; rgb->g=255; rgb->b=0;}
   else if(ctrl->color==blue) {rgb->r=0; rgb->g=0; rgb->b=255;}
   else if(ctrl->color==magenta) {rgb->r=255; rgb->g=0; rgb->b=255;}
   else if(ctrl->color==cyan) {rgb->r=0; rgb->g=255; rgb->b=255;}
   else if(ctrl->color==white) {rgb->r=255; rgb->g=255; rgb->b=255;}

   if(ctrl->backcol==red) {rgb->br=255; rgb->bg=0; rgb->bb=0;}
   else if(ctrl->backcol==green) {rgb->br=0; rgb->bg=255; rgb->bb=0;}
   else if(ctrl->backcol==yellow) {rgb->br=255; rgb->bg=255; rgb->bb=0;}
   else if(ctrl->backcol==blue) {rgb->br=0; rgb->bg=0; rgb->bb=255;}
   else if(ctrl->backcol==magenta) {rgb->br=255; rgb->bg=0; rgb->bb=255;}
   else if(ctrl->backcol==cyan) {rgb->br=0; rgb->bg=255; rgb->bb=255;}
   else if(ctrl->backcol==white) {rgb->br=255; rgb->bg=255; rgb->bb=255;}
   else if(ctrl->backcol==black) {rgb->br=0; rgb->bg=0; rgb->bb=0;}

   (*times)++;
}

void Test_Sub_Tele_Graphic(int *errors, int *times, unsigned char chr, int ox, int oy, Control *ctrl) {

   Color rgb;
   unsigned gap=NONE;
   unsigned char sixel[COL][ROW];

   Color_Init(&rgb);
   make_rgb(ctrl, &rgb);
   if(ctrl->graphic==cont) {gap=NONE;}
   else if(ctrl->graphic==seper) {gap=twice;}
   Test_Tele_Graphic(errors, times, chr, ox, oy, &rgb, gap, ctrl, sixel);

   /*------Test Part------*/
   if(ctrl->graphic==cont) {
      if(gap!=NONE) {
         (*errors)++;
         printf("Errors detected in 'Test_Sub_Tele_Graphic'\n");
      }
   }
   else if(ctrl->graphic!=seper) {
      if(gap!=twice) {
         (*errors)++;
         printf("Errors detected in 'Test_Sub_Tele_Graphic'\n");
      }
   }
   (*times)++;
}

void Test_Tele_Graphic(int *errors, int *times, unsigned char chr, int ox, int oy, Color *rgb, unsigned gap, Control *ctrl, unsigned char sixel[COL][ROW]) {

   int i, j;

   /* In this fuction, arguments will be pass through to others,
      checking the hold graphic mode will be done in the second part.
      Pass the test here */
   if(ctrl->hold != held) {
      Test_Base_sixel(errors, times, sixel);
      if(chr-BASE_CODE > BASE_CODE) {
         Test_Calcul_Graphic(errors, times, (chr-BASE_CODE), sixel);
      }
   }

   for(j=0;j<ROW;j++) {
      for(i=0;i<COL;i++) {
         Test_DrawGraphic(times, ox, oy, rgb, gap, sixel, i, j);
      }
   }
   (*times)++;
}

void Test_Base_sixel(int *errors, int *times, unsigned char sixel[COL][ROW]) {

   int i, j;
   for(i=0;i<COL;i++) {
      for(j=0;j<ROW;j++) {
         sixel[i][j]=NONE;
      }
   }

   /*------Test Part------*/
   for(i=0;i<COL;i++) {
      for(j=0;j<ROW;j++) {
         if(sixel[i][j]!=NONE) {
            (*errors)++;
            printf("Errors detected in 'Test_Base_sixel'\n");
         }
      }
   }
   (*times)++;
}

void Test_DrawGraphic(int *times, int ox, int oy, Color *rgb, unsigned gap, unsigned char sixel[COL][ROW], int i, int j) {

   /* Check the actual output of drawing graphic is hard to prove,
      take this part to check the teletext screens using this code.
      Not passing through errors here */
   SDL_Simplewin sw;
   unsigned x, y;
   for(y = 0; y < (RECTH-gap); y++){
      for(x = 0; x < (RECTW-gap); x++){
         if(sixel[i][j] == occupied){
            Neill_SDL_SetDrawColour(&sw, rgb->r, rgb->g, rgb->b);
            SDL_RenderDrawPoint(sw.renderer, x+(ox+i*RECTW), y+(oy+j*RECTH));
          }
          else{
             Neill_SDL_SetDrawColour(&sw, rgb->br, rgb->bg, rgb->bb);
             SDL_RenderDrawPoint(sw.renderer, x+(ox+i*RECTW), y+(oy+j*RECTH));
          }
      }
   }
   (*times)++;
}

void Test_Calcul_Graphic(int *errors, int *times, unsigned char chr, unsigned char sixel[COL][ROW]) {

   /* Check this function's output in the second part,
      simply check the size of number here */
   if(chr >= TR) {sixel[R][T] = occupied; chr = chr - TR;}
   if(chr > TR) {
      (*errors)++;
      printf("Errors detected in 'Test_Calcul_Graphic'\n");
   }
   if(chr >= TL) {sixel[L][T] = occupied; chr = chr - TL;}
   if(chr > TL) {
      (*errors)++;
      printf("Errors detected in 'Test_Calcul_Graphic'\n");
   }
   if(chr >= SR) {sixel[R][S] = occupied; chr = chr - SR;}
   if(chr > SR) {
      (*errors)++;
      printf("Errors detected in 'Test_Calcul_Graphic'\n");
   }
   if(chr >= SL) {sixel[L][S] = occupied; chr = chr - SL;}
   if(chr > SL) {
      (*errors)++;
      printf("Errors detected in 'Test_Calcul_Graphic'\n");
   }
   if(chr >= FR) {sixel[R][F] = occupied; chr = chr - FR;}
   if(chr > FR) {
      (*errors)++;
      printf("Errors detected in 'Test_Calcul_Graphic'\n");
   }
   if(chr >= FL) {sixel[L][F] = occupied;}
   (*times)++;
}

void Test_Sub_Tele_DrawChar(int *errors, int *times, unsigned char chr, int ox, int oy, Control *ctrl) {

   Color rgb;
   unsigned expand_leng=padding, h=NONE;

   Test_Color_Init(errors, times, &rgb);
   Test_make_rgb(times, ctrl, &rgb);
   if(ctrl->height == hdouble_up) {expand_leng=twice; h=NONE;}
   else if(ctrl->height == hdouble_down) {expand_leng=twice; h=FNTHEIGHT;}
   Test_Tele_DrawChar(times, chr, ox, oy, &rgb, expand_leng, h);

   /*------Test Part------*/
   if(ctrl->height==hdouble_up) {
      if(expand_leng!=twice || h!=NONE) {
         (*errors)++;
         printf("Errors detected in 'Test_Sub_Tele_DrawChar'\n");
      }
   }
   if(ctrl->height==hdouble_down) {
      if(expand_leng!=twice || h!=FNTHEIGHT) {
         (*errors)++;
         printf("Errors detected in 'Test_Sub_Tele_DrawChar'\n");
      }
   }
   (*times)++;
}

void Test_Tele_DrawChar(int *times, unsigned char chr, int ox, int oy, Color *rgb, unsigned expand_leng, unsigned h) {

   /* Check the actual output of drawing char is hard to prove,
      take this part to check the teletext screens using this code.
      Not passing through errors here */
   SDL_Simplewin sw;
   fntrow fontdata[FNTCHARS][FNTHEIGHT];
   unsigned x, y;

   for(y = (0+h); y < (FNTHEIGHT+h); y++){
      for(x = 0; x < FNTWIDTH; x++){
         if(fontdata[chr-FNT1STCHAR][y/expand_leng] >> (FNTWIDTH - 1 - x) & 1){
            Neill_SDL_SetDrawColour(&sw, rgb->r, rgb->g, rgb->b);
            SDL_RenderDrawPoint(sw.renderer, x + ox, y+oy);
         }
         else{
            Neill_SDL_SetDrawColour(&sw, rgb->br, rgb->bg, rgb->bb);
            SDL_RenderDrawPoint(sw.renderer, x + ox, y+oy);
         }
      }
   }
   (*times)++;
}

/*---------------------Second Test---------------------*/

void Second_Test(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char text[LENG], unsigned char panda[LENG], unsigned char lfc[LENG]) {

   Common_Test(sw, fontdata, text);
   Common_Test(sw, fontdata, panda);
   Common_Test(sw, fontdata, lfc);
   Color_Test(text);
   Color_Test(panda);
   Color_Test(lfc);
   Back_Color_Test(text);
   Back_Color_Test(panda);
   Back_Color_Test(lfc);
   Double_H_Test(text); 
   Double_H_Test(panda);
   Double_H_Test(lfc); 
   Text_Test(sw, fontdata, text);
   Text_Test(sw, fontdata, panda);
   Text_Test(sw, fontdata, lfc); 

   Graphic_Mode_Test(text);
   Graphic_Mode_Test(panda);
   Graphic_Mode_Test(lfc);
   Sixel_Test(sw, text);
   Sixel_Test(sw, panda);
   Sixel_Test(sw, lfc);
}

void read_file(FILE *file, unsigned char words[LENG]) {
   int i=0;
   while((fscanf(file, "%c", &words[i])) != EOF) {i++;}
}

void Common_Test(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG]) {

   int pos, ox=DEFAULT, oy=DEFAULT;
   unsigned char chr;
   Control ctrl;
   Control_Init(&ctrl);
   assert(ctrl.color==white);

   for(pos=0; pos<LENG; pos++) {
      /* Check if all the inputs are filled or not */
      assert(words[pos] != '\0');
      Read_Input(words, pos, &ctrl);
      chr = Each_Control(sw, fontdata, words, &ctrl, pos, ox, oy);
      /* Check if the control codes are converted to '0xa0' before drawing */
      if(words[pos] >= 0x80 && words[pos] <= 0x9f) {
         if(ctrl.graphic==basic) {assert(chr==(0xa0-CONVERT));}
         else {assert(chr==0xa0);}
      }
      /* Check that each newline begins with the base case */
      if(NEWLINE==basic) {
         assert(ctrl.color == white);
         assert(ctrl.height == hsingle);
         assert(ctrl.graphic == basic);
         assert(ctrl.backcol == black);
         assert(ctrl.hold == basic);
      }
   }
}

void Color_Test(unsigned char words[LENG]) {

   int pos=DEFAULT, mode=DEFAULT;
   Control ctrl;

   Color rgb;
   Control_Init(&ctrl);
   assert(ctrl.height==hsingle);
   Color_Init(&rgb);
   assert(rgb.r==255);

   do {
      Read_Input(words, pos, &ctrl);
      make_rgb(&ctrl, &rgb);
      /* Check the alphanumeric and graphic colors */
         if(words[pos]==0x81 || words[pos]==0x91) {mode = red;}
         else if(words[pos]==0x82 || words[pos]==0x92) {mode = green;}
         else if(words[pos]==0x83 || words[pos]==0x93) {mode = yellow;}
         else if(words[pos]==0x84 || words[pos]==0x94) {mode = blue;}
         else if(words[pos]==0x85 || words[pos]==0x95) {mode = magenta;}
         else if(words[pos]==0x86 || words[pos]==0x96) {mode = cyan;}
         else if(words[pos]==0x87 || words[pos]==0x97 
                 || NEWLINE==basic) {mode = white;}
         /* Check it is maintained before other colors come 
            or the line is ended */
         if(mode==red) {
            assert(ctrl.color==red);
            assert(rgb.r==255);
            assert(rgb.g==0);
            assert(rgb.b==0);
         }
         if(mode==green) {
            assert(ctrl.color==green);
            assert(rgb.r==0);
            assert(rgb.g==255);
            assert(rgb.b==0);
         }
         if(mode==yellow) {
            assert(ctrl.color==yellow);
            assert(rgb.r==255);
            assert(rgb.g==255);
            assert(rgb.b==0);
         }
         if(mode==blue) {
            assert(ctrl.color==blue);
            assert(rgb.r==0);
            assert(rgb.g==0);
            assert(rgb.b==255);
         }
         if(mode==magenta) {
            assert(ctrl.color==magenta);
            assert(rgb.r==255);
            assert(rgb.g==0);
            assert(rgb.b==255);
         }
         if(mode==cyan) {
            assert(ctrl.color==cyan);
            assert(rgb.r==0);
            assert(rgb.g==255);
            assert(rgb.b==255);
         }
         if(mode==white) {
            assert(ctrl.color==white);
            assert(rgb.r==255);
            assert(rgb.g==255);
            assert(rgb.b==255);
         }
      pos++;
   }while(pos<LENG);
}

void Back_Color_Test(unsigned char words[LENG]) {

   int pos=DEFAULT, mode=NONE;
   Control ctrl;
   Color rgb;
   Control_Init(&ctrl);
   assert(ctrl.graphic==basic);
   Color_Init(&rgb);
   assert(rgb.g==255);

   do {
      Read_Input(words, pos, &ctrl);
      make_rgb(&ctrl, &rgb);
      /* Check the alphanumeric and graphic colors */
      if(words[pos]==0x9d) {
         if(words[pos-PREV]==0x81) {mode = red;}
         if(words[pos-PREV]==0x82) {mode = green;}
         if(words[pos-PREV]==0x83) {mode = yellow;}
         if(words[pos-PREV]==0x84) {mode = blue;}
         if(words[pos-PREV]==0x85) {mode = magenta;}
         if(words[pos-PREV]==0x86) {mode = cyan;}
         if(words[pos-PREV]==0x87 || words[pos-PREV]==0xa0) {mode = white;}
      }
      if(words[pos]==0x9c || NEWLINE==basic) {mode = black;}
         /* Check it is maintained before other colors come 
            or the line is ended */
         if(mode==red) {
            assert(ctrl.backcol==red);
            assert(rgb.br==255);
            assert(rgb.bg==0);
            assert(rgb.bb==0);
         }
         if(mode==green) {
            assert(ctrl.backcol==green);
            assert(rgb.br==0);
            assert(rgb.bg==255);
            assert(rgb.bb==0);
         }
         if(mode==yellow) {
            assert(ctrl.backcol==yellow);
            assert(rgb.br==255);
            assert(rgb.bg==255);
            assert(rgb.bb==0);
         }
         if(mode==blue) {
            assert(ctrl.backcol==blue);
            assert(rgb.br==0);
            assert(rgb.bg==0);
            assert(rgb.bb==255);
         }
         if(mode==magenta) {
            assert(ctrl.backcol==magenta);
            assert(rgb.br==255);
            assert(rgb.bg==0);
            assert(rgb.bb==255);
         }
         if(mode==cyan) {
            assert(ctrl.backcol==cyan);
            assert(rgb.br==0);
            assert(rgb.bg==255);
            assert(rgb.bb==255);
         }
         if(mode==white) {
            assert(ctrl.backcol==white);
            assert(rgb.br==255);
            assert(rgb.bg==255);
            assert(rgb.bb==255);
         }
         if(mode==black) {
            assert(ctrl.backcol==black);
            assert(rgb.br==0);
            assert(rgb.bg==0);
            assert(rgb.bb==0);
         }
      pos++;
   }while(pos<LENG);
}

void Double_H_Test(unsigned char words[LENG]) {

   int pos=DEFAULT, mode=NONE;
   Control ctrl;
   Control_Init(&ctrl);
   assert(ctrl.backcol==black);

   do {
      Read_Input(words, pos, &ctrl);
      if(words[pos]==0x8d) {
         /* If double height was used in the previous line
            make the bottom line */
         if(words[pos-LINE]==0x8d) {mode=hdouble_down;}
         /* If not, make the upper line */ 
         else {mode=hdouble_up;}
      }
      else if(words[pos]==0x8c || NEWLINE==basic) {mode=hsingle;}
      /* With in double height mode, if the upper line is also double height,
         make double height mode print the bottom line */
      else if(ctrl.height!=hsingle && words[pos-LINE]==0x8d) {
         mode=hdouble_down;
      }
      /* Check if double height mode is maintained */
      if(mode==hdouble_down) {
         assert(ctrl.height==hdouble_down);
      }
      else if(mode==hdouble_up) {
         assert(ctrl.height==hdouble_up);
      }
      else if(mode==hsingle) {
         assert(ctrl.height==hsingle);
      }
      pos++;
   }while(pos<LENG);
}

void Text_Test(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG]) {

   int pos, ox=DEFAULT, oy=DEFAULT;
   unsigned char chr;
   Control ctrl;
   Control_Init(&ctrl);
   assert(ctrl.hold==basic);

   for(pos=0; pos<LENG; pos++) {
      Read_Input(words, pos, &ctrl);
      /* This 'chr' after passing 'Each_Control' function will go through
         DrawChar function */
      chr = Each_Control(sw, fontdata, words, &ctrl, pos, ox, oy);
      /* When inputs are characters in the text mode  */
      if(ctrl.graphic==basic && words[pos]>0xa0 && words[pos]<0xff) {
         /* Check that inputs are correspond to the Ascii codes */
         assert(chr==(words[pos]-CONVERT));
      }
      /* Check blast-through text */
      else if(ctrl.graphic!=basic && words[pos]>=0xc0 
              && words[pos]<=0xdf) {
         assert(chr==(words[pos]-CONVERT));
      }
   }
}

void Graphic_Mode_Test(unsigned char words[LENG]) {

   int pos=DEFAULT, mode=NONE, hold=NONE;
   Control ctrl;
   Control_Init(&ctrl);
   assert(ctrl.color==white);

   do {
      Read_Input(words, pos, &ctrl);

      if(words[pos]>=0x90 && words[pos]<=0x97) {
         if(ctrl.hold==basic) {mode=cont;}
      }
      else if(words[pos]==0x99) {mode=cont; hold=basic;}
      else if(words[pos]==0x9a) {mode=seper; hold=basic;}
      else if(words[pos]==0x9e) {hold=held;}
      else if(words[pos]==0x9f) {hold=basic;}
      else if(words[pos]<0x90 || NEWLINE==basic) {
         mode=basic; hold=basic;
      }

      /* Check if each code of test files
         has right instruction */
      if(mode==basic) {
         assert(ctrl.graphic==basic);
      }
      else if(mode==cont) {
         assert(ctrl.graphic==cont);
      }
      else if(mode==seper) {
         assert(ctrl.graphic==seper);
      }
      if(hold==basic) {
         assert(ctrl.hold==basic);
      }
      else if(hold==held) {
         assert(ctrl.hold==held);
      }
      pos++;
   }while(pos<LENG);
}

void Sixel_Test(SDL_Simplewin *sw, unsigned char words[LENG]) {

   int pos=DEFAULT, ox=DEFAULT, oy=DEFAULT, gap=NONE;
   unsigned char sixel[COL][ROW], new_s[COL][ROW];
   Control ctrl;
   Color rgb;
   Control_Init(&ctrl);
   assert(ctrl.height==hsingle);
   Color_Init(&rgb);
   assert(rgb.br==0);

   do {
      Read_Input(words, pos, &ctrl);
      /* Randomly choose one graphic, and check the sixel output */
      Tele_Graphic(sw, words[pos], ox, oy, &rgb, gap, &ctrl, sixel);
      if(words[pos]==0xb5) {
         assert(sixel[L][F]==occupied);
         assert(sixel[R][F]==NONE);
         assert(sixel[L][S]==occupied);
         assert(sixel[R][S]==NONE);
         assert(sixel[L][T]==occupied);
         assert(sixel[R][T]==NONE);
      }
      if(words[pos]==0xea) {
         assert(sixel[L][F]==NONE);
         assert(sixel[R][F]==occupied);
         assert(sixel[L][S]==NONE);
         assert(sixel[R][S]==occupied);
         assert(sixel[L][T]==NONE);
         assert(sixel[R][T]==occupied);
      }
      /* If graphic mode is holding */
      if(words[pos]==0x9e) {
         /* Make a sixel of the previous one */
         Tele_Graphic(sw, words[pos-PREV], ox, oy, &rgb, gap, &ctrl, sixel);
         make_ident(sixel, new_s);
         /* Pass it through a new sixel for the next input */
         Tele_Graphic(sw, words[pos+NEXT], ox, oy, &rgb, gap, &ctrl, new_s);
         /* Even though two characters are different */
         assert(words[pos-PREV]!=words[pos+NEXT]);
         /* Make sure that a new sixel will not be changed */
         assert(sixel[L][F]==new_s[L][F]);
         assert(sixel[R][F]==new_s[R][F]);
         assert(sixel[L][S]==new_s[L][S]);
         assert(sixel[R][S]==new_s[R][S]);
         assert(sixel[L][T]==new_s[L][T]);
         assert(sixel[R][T]==new_s[R][T]);
      }
      pos++;
   }while(pos<LENG);
}

void make_ident(unsigned char sixel[COL][ROW], unsigned char new_s[COL][ROW]) {

   int i,j;
   for(i=0;i<COL;i++) {
      for(j=0;j<ROW;j++) {
         new_s[i][j] = sixel[i][j];
      }
   }
}

