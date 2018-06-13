#include "teletext.h"

/*-------------------------NEW FUNCTIONS--------------------------*/
/*----------------------------------------------------------------*/

void Control_Init(Control *ctrl) {
   ctrl->color = white;
   ctrl->height = hsingle;
   ctrl->graphic = basic;
   ctrl->backcol = black;
   ctrl->hold = basic; 
}

void Color_Init(Color *rgb) {
   rgb->r=255; rgb->g=255; rgb->b=255; rgb->br=0; rgb->bg=0; rgb->bb=0;
}

/* The first step to */
void Control_Code(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG], Control *ctrl) {

   int pos=0, ox=0, oy=0;

   do {
      Read_Input(words, pos, ctrl);
      Each_Control(sw, fontdata, words, ctrl, pos, ox, oy);
      pos++;
      ox = pos%LINE*FNTWIDTH;
      oy = pos/LINE*FNTHEIGHT;
   }while(pos<LENG);
}

void Read_Input(unsigned char words[LENG], int pos, Control *ctrl) {

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

   /* When the double height code comes, */
   if(words[pos]==0x8d) {
      /* If the same position of previous line is also double height,
         set it as the bottom line of double height */
      if(words[pos-LINE]==0x8d) {ctrl->height=hdouble_down;}
      else {ctrl->height = hdouble_up;}
   }
   else if(words[pos]==0x8c) {ctrl->height=hsingle;}

   if(ctrl->height!=hsingle && words[pos-LINE]==0x8d) {
      ctrl->height=hdouble_down; 
   }

   /* If not holding, default is contiguous mode */
   if(words[pos]>=0x90 && words[pos]<=0x97) {
      if(ctrl->hold==basic) {ctrl->graphic=cont;}
   }
   else if(words[pos]==0x99) {ctrl->graphic=cont; ctrl->hold=basic;}
   else if(words[pos]==0x9a) {ctrl->graphic=seper; ctrl->hold=basic;}
   else if(words[pos]==0x9e) {ctrl->hold=held;}
   /* If control codes of text mode come, release graphic mode */
   else if(words[pos]<0x90) {ctrl->graphic=basic; ctrl->hold=basic;}
   else if(words[pos]==0x9f) {ctrl->hold=basic;}
   if(NEWLINE==basic) {ctrl->color=white; ctrl->height=hsingle; 
                       ctrl->graphic=basic; ctrl->backcol=black;
                       ctrl->hold=basic;}
}

unsigned char Each_Control(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG], Control *ctrl, int pos, int ox, int oy) {

   unsigned char chr = words[pos];

   /* If it is control code, convert it to space */
   if(words[pos] >= 0x80 && words[pos] <= 0x9f) {chr=0xa0;}
   /* To realise the bottom line of double height,
      move oy as the distance of FNTHEIGHT */
   if(ctrl->height==hdouble_down) {oy=oy-FNTHEIGHT;}
   /* In a graphic mode */
   if(ctrl->graphic != basic) {
      /* Set a blast-through */
      if(words[pos]>=0xc0 && words[pos]<=0xdf) {
         chr = chr-CONVERT;
         Sub_Tele_DrawChar(sw, fontdata, chr, ox, oy, ctrl);
      }
      /* Others go through graphic drawing function */
      else {Sub_Tele_Graphic(sw, chr, ox, oy, ctrl);}
   }
   /* In a text mode */
   else {
         /* Convert charaters to ASCII codes */
         if(chr >= 0xa0) {chr = chr-CONVERT;}
         Sub_Tele_DrawChar(sw, fontdata, chr, ox, oy, ctrl);
   }
   return chr;
}

void make_rgb(Control *ctrl, Color *rgb) {
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
}

void Sub_Tele_Graphic(SDL_Simplewin *sw, unsigned char chr, int ox, int oy, Control *ctrl) {

   Color rgb;
   unsigned gap=NONE;
   unsigned char sixel[COL][ROW];

   Color_Init(&rgb);
   make_rgb(ctrl, &rgb);
   if(ctrl->graphic==cont) {gap = NONE;}
   else if(ctrl->graphic==seper) {gap = twice;}
   Tele_Graphic(sw, chr, ox, oy, &rgb, gap, ctrl, sixel);
}

