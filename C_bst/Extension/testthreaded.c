#include "bst.h"
#include <time.h>
#include <assert.h>
#define CSEC (double)(CLOCKS_PER_SEC)

#define STRSIZE 20
#define INTSIZE 10
#define WORDS 23

int mystrcmp(const void* a, const void* b);
int mychrcmp(const void* a, const void* b);
int myintcmp(const void* a, const void* b);
char* myprintstr(const void* v);
char* myprintchr(const void* v);
char* myprintint(const void* v);

void threaded_getordered(void);
void threaded_getordered_int(void);
void test_rebalance(void);

int main(void)
{
   clock_t c1, c2;
   c1 = clock();
   printf("Beginning BST Test ...\n");

   threaded_getordered();
   threaded_getordered_int();
   test_rebalance();

   printf("Finished BST Test ...\n");
   c2 = clock();
   printf("%f\n", (double)(c2-c1)/CSEC);
   return 0;

}

void threaded_getordered_int(void)
{
   int i;
   int num1[] = {1, 2, 3, 4, 5, 6, 7, 8};
   int num2[8];
   bst* b = bst_init(sizeof(int), mystrcmp, myprintstr);
   bst_insertarray(b, num1, 8);
   assert(bst_size(b)==8);
   bst_threaded(b, num2);
   for(i=0; i<7; i++){
      assert(num2[i] < num2[i+1]);
   }
   bst_free(&b);
   assert(b==NULL);
}

void threaded_getordered(void)
{
   int i, sc;
   char words1[WORDS][STRSIZE] = {"it", "is", "a", "truth", "universally", "acknowledged", "that",  "a", "single", "man", "in", "possession", "of", "a", "good", "fortune", "must", "be", "in", "want", "of", "a", "wife"};
   char words2[WORDS][STRSIZE];
   bst* b = bst_init(STRSIZE, mystrcmp, myprintstr);
   bst_insertarray(b, words1, WORDS);
   assert(bst_size(b)==18);
   bst_threaded(b, words2);
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
   for(i=0; i<2048; i++){
      bst_insert(b, &i);
   }
   assert(bst_maxdepth(b)==i);
   rb = bst_rebalance(b);
   assert(bst_maxdepth(rb)==12);
   bst_free(&b);
   bst_free(&rb);
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

