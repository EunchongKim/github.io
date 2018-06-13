#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "teletext.h"

int main(int argc, char **argv) {

   char html[MAXL];
   unsigned char tele[LENG];
   Table table;
   char* name_html;
   char* name_tele;
   int html_leng;
   if(argc != ARGV_NUM) {
      fprintf(stderr, "%s", "Please type the filename.\n");
      exit(EXIT_FAILURE);
   }

   name_html = argv[IN];
   name_tele = argv[OUT];
   html_leng = read_html(html, name_html);

   Table_Init(&table);
   HTML_Lexer(html, &table, html_leng);
   Tag_Parser(&table);
   Generate_Tele(&table, tele);

   write_file(tele, name_tele);
   return DEFAULT;
}

int read_html(char html[MAXL], char* name) {

   FILE *ftest;
   int i=DEFAULT;

   ftest = fopen(name, "r");
   while(i<MAXL && (fscanf(ftest, "%c", &html[i])) != EOF) {
      if(html[i]=='\n') {i--;}
      i++;
   }
   fclose(ftest);
   return i;
}

void write_file(unsigned char tele[LENG], char* name) {

   FILE *fhtml;
   int i=DEFAULT;

   fhtml = fopen(name, "w");
   while(i<LENG && (fprintf(fhtml, "%c", tele[i])) != EOF) {
      i++;
   }
   fclose(fhtml);
}

void Symbol_Init(Symbol *s) {

   reset_tag(s->tag);
   reset_words(s->words);
   s->color=DEFAULT; s->backcol=DEFAULT; s->text=DEFAULT;
   s->size=DEFAULT; s->br=DEFAULT; s->list=DEFAULT;
}

void Table_Init(Table *t) {

   int i=DEFAULT;
   for(i=0;i<SETNUM;i++) {
      Symbol_Init(&(t->set[i]));
   }
   t->sz=DEFAULT;
}

void Stack_Init(Stack *s) {
   s->top=DEFAULT;
}

/* Firstly, divide text to print and tags
   and store them to structure */
void HTML_Lexer(char html[MAXL], Table *tb, int leng) {

   int i=DEFAULT, j=DEFAULT, l=DEFAULT,
       text_index=DEFAULT, skip=DEFAULT;
   Stack s;
   char t[TAGL], w[WORDSL];
   reset_tag(t);
   reset_words(w);
   Stack_Init(&s);

   for(i=0;i<leng;i++) {
      /* Push */
      Push(&s, html[i]);
      l++;
      /* If '>' comes, the next one is '<'
         and it is not closing(like </TITLE>),
         Pop and store as a tag to the table
         (<HTML><HEAD>..) */
      if(skip==DEFAULT && html[i]=='>' && html[i+NEXT]=='<') {
         for(j=l-PREV;j>=0;j--) {
            t[j]=Pop(&s);
         }
         Table_Set(tb, t, w);
         reset_tag(t);
         l=DEFAULT;
      }
      /* If '>' comes, the next one is not '<',
         assuming it as a text */
      else if(html[i]=='>' && skip==DEFAULT && html[i+NEXT]!='<') {
         for(j=l-padding;j>=0;j--) {
            t[j]=Pop(&s);
         }
         l=DEFAULT;
         text_index=TRUE;
      }
      /* If text index is ture, pop words to store when '<' comes */
      else if(text_index==TRUE && skip==DEFAULT && html[i+NEXT]=='<') {
         for(j=l;j>0;j--) {
            w[j]=Pop(&s);
         }
         Table_Set(tb, t, w);
         reset_tag(t);
         reset_words(w);
         l=DEFAULT;
         text_index=DEFAULT;
      }
      /* Skip closing tags */
      else if(html[i]=='<' && html[i+NEXT]=='/') {
         skip=TRUE;
      }
      else if(skip==TRUE && html[i]=='>') {
         for(j=l-PREV;j>=0;j--) {
            t[j]=Pop(&s);
         }
         l=DEFAULT;
         reset_tag(t);
         reset_words(w);
         skip=DEFAULT;
      }
   }
}

void Table_Set(Table *tb, char t[TAGL], char w[WORDSL]) {

   memcpy(tb->set[tb->sz].words, w, WORDSL);
   memcpy(tb->set[tb->sz].tag, t, TAGL);
   (tb->sz)++;
}

void reset_tag(char t[TAGL]) {
   int i=DEFAULT;
   for(i=0;i<TAGL;i++) {
      t[i]='\0';
   }
}

void reset_words(char w[WORDSL]) {
   int i=DEFAULT;
   for(i=0;i<WORDSL;i++) {
      w[i]='\0';
   }
}