void Tele_Graphic(SDL_Simplewin *sw, unsigned char chr, int ox, int oy, Color *rgb, unsigned gap, Control *ctrl, unsigned char sixel[COL][ROW]) {

   int i, j;

   /* If it is not hold graphic, generate new sixel
      If holding, skip this and use the previous sixel */
   if(ctrl->hold != held) {
      Base_sixel(sixel);
      if(chr > BASE_CODE) {
         Calcul_Graphic((chr-BASE_CODE), sixel);
      }
   }

   for(j=0;j<ROW;j++) {
      for(i=0;i<COL;i++) {
         DrawGraphic(sw, ox, oy, rgb, gap, sixel, i, j);
      }
   }
}

/* Calculate graphics and convert them to sixels */
void Calcul_Graphic(unsigned char chr, unsigned char sixel[COL][ROW]) {
   if(chr >= TR) {sixel[R][T] = occupied; chr = chr - TR;}
   if(chr >= TL) {sixel[L][T] = occupied; chr = chr - TL;}
   if(chr >= SR) {sixel[R][S] = occupied; chr = chr - SR;}
   if(chr >= SL) {sixel[L][S] = occupied; chr = chr - SL;}
   if(chr >= FR) {sixel[R][F] = occupied; chr = chr - FR;}
   if(chr >= FL) {sixel[L][F] = occupied;}
}

void Base_sixel(unsigned char sixel[COL][ROW]) {

   int i, j;
   for(i=0;i<COL;i++) {
      for(j=0;j<ROW;j++) {
         sixel[i][j]=NONE;
      }
   }
}

void DrawGraphic(SDL_Simplewin *sw, int ox, int oy, Color *rgb, unsigned gap, unsigned char sixel[COL][ROW], int i, int j) {

   unsigned x, y;
   for(y = 0; y < (RECTH-gap); y++){
      for(x = 0; x < (RECTW-gap); x++){
         if(sixel[i][j] == occupied){
            Neill_SDL_SetDrawColour(sw, rgb->r, rgb->g, rgb->b);
            SDL_RenderDrawPoint(sw->renderer, x+(ox+i*RECTW), y+(oy+j*RECTH));
          }
          else{
             Neill_SDL_SetDrawColour(sw, rgb->br, rgb->bg, rgb->bb);
             SDL_RenderDrawPoint(sw->renderer, x+(ox+i*RECTW), y+(oy+j*RECTH));
          }
      }
   }
}

void Sub_Tele_DrawChar(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char chr, int ox, int oy, Control *ctrl) {

   Color rgb;
   unsigned expand_leng=padding, h=NONE;

   Color_Init(&rgb);
   make_rgb(ctrl, &rgb);
   if(ctrl->height==hdouble_up) {expand_leng=twice; h=NONE;}
   else if(ctrl->height==hdouble_down) {expand_leng=twice; h=FNTHEIGHT;}
   Tele_DrawChar(sw, fontdata, chr, ox, oy, &rgb, expand_leng, h);
}

void Tele_DrawChar(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char chr, int ox, int oy, Color *rgb, unsigned expand_leng, unsigned h) {
   unsigned x, y;
   for(y = (0+h); y < (FNTHEIGHT+h); y++){
      for(x = 0; x < FNTWIDTH; x++){
         if(fontdata[chr-FNT1STCHAR][y/expand_leng] >> (FNTWIDTH - 1 - x) & 1){
            Neill_SDL_SetDrawColour(sw, rgb->r, rgb->g, rgb->b);
            SDL_RenderDrawPoint(sw->renderer, x + ox, y+oy);
         }
         else{
            Neill_SDL_SetDrawColour(sw, rgb->br, rgb->bg, rgb->bb);
            SDL_RenderDrawPoint(sw->renderer, x + ox, y+oy);
         }
      }
   }
}

/*----------------Provided ones----------------*/

/* Set up a simple (WIDTH, HEIGHT) window.
   Attempt to hide the renderer etc. from user. */
