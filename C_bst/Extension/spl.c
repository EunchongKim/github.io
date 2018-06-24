#include "bst.h"
#include <time.h>
#include <assert.h>
#define WORDSZ 30
#define CSEC (double)(CLOCKS_PER_SEC)
#define ARGV 3

void read_dic(FILE *fp, bst *b);
void misspelt_check(FILE *fp, bst* mis, bst *shuf);
char* myprintstr(const void* v);
int mystrcmp(const void* a, const void* b);

int main (int argc, char **argv) {

   FILE *fshuffle, *fdarkness;
   bst *bshuf, *misspelt;
   clock_t c1, c2;

   if (argc != ARGV) {
      ON_ERROR("Please type the filename\n");
   }
   fshuffle = fopen(argv[1], "r");
   fdarkness = fopen(argv[2], "r");

   if(fshuffle == NULL && fdarkness == NULL) {
      ON_ERROR("Cannot find the file\n");
   }

   bshuf = bst_init(WORDSZ, mystrcmp, myprintstr);
   misspelt = bst_init(WORDSZ, mystrcmp, myprintstr);

   read_dic(fshuffle, bshuf);
   c1 = clock();
   misspelt_check(fdarkness, misspelt, bshuf);
   c2 = clock();

   bst_free(&bshuf);
   bst_free(&misspelt);
   assert(bshuf==NULL);
   assert(misspelt==NULL);

   fclose(fshuffle);
   fclose(fdarkness);
   printf("%f\n", (double)(c2-c1)/CSEC);
   return 0;
}

/* Read texts from the novel, check if they are in the dictionary.
   If not, store them to the new tree and make it clear that 
   it doesn't come twice */
void misspelt_check(FILE *fp, bst* mis, bst *shuf) {

	char word[WORDSZ];
	while (fgets(word, WORDSZ, fp) != NULL) {
		if(bst_isin(mis, word) == false) {
			if(bst_isin(shuf, word) == false) {
				bst_insert(mis, word);
				printf("%s", word);
			}
		}
	}
}

/* Read words from the dictionary and make them a tree */
void read_dic(FILE *fp, bst *b) {

	char word[WORDSZ];
	while (fgets(word, WORDSZ, fp) != NULL) {
		bst_insert(b, word);
	}
}

char* myprintstr(const void* v) {
   return (char*)v;
}

int mystrcmp(const void* a, const void* b) {
   return strcmp(a, b);
}