void Push(Stack *s, char chr) {
   assert(s->top < MAXL);
   s->stk[s->top] = chr;
   s->top = s->top+1;
}

unsigned char Pop(Stack *s) {
   assert(s->top > 0);
   s->top = s->top-PREV;
   return s->stk[s->top];
}

void Tag_Parser(Table *tb) {
   int i=DEFAULT, j=DEFAULT, l=DEFAULT, check=DEFAULT;
   char t[TAGL], w[WORDSL];
   char subt[TAGL];
   Stack s;
   reset_tag(t);
   Stack_Init(&s);

   /* Check each tag inside the set */
   for(i=0;i<tb->sz;i++) {
      j=DEFAULT;
      memcpy(t, tb->set[i].tag, TAGL);
      memcpy(w, tb->set[i].words, WORDSL);
      /* check if the tag contains words to print or not */
      check = check_text(w);
      /* If there are words, set the text instruction as occupied */
      if(check!=DEFAULT) {tb->set[i].text=TRUE;}
      /* Parsing each tag to store each instruction */
      while(t[j]!='\0') {
         Push(&s, t[j]);
         l++;
         if(t[j]=='>') {
            Small_Parser(subt, l, &s);
            Set_Ins(subt, &(tb->set[i]));
            reset_tag(subt);
            l=DEFAULT;
         }
         else if(t[j]==' ') {
            Small_Parser(subt, l, &s);
            Set_Ins(subt, &(tb->set[i]));
            reset_tag(subt);
            l=DEFAULT;
         }
         else if(t[j]=='"') {
            Small_Parser(subt, l, &s);
            Set_Ins(subt, &(tb->set[i]));
            reset_tag(subt);
            l=DEFAULT;
         }
         j++;
      }
   }
}

int check_text(char w[WORDSL]) {
   int i, check=DEFAULT;
   for(i=0;i<WORDSL;i++) {
      if(w[i]!='\0') {check++;}
   }
   return check;
}

void Small_Parser(char subt[TAGL], int l, Stack *s) {
   int i;
   for(i=l;i>0;i--) {
      subt[i-PREV]=Pop(s);
   }
}

/* Set instructions by parsing again and interpreting HTML tags */
void Set_Ins(char subt[TAGL], Symbol *s) {
   int i=DEFAULT, l=DEFAULT, selec=BACK;
   char ins[TAGL];
   Stack stk;
   Stack_Init(&stk);
   reset_tag(ins);

   /* If tag equals to '<H3', give a new next line */
   if(memcmp(subt, HEAD, (strlen(HEAD)))==MATCH) {
      s->br=TRUE;
   }
   /* If tag equals to '<H1', set double height */
   if(memcmp(subt, D_HEAD, (strlen(D_HEAD)))==MATCH) {
      s->br=TRUE;
      s->size=TRUE;
   }
   /* If tag equals to '<P', give a new next line */
   if(memcmp(subt, PARA, (strlen(PARA)))==MATCH) {
      s->br=TRUE;
   }
   /* If tag equals to '<li', set as a list */
   if(memcmp(subt, LIST, (strlen(LIST)))==MATCH) {
      s->br=DEFAULT;
      s->list=TRUE;
      s->litype=TRUE;
   }
   /* If tag equals to 'center', set align as center */
   if(memcmp(subt, CENTER, (strlen(CENTER)))==MATCH) {
      s->align=center;
   }
   /* If tag equals to 'right', set align as right */
   if(memcmp(subt, RIGHT, (strlen(RIGHT)))==MATCH) {
      s->align=right;
   }
   /* If tag contains background color information
      parsing and getting it to set right instruction */
   if(memcmp(subt, BACKCOL, (strlen(BACKCOL)))==MATCH) {
      selec=BACK;
      while(subt[i]!='\0') {
         Push(&stk, subt[i]);
         l++;
         if(subt[i]==':') {
            Small_Parser(ins, l, &stk);
            reset_tag(ins);
            l=DEFAULT;
         }
         else if(subt[i]==';' || subt[i]=='"') {
            Small_Parser(ins, l, &stk);
            Set_Color(ins, s, selec);
            reset_tag(ins);
            l=DEFAULT;
         }
         i++;
      }
   }
   /* If tag contains foreground color information
      parsing and getting it to set right instruction */
   if(memcmp(subt, COLOR, (strlen(COLOR)))==MATCH) {
      i=DEFAULT; l=DEFAULT; selec=FORE;
      while(subt[i]!='\0') {
         Push(&stk, subt[i]);
         l++;
         if(subt[i]==':') {
            Small_Parser(ins, l, &stk);
            reset_tag(ins);
            l=DEFAULT;
         }
         else if(subt[i]==';' || subt[i]=='"') {
            Small_Parser(ins, l, &stk);
            Set_Color(ins, s, selec);
            reset_tag(ins);
            l=DEFAULT;
         }
         i++;
      }
   }
   /* If tag contains list type information
      parsing and getting it to set right instruction */
   if(memcmp(subt, LITYPE, (strlen(LITYPE)))==MATCH) {
      i=DEFAULT; l=DEFAULT;
      while(subt[i]!='\0') {
         Push(&stk, subt[i]);
         l++;
         if(subt[i]==':') {
            Small_Parser(ins, l, &stk);
            reset_tag(ins);
            l=DEFAULT;
         }
         else if(subt[i]==';' || subt[i]=='"') {
            Small_Parser(ins, l, &stk);
            Set_Shape(ins, s);
            reset_tag(ins);
            l=DEFAULT;
         }
         i++;
      }
   }
}

