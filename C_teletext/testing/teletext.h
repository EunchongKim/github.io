#include <SDL.h>
#include <math.h>

#define WWIDTH 800
#define WHEIGHT 600

/* Font stuff */
typedef unsigned short fntrow;
#define FNTWIDTH (sizeof(fntrow)*8)
#define FNTHEIGHT 18
#define FNTCHARS 96
#define FNT1STCHAR 32

#define SDL_8BITCOLOUR 256

#ifndef M_PI
#define M_PI           3.14159265358979323846
#endif

#define NONE 0
#define PREV 1
#define NEXT 1
#define EXIT_ERROR 1
#define padding 1
#define occupied 1
#define ARGV 2
#define COL 2
#define twice 2
#define ROW 3
#define FUNCTIONS 12
#define LINE 40
#define CONVERT 128
#define BASE_CODE 160
#define LENG 1000
#define RECTW FNTWIDTH/2
#define RECTH FNTHEIGHT/3
#define NEWLINE (pos+1)%LINE
/* For sixel, FL = First left cell,
   TR = Third Right cell */
#define FL 1
#define FR 2
#define SL 4
#define SR 8
#define TL 16
#define TR 64
/* For sixel, F,S,T = 1,2,3 row,
   L,R = 0,1 col */
#define F 0
#define S 1
#define T 2
#define L 0
#define R 1

/*-------- For Extention --------*/
#define DEFAULT 0
#define MATCH 0
#define BACK 0
#define FORE 1
#define TRUE 1
#define IN 1
#define OUT 2
#define SPACE 2
#define ARGV_NUM 3
#define SETNUM 100
#define TAGL 100
#define WORDSL 126
#define MAXL 2000
/* For tags */
#define HEAD "<H3"
#define D_HEAD "<H1"
#define PARA "<P"
#define LIST "<li"
#define LITYPE "list-style"
#define SQUARE "square"
#define BACKCOL "background"
#define COLOR "color"
#define CENTER "center"
#define RIGHT "right"
#define RED "red"
#define GREEN "green"
#define BLUE "blue"
#define YELLOW "yellow"
#define MAGENTA "magenta"
#define CYAN "cyan"
#define WHITE "white"
#define BLACK "black"
#define center 1
#define firpos 1
#define right 2

/* All info required for windows / renderer & event loop */
struct SDL_Simplewin {
   SDL_bool finished;
   SDL_Window *win;
   SDL_Renderer *renderer;
   SDL_Texture *display;
};
typedef struct SDL_Simplewin SDL_Simplewin;

/* Store control codes' instructions */
typedef struct Control {
   int color;
   int height;
   int graphic;
   int backcol;
   int hold;
} Control;

typedef struct Color {
   Uint8 r;
   Uint8 g;
   Uint8 b;
   Uint8 br;
   Uint8 bg;
   Uint8 bb;
} Color;

typedef enum code {basic, red, green, yellow, blue, magenta, cyan, white, black,
                   hsingle, hdouble_up, hdouble_down, cont, seper, held} Code;
/* Hide SDL screen */
void Hidden_SDL_Init(SDL_Simplewin *sw);
void Neill_SDL_Init(SDL_Simplewin *sw);
void Neill_SDL_Events(SDL_Simplewin *sw);
void Neill_SDL_SetDrawColour(SDL_Simplewin *sw, Uint8 r, Uint8 g, Uint8 b);
void Neill_SDL_ReadFont(fntrow fontdata[FNTCHARS][FNTHEIGHT], char *fname);
void Neill_SDL_UpdateScreen(SDL_Simplewin *sw);

void Control_Init(Control *ctrl);
void Color_Init(Color *rgb);
void Control_Code(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG], Control *ctrl);
void Read_Input(unsigned char words[LENG], int pos, Control *ctrl);
unsigned char Each_Control(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG], Control *ctrl, int pos, int ox, int oy);
void make_rgb(Control *ctrl, Color *rgb);
void Sub_Tele_Graphic(SDL_Simplewin *sw, unsigned char chr, int ox, int oy, Control *ctrl);
void Tele_Graphic(SDL_Simplewin *sw, unsigned char chr, int ox, int oy, Color *rgb, unsigned gap, Control *ctrl, unsigned char sixel[COL][ROW]);
void Calcul_Graphic(unsigned char chr, unsigned char sixel[COL][ROW]);
void Base_sixel(unsigned char sixel[COL][ROW]);
void DrawGraphic(SDL_Simplewin *sw, int ox, int oy, Color *rgb, unsigned gap, unsigned char sixel[COL][ROW], int i, int j);
void Sub_Tele_DrawChar(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char chr, int ox, int oy, Control *ctrl);
void Tele_DrawChar(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char chr, int ox, int oy, Color *rgb, unsigned expand_leng, unsigned h);

/*--------For Test--------*/
void read_file(FILE *file, unsigned char words[LENG]);
/*--------First Test for checking each function--------*/
void First_Test(unsigned char words[LENG]);
void Test_Control_Init(int *errors, int *times, Control *ctrl);
void Test_Control_Code(int *errors, int *times, unsigned char words[LENG], Control *ctrl);
void Test_Read_Input(int *errors, int *times, unsigned char words[LENG], int pos, Control *ctrl);
unsigned char Test_Each_Control(int *errors, int *times, unsigned char words[LENG], Control *ctrl, int pos, int ox, int oy);
void Test_make_rgb(int *times, Control *ctrl, Color *rgb);
void Test_Sub_Tele_Graphic(int *errors, int *times, unsigned char chr, int ox, int oy, Control *ctrl);
void Test_Tele_Graphic(int *errors, int *times, unsigned char chr, int ox, int oy, Color *rgb, unsigned gap, Control *ctrl, unsigned char sixel[COL][ROW]);
void Test_Calcul_Graphic(int *errors, int *times, unsigned char chr, unsigned char sixel[COL][ROW]);
void Test_Base_sixel(int *errors, int *times, unsigned char sixel[COL][ROW]);
void Test_DrawGraphic(int *times, int ox, int oy, Color *rgb, unsigned gap, unsigned char sixel[COL][ROW], int i, int j);
void Test_Sub_Tele_DrawChar(int *errors, int *times, unsigned char chr, int ox, int oy, Control *ctrl);
void Test_Tele_DrawChar(int *times, unsigned char chr, int ox, int oy, Color *rgb, unsigned expand_leng, unsigned h);
/*--------Second Test for checking each instruction--------*/
void Second_Test(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char text[LENG], unsigned char panda[LENG], unsigned char lfc[LENG]);
void Common_Test(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG]);
void Color_Test(unsigned char words[LENG]);
void Back_Color_Test(unsigned char words[LENG]);
void Double_H_Test(unsigned char words[LENG]);
void Text_Test(SDL_Simplewin *sw, fntrow fontdata[FNTCHARS][FNTHEIGHT], unsigned char words[LENG]);
void Graphic_Mode_Test(unsigned char words[LENG]);
void Sixel_Test(SDL_Simplewin *sw, unsigned char words[LENG]);
void make_ident(unsigned char sixel[COL][ROW], unsigned char new_s[COL][ROW]);

