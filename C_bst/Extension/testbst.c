#include "bst.h"
#include <time.h>
#include <assert.h>

#define STRSIZE 20
#define WORDS 23

int mystrcmp(const void* a, const void* b);
int mychrcmp(const void* a, const void* b);
int myintcmp(const void* a, const void* b);
char* myprintstr(const void* v);
char* myprintchr(const void* v);
char* myprintint(const void* v);

void test_simpleinsert(void);
void test_insertarray(void);
void test_getordered(void);
void test_print(void);
void test_rebalance(void);

int main(void)
{

   printf("Beginning BST Test ...\n");

   test_simpleinsert();
   test_insertarray();
   test_getordered();
   test_rebalance();
   test_print();

   printf("Finished BST Test ...\n");
   return 0;

}

void test_simpleinsert(void)
{
   char str[STRSIZE];
   bst* b;
   b = bst_init(STRSIZE, mystrcmp, myprintstr);
   assert(bst_size(b)==0);
   strcpy(str, "dog");
   bst_insert(b, str);
   assert(bst_size(b)==1);
   assert(bst_isin(b, str));
   strcpy(str, "apple");
   bst_insert(b, str);
   assert(bst_size(b)==2);
   assert(bst_isin(b, str));
   strcpy(str, "garage");
   bst_insert(b, str);
   assert(bst_size(b)==3);
   assert(bst_isin(b, str));
   strcpy(str, "baggage");
   bst_insert(b, str);
   assert(bst_size(b)==4);
   assert(bst_isin(b, str));
   strcpy(str, "baggage");
   bst_insert(b, str);
   assert(bst_size(b)==4);
   assert(bst_isin(b, str));
   strcpy(str, "igloo");
   bst_insert(b, str);
   assert(bst_size(b)==5);
   assert(bst_isin(b, str));
   strcpy(str, "cat");
   bst_insert(b, str);
   assert(bst_size(b)==6);
   assert(bst_isin(b, str));
   strcpy(str, "eccentric");
   bst_insert(b, str);
   assert(bst_size(b)==7);
   assert(bst_isin(b, str));
   strcpy(str, "fresian");
   bst_insert(b, str);
   assert(bst_size(b)==8);
   assert(bst_isin(b, str));
   strcpy(str, "hotel");
   bst_insert(b, str);
   assert(bst_size(b)==9);
   assert(bst_isin(b, str));
   strcpy(str, "jaguar");
   bst_insert(b, str);
   assert(bst_size(b)==10);
   assert(bst_isin(b, str));
   assert(bst_maxdepth(b)==4);

   bst_free(&b);
   assert(b==NULL);
}

void test_insertarray(void)
{
   char words1[WORDS][STRSIZE] = {"it", "is", "a", "truth", "universally", "acknowledged", "that",  "a", "single", "man", "in", "possession", "of", "a", "good", "fortune", "must", "be", "in", "want", "of", "a", "wife"};
   bst* b = bst_init(STRSIZE, mystrcmp, myprintstr);
   bst_insertarray(b, words1, WORDS);
   assert(bst_size(b)==18);
   bst_free(&b);
   assert(b==NULL);
}

void test_getordered(void)
{
   int i, sc;
   char words1[WORDS][STRSIZE] = {"it", "is", "a", "truth", "universally", "acknowledged", "that",  "a", "single", "man", "in", "possession", "of", "a", "good", "fortune", "must", "be", "in", "want", "of", "a", "wife"};
   char words2[WORDS][STRSIZE];
   bst* b = bst_init(STRSIZE, mystrcmp, myprintstr);
   bst_insertarray(b, words1, WORDS);
   assert(bst_size(b)==18);
   bst_getordered(b, words2);
   for(i=0; i<17; i++){
      sc = strcmp(words2[i], words2[i+1]);
      assert(sc<0);
   }
   bst_free(&b);
   assert(b==NULL);
}

void test_rebalance(void)
{
   int i;
   bst* b = bst_init(sizeof(int), myintcmp, myprintint);
   bst* rb;
   /* Sorted List, bad for building tree */
   for(i=0; i<2048; i++){
      bst_insert(b, &i);
   }
   assert(bst_maxdepth(b)==i);
   rb = bst_rebalance(b);
   assert(bst_maxdepth(rb)==12);
   bst_free(&b);
   bst_free(&rb);
}

void test_print(void)
{
   int i, sc;
   char istr[] = "MNKIDFGH";
   char pstr[] = "(M(K(I(D(F(G(H))))))(N))";
   char* ostr;
   bst* b;
   b = bst_init(sizeof(char), mychrcmp, myprintchr);
   assert(b!=NULL);
   assert(bst_size(b)==0);
   for(i=0; i<(int)strlen(istr); i++){
      bst_insert(b, &istr[i]);
      assert(bst_isin(b,&istr[i]));
      assert(bst_size(b)==(i+1));
   }
   ostr = bst_print(b);
   sc = strcmp(pstr,ostr);
   assert(sc==0);
   free(ostr);
   bst_free(&b);
}

char* myprintstr(const void* v)
{
   return (char*)v;
}

char* myprintchr(const void* v)
{
   static char str[100];
   sprintf(str, "%c", *(char*)v);
   return str;
}

char* myprintint(const void* v)
{
   static char str[100];
   sprintf(str, "%d", *(int*)v);
   return str;
}

int mystrcmp(const void* a, const void* b)
{
   return strcmp(a, b);
}

int mychrcmp(const void* a, const void* b)
{
   return *(char*)a - *(char*)b;
}

int myintcmp(const void* a, const void* b)
{
   return *(int*)a - *(int*)b;
}