void Set_Shape(char ins[TAGL], Symbol *s) {

   if(memcmp(ins, SQUARE, (strlen(SQUARE)))==MATCH) {
      s->litype=TRUE;
   }
}

/* Set color instructions by drawing a meaning from
   HTML tags */
void Set_Color(char ins[TAGL], Symbol *s, int selec) {

   if(memcmp(ins, RED, (strlen(RED)))==MATCH) {
      if(selec==BACK) {s->backcol=red;}
      if(selec==FORE) {s->color=red;}
   }
   if(memcmp(ins, GREEN, (strlen(GREEN)))==MATCH) {
      if(selec==BACK) {s->backcol=green;}
      if(selec==FORE) {s->color=green;}
   }
   if(memcmp(ins, BLUE, (strlen(BLUE)))==MATCH) {
      if(selec==BACK) {s->backcol=blue;}
      if(selec==FORE) {s->color=blue;}
   }
   if(memcmp(ins, YELLOW, (strlen(YELLOW)))==MATCH) {
      if(selec==BACK) {s->backcol=yellow;}
      if(selec==FORE) {s->color=yellow;}
   }
   if(memcmp(ins, MAGENTA, (strlen(MAGENTA)))==MATCH) {
      if(selec==BACK) {s->backcol=magenta;}
      if(selec==FORE) {s->color=magenta;}
   }
   if(memcmp(ins, CYAN, (strlen(CYAN)))==MATCH) {
      if(selec==BACK) {s->backcol=cyan;}
      if(selec==FORE) {s->color=cyan;}
   }
   if(memcmp(ins, WHITE, (strlen(WHITE)))==MATCH) {
      if(selec==BACK) {s->backcol=white;}
      if(selec==FORE) {s->color=white;}
   }
   if(memcmp(ins, BLACK, (strlen(BLACK)))==MATCH) {
      if(selec==BACK) {s->backcol=black;}
      if(selec==FORE) {s->color=black;}
   }
}

void Base_Tele (unsigned char tele[LENG]) {
   int i;
   for(i=0;i<LENG;i++) {
      tele[i]=0xa0;
   }
}

void Generate_Tele(Table *tb, unsigned char tele[LENG]) {

   int i=DEFAULT, pos=DEFAULT, d=DEFAULT;
   /* Fill teletext characters as '0xa0' as the base */
   Base_Tele(tele);
   /* Before tag instruction numbers and teletext
      don't exceed their limits, */
   while((i<(tb->sz)) && pos < LENG) {
      /* If it has a background color, set it */
      if((tb->set[i].backcol)!=DEFAULT) {
         Gener_Backcol(&(tb->set[i]), tele, &pos);
      }
      /* If it has a foreground color, set it */
      if((tb->set[i].color)!=DEFAULT) {
         Gener_Color(&(tb->set[i]), tele, &pos);
      }
      /* If it has a text to print, set it */
      if((tb->set[i].text)==TRUE) {
         Gener_Text(&(tb->set[i]), tele, &pos);
      }
      /* if double height is true,
         copy the previous line to the next line */
      if((tb->set[i].size)==TRUE) {
         while(d<LINE) {
            tele[pos] = tele[pos-LINE];
            pos++;
            d++;
         }
      }
      /* If the line needs to the next blank line
         like a paragraph and a heading,
         add a new line after the current line */
      if((tb->set[i].br)==TRUE) {
         while(((pos)%LINE)!=DEFAULT) {pos++;}
         pos = pos+LINE;
      }
      /* If it is not the end of the line, skip to there,
         start a new instruction */
      while(((pos)/LINE)!=DEFAULT && ((pos)%LINE)!=DEFAULT) {
         pos++;
      }
      i++;
   }
}