void Neill_SDL_Init(SDL_Simplewin *sw)
{

   if (SDL_Init(SDL_INIT_VIDEO) != 0) {
      fprintf(stderr, "\nUnable to initialize SDL:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   } 

   sw->finished = 0;
   
   sw->win= SDL_CreateWindow("SDL Window",
                          SDL_WINDOWPOS_UNDEFINED,
                          SDL_WINDOWPOS_UNDEFINED,
                          WWIDTH, WHEIGHT,
                          SDL_WINDOW_SHOWN);
   if(sw->win == NULL){
      fprintf(stderr, "\nUnable to initialize SDL Window:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   }

   sw->renderer = SDL_CreateRenderer(sw->win, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_TARGETTEXTURE);
   if(sw->renderer == NULL){
      fprintf(stderr, "\nUnable to initialize SDL Renderer:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   }

   SDL_SetRenderDrawBlendMode(sw->renderer,SDL_BLENDMODE_BLEND);

   /* Create texture for display */
   sw->display = SDL_CreateTexture(sw->renderer, SDL_PIXELFORMAT_RGBA8888, SDL_TEXTUREACCESS_TARGET, WWIDTH, WHEIGHT);
   if(sw->display == NULL){
      fprintf(stderr, "\nUnable to initialize SDL texture:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   }
   SDL_SetRenderTarget(sw->renderer, sw->display);

   /* Clear screen, & set draw colour to black */
   SDL_SetRenderDrawColor(sw->renderer, 0, 0, 0, 255);
   SDL_RenderClear(sw->renderer);

}

void Hidden_SDL_Init(SDL_Simplewin *sw)
{
   if (SDL_Init(SDL_INIT_VIDEO) != 0) {
      fprintf(stderr, "\nUnable to initialize SDL:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   } 

   sw->finished = 0;
   
   sw->win= SDL_CreateWindow("SDL Window",
                          SDL_WINDOWPOS_UNDEFINED,
                          SDL_WINDOWPOS_UNDEFINED,
                          WWIDTH, WHEIGHT,
                          SDL_WINDOW_HIDDEN);
   if(sw->win == NULL){
      fprintf(stderr, "\nUnable to initialize SDL Window:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   }

   sw->renderer = SDL_CreateRenderer(sw->win, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_TARGETTEXTURE);
   if(sw->renderer == NULL){
      fprintf(stderr, "\nUnable to initialize SDL Renderer:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   }

   SDL_SetRenderDrawBlendMode(sw->renderer,SDL_BLENDMODE_BLEND);

   /* Create texture for display */
   sw->display = SDL_CreateTexture(sw->renderer, SDL_PIXELFORMAT_RGBA8888, SDL_TEXTUREACCESS_TARGET, WWIDTH, WHEIGHT);
   if(sw->display == NULL){
      fprintf(stderr, "\nUnable to initialize SDL texture:  %s\n", SDL_GetError());
      SDL_Quit();
      exit(1);
   }
   SDL_SetRenderTarget(sw->renderer, sw->display);

   /* Clear screen, & set draw colour to black */
   SDL_SetRenderDrawColor(sw->renderer, 0, 0, 0, 255);
   SDL_RenderClear(sw->renderer);
}

/* Housekeeping to do with the render buffer & updating the screen */
void Neill_SDL_UpdateScreen(SDL_Simplewin *sw)
{
   SDL_SetRenderTarget(sw->renderer, NULL);
   SDL_RenderCopy(sw->renderer, sw->display, NULL, NULL);
   SDL_RenderPresent(sw->renderer);
   SDL_SetRenderTarget(sw->renderer, sw->display);
}

/* Gobble all events & ignore most */
void Neill_SDL_Events(SDL_Simplewin *sw)
{
   SDL_Event event;
   while(SDL_PollEvent(&event)) 
   {      
       switch (event.type){
          case SDL_QUIT:
          case SDL_MOUSEBUTTONDOWN:
          case SDL_KEYDOWN:
             sw->finished = 1;
       }
    }
}

/* Trivial wrapper to avoid complexities of renderer & alpha channels */
void Neill_SDL_SetDrawColour(SDL_Simplewin *sw, Uint8 r, Uint8 g, Uint8 b)
{
   SDL_SetRenderDrawColor(sw->renderer, r, g, b, SDL_ALPHA_OPAQUE);
}


void Neill_SDL_ReadFont(fntrow fontdata[FNTCHARS][FNTHEIGHT], char *fname)
{
    FILE *fp = fopen(fname, "rb");
    size_t itms;
    if(!fp){
       fprintf(stderr, "Can't open Font file %s\n", fname);
       exit(1);
   }
   itms = fread(fontdata, sizeof(fntrow), FNTCHARS*FNTHEIGHT, fp);
   if(itms != FNTCHARS*FNTHEIGHT){
       fprintf(stderr, "Can't read all Font file %s (%d) \n", fname, (int)itms);
       exit(1);
   }
   fclose(fp);
}

