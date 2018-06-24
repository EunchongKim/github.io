#ifndef __BSTREE_H__
#define __BSTREE_H__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define ON_ERROR(STR) fprintf(stderr, STR); exit(EXIT_FAILURE)

/* A Dynamic (Unbalanced) Binary Search Tree, storing
   (void) pointers to the data. No duplicates can be stored */

struct bstnode {
   void* data;
   struct bstnode* left;
   struct bstnode* right;
};
typedef struct bstnode bstnode;

struct bst {
   bstnode* top;
   /* Data element size, in bytes */
   int elsz;
   /* Like for qsort(), pass a function that can compare two of the data items */
   int(*compare)(const void* a, const void* b);
   /* Pass a function that can return the data item as a string (for printing) */
   char*(*prntnode)(const void* a);
};
typedef struct bst bst;

bst* bst_init(int sz,
              int(*comp)(const void* a, const void* b),
              char*(*prnt)(const void* a)  );
/* Insert 1 item into the tree */
void bst_insert(bst* b, void* v);

/* Number of nodes in tree */
int bst_size(bst* b);

/* Longest path from root to any leaf */
int bst_maxdepth(bst* b);

/* Whether the data in v, is stored in the tree */
bool bst_isin(bst* b, void* v);

/* Bulk insert n items from an array v into an initialised tree */
void bst_insertarray(bst* b, void* v, int n);

/* Clear all memory associated with tree, & set pointer to NULL */
void bst_free(bst** p);

/***************************/
/* Advanced functionality  */
/***************************/

/* Return a string displaying the tree in a textual form (head(left)(right)) recursively */
char* bst_print(bst* b);

/* Fill an array with a copy of the sorted tree data */
void bst_getordered(bst* b, void* v);

void bst_threaded(bst *b, void* v);

/* Rebalance the tree, recursively using the median of the sorted keys */
bst* bst_rebalance(bst* b);

#endif
