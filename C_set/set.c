#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "set.h"

int set_isin(set *s, arrtype l, int *i);
void element_remove(set* s, int i);

/* Creat the empty set */
set* set_init() {

   set *s;
   /* Allocate a set with a minimum size */
   s = (set *)calloc(1, sizeof(set));
   if(s == NULL) {
      ON_ERROR("Creation of Set Failed\n");
   }
   s->ua = arr_init();
   s->sz = 0;
   return s;
}

/* Create new set, copied from another */
set* set_copy(set* s) {

   set *l; int i;
   l = set_init();
   /* If s is NULL, return an empty set */
   if(s == NULL) {
      return l;
   }
   /* If not, copy the value of set s to a new set */
   for(i=0; i<s->sz; i++) {
      arr_set(l->ua, i, arr_get(s->ua, i));
   }
   l->sz = s->sz;
   return l;
}

/* Create new set, copied from an array of length n */
set* set_fromarray(arrtype* a, int n) {

   set *s; int i;
   s = set_init();
   /* If n is 0, return an empty set */
   if(n == 0) {
      return s;
   }
   if(n < 0){
      ON_ERROR("Array accessed with negative index ...\n");
   }
   for(i=0; i<n; i++) {
      set_insert(s, a[i]);
   }
   return s;
}

/* Add one element into the set without a duplicated one */
void set_insert(set* s, arrtype l) {

   if(s == NULL) {
      return ;
   }
   /* If the element is already in the set, end the function */
   if(set_contains(s, l) == 1) {
      return ;
   }
   arr_set(s->ua, s->sz, l);
   s->sz++;
}

/* Return size of the set */
int set_size(set* s) {

   if(s == NULL) {
      return 0;
   }
   return s->sz;
}

/* Return true if l is in the set, false elsewise */
int set_contains(set* s, arrtype l) {

   int i;
   if(s == NULL) {
      return 0;
   }
   for(i=0; i<set_size(s); i++) {
      if(set_isin(s, l, &i)) return 1;
   }
   return 0;
}

/* Return true if l is in the array - i is the position */
/* Choose a proper condition according to the arrtype */
int set_isin(set *s, arrtype l, int *i) {

   #ifdef ARRSTRINGS
      arrtype j = arr_get(s->ua, *i);
      if(strcmp(j.str, l.str) == 0) return 1;
   #else
      if(arr_get(s->ua, *i) == l) return 1;
   #endif
   return 0;
}

/* Remove l from the set (if it's in) */
void set_remove(set* s, arrtype l) {

   int i;
   /* If l is not in the set, return */
   if(set_contains(s,l) == 0) {
      return ;
   }
   /* If it is, check the position of l, and remove it */
   for(i=0; i<set_size(s); i++) {
      if(set_isin(s, l, &i)) {
         element_remove(s, i);
      }
   }
   s->sz--;
}

/* Remove the element which is in 'i' position from the set */
/* By moving the next elements of 'i' to the previous positions */
void element_remove(set* s, int i) {

   int j, size = set_size(s);
   for(j=i; j<size-1; j++) {
      arr_set(s->ua, j, arr_get(s->ua, j+1));
   }
}

/* Remove the first element, and return the removed one */
arrtype set_removeone(set* s) {

   arrtype removed = arr_get(s->ua, 0);
   if(s == NULL) {
      ON_ERROR("Can't remove an element from an empty set\n");
   }
   element_remove(s, 0);
   s->sz--;
   return removed;
}

/* Create a new set, containing all elements from s1 & s2 */
/* While combining two sets, remove all duplicate elements */
set* set_union(set* s1, set* s2) {

   set *merge;
   int i, s1size, s2size;

   merge = set_init();
   s1size = set_size(s1);
   s2size = set_size(s2);
   /* Insert all elements of s1 to the merged set*/
   for(i=0; i<s1size; i++) {
      set_insert(merge, arr_get(s1->ua, i));
   }
   /* Insert all elements of s2 to the merged set */
   for(i=0; i<s2size; i++) {
      set_insert(merge, arr_get(s2->ua, i));
   }
   return merge;
}

/* Create a new set, containing all elements in both s1 & s2 */
set* set_intersection(set* s1, set* s2) {

   set *common; int i;
   common = set_init();

   /* Check if the elements of s1 are equal to the ones of s2 */
   /* If there are, put them into the common set */
   for(i=0; i<set_size(s1); i++) {
      if(set_contains(s2, arr_get(s1->ua, i)) == 1) {
         set_insert(common, arr_get(s1->ua, i));
      }
   }
   return common;
}

/* Clears all space used, and sets pointer to NULL */
void set_free(set** s) {

   set *a;
   if(s == NULL) {
      return ;
   }
   a = *s;
   arr_free(&a->ua);
   free(a);
   *s = NULL;
}