/* Generate teletext according to text instructions */
void Gener_Text(Symbol *s, unsigned char tele[LENG], int *pos) {

   int i=firpos, leng=DEFAULT, mid=DEFAULT, r=DEFAULT;

   /* Check the length of text */
   while((s->words[i])!='\0') {
      i++;
      leng++;
   }

   /* If double height is true,
      put the double height control code,
      move position to the next,
      put the words by converting them to teletext */
   if(s->size==TRUE) {
      tele[(*pos)]=0x8d;
      (*pos)=(*pos)+SPACE;
      for(i=firpos;i<=leng;i++) {
         tele[(*pos)] = (s->words[i])+CONVERT;
         (*pos)++;
      }
   }
   /* If align is center,
      give some paddings before starting the line */
   else if(s->align==center) {
      if(s->color==white || s->backcol==DEFAULT) {
         mid=((LINE-leng)/twice)-padding;
      }
      /* If other color codes were used */
      else {
         /* Reduce the padding */
         mid=((LINE-leng)/twice)-SPACE;
      }
      (*pos) = (*pos)+mid;
      for(i=firpos;i<=leng;i++) {
         tele[(*pos)] = (s->words[i])+CONVERT;
         (*pos)++;
      }
   }
   /* If align is right,
      give some paddings before starting the line */
   else if(s->align==right) {
      r=LINE-leng-SPACE;
      (*pos) = (*pos)+r;
      for(i=firpos;i<=leng;i++) {
         tele[(*pos)] = (s->words[i])+CONVERT;
         (*pos)++;
      }
   }
   /* If the line is list, draw a bullet as square shape
      at the beginning of the line */
   else if(s->list==TRUE) {
      tele[(*pos)] = 0xff;
      (*pos)++;
      for(i=firpos;i<=leng;i++) {
         tele[(*pos)] = (s->words[i])+CONVERT;
         (*pos)++;
      }
   }
   /* put the words by converting them to teletext */
   else {
      for(i=firpos;i<=leng;i++) {
         tele[(*pos)] = (s->words[i])+CONVERT;
         (*pos)++;
      }
   }
}

/* Generate teletext according to foreground color instructions */
void Gener_Color(Symbol *s, unsigned char tele[LENG], int *pos) {

   if(s->color==red) {tele[(*pos)]=0x81; (*pos)++;}
   else if(s->color==green) {tele[(*pos)]=0x82; (*pos)++;}
   else if(s->color==yellow) {tele[(*pos)]=0x83; (*pos)++;}
   else if(s->color==blue)  {tele[(*pos)]=0x84; (*pos)++;}
   else if(s->color==magenta) {tele[(*pos)]=0x85; (*pos)++;}
   else if(s->color==cyan) {tele[(*pos)]=0x86; (*pos)++;}
   else if(s->color==white) {tele[(*pos)]=0x87; (*pos)++;}
}

/* Generate teletext according to background color instructions */
void Gener_Backcol(Symbol *s, unsigned char tele[LENG], int *pos) {
   if(s->backcol==black) {tele[*pos]=0x9c; (*pos)++;}
   else if(s->backcol==red) {
      tele[(*pos)]=0x81; tele[(*pos)+NEXT]=0x9d; (*pos)=(*pos)+SPACE;
   }
   else if(s->backcol==green) {
      tele[(*pos)]=0x82; tele[(*pos)+NEXT]=0x9d; (*pos)=(*pos)+SPACE;
   }
   else if(s->backcol==yellow) {
      tele[(*pos)]=0x83; tele[(*pos)+NEXT]=0x9d; (*pos)=(*pos)+SPACE;
   }
   else if(s->backcol==blue) {
      tele[(*pos)]=0x84; tele[(*pos)+NEXT]=0x9d; (*pos)=(*pos)+SPACE;
   }
   else if(s->backcol==magenta) {
      tele[(*pos)]=0x85; tele[(*pos)+NEXT]=0x9d; (*pos)=(*pos)+SPACE;
   }
   else if(s->backcol==cyan) {
      tele[(*pos)]=0x86; tele[(*pos)+NEXT]=0x9d; (*pos)=(*pos)+SPACE;
   }
   else if(s->backcol==white) {
      tele[(*pos)]=0x87; tele[(*pos)+NEXT]=0x9d; (*pos)=(*pos)+SPACE;
   }
}

