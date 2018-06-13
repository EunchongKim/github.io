#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#include "set.h"
#define ARRDEFSZ 32

void makelist(set *s, arrtype *l, FILE *ifp);
int main(void) {

   FILE *ifpride, *ifpsense;
   set *pride, *sense, *common;
   arrtype *tempdata;

   ifpride = fopen("pride-and-prej.txt", "r");
   ifpsense = fopen("sense-and-sense.txt", "r");

   pride = set_init();
   sense = set_init();
   tempdata = (arrtype *)malloc(ARRDEFSZ*sizeof(arrtype));

   makelist(pride, tempdata, ifpride);
   makelist(sense, tempdata, ifpsense);
   assert(pride != NULL);
   assert(sense != NULL);

   common = set_intersection(pride, sense);

   printf("There are %d unique words in sense-and-sense.txt\n", set_size(sense));
   printf("There are %d unique words in pride-and-prej.txt\n", set_size(pride));
   printf("There are %d common words\n", set_size(common));

   fclose(ifpride);
   fclose(ifpsense);

   set_free(&pride);
   set_free(&sense);
   set_free(&common);
   assert(pride == NULL);
   assert(sense == NULL);
   assert(common == NULL);

   return 0;
}

/* Read strings from a file and put them into the set s */
void makelist(set *s, arrtype *l, FILE *ifp) {
   while(fgets(l->str, ARRDEFSZ, ifp) != NULL) {
      set_insert(s, *l);
   }
}
