#include "bst.h"
#include <time.h>
#include <assert.h>
#define WORDSZ 35
#define CSEC (double)(CLOCKS_PER_SEC)
#define ARGV 3
#define PRIME 700109

void make_hash(char* shuf, char** words);
void misspelt_check(char* dark, char** words);
int word_check(char** words, char* word, int index);
int hash(char* s);
void free_words(char*** words);

int main (int argc, char **argv) {

   char **words;
   unsigned int i;
   clock_t c1, c2;

   if (argc != ARGV) {
      ON_ERROR("Please type the filename\n");
   }

   words = (char **)calloc(PRIME, sizeof(char *));
   for (i=0;i<PRIME;i++) {
      words[i] = (char *)calloc(WORDSZ, sizeof(char));
   }
   make_hash(argv[1], words);
   c1 = clock();
   misspelt_check(argv[2], words);
   c2 = clock();
   printf("%f\n", (double)(c2-c1)/CSEC);
   
   free_words(&words);
   assert(words==NULL);
   return 0;
}

/* Read texts from the novel, check if the hash numbers of the words
   match to the dictionary */
void misspelt_check(char* dark, char** words) {

   char word[WORDSZ]; int index;
   FILE *fdarkness = fopen(dark, "r");
   if(fdarkness==NULL) {
      ON_ERROR("Can't open the file\n");
   }

   while (fscanf(fdarkness, "%s", word) != EOF) {
      index = hash(word);
      /* Check if the index of the word's hash number is empty,
         print it as misspelt word */
      if(word_check(words,word,index));
      else {
         /* Considering collision cases,
            double check the word from the novel is stored in
            other indexes */
         while(strcmp(words[index],word) != 0) {
            index++;
            word_check(words,word,index);
         }
      }
   }
   fclose(fdarkness);
}

/* Small function for 'misspelt_check'
   If the index of the array is empty, print it,
   and store it into the array to prevent it from printing twice */
int word_check(char** words, char* word, int index) {

   if(strcmp(words[index],"")==0) {
      printf("%s\n", word);
      strcpy(words[index],word);
      return 1;
   }
   return 0;
}

/* Read words from the dictionary, make hash numbers,
   and store the words into the array
   having the indexes of each hash numbers */
void make_hash(char* shuf, char** words) {

   char word[WORDSZ]; int index;
   FILE *fshuffle = fopen(shuf, "r");
   if(fshuffle==NULL) {
      ON_ERROR("Can't open the file\n");
   }

   while (fscanf(fshuffle, "%s", word) != EOF) {
      index = hash(word);
      /* If the index of array is already occupied, increment index
         while finding the empty position */
      while(strcmp(words[index],"") != 0) {
         index++;
      }
      strcpy(words[index], word);
   }
   fclose(fshuffle);
}

/* Return hash numbers per words */
int hash(char *s) {
   /* Magic number to make hash numbers */
   unsigned long hash_num = 5381;
   int c;
   while((c=(*s++))) {
      hash_num = 33 * hash_num + c;
   }
   /* Divide hash numbers to a prime number to reduce collisions */
   return (int)(hash_num%PRIME);
}

void free_words(char*** words) {

   unsigned int i;
   char** for_free = *words;
   for(i=0;i<PRIME;i++) {
      free(for_free[i]);
   }
   free(for_free);
   *words=NULL;
}
