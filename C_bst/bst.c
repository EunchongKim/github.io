#include "bst.h"
#include <assert.h>
#define increment 1
#define extra 3

bstnode* node_init(int sz);
void node_insert(bst* b, bstnode* n, void* v);
int node_size(bst *b, bstnode *n);
int node_maxdepth(bst* b, bstnode *n);
bool node_isin(bst* b, bstnode *n, void* v);
void for_free(bstnode* n);
char* node_print(bst* b, bstnode* n);
void node_getordered(bst *b, bstnode* n, void* v, int* p);
bstnode* node_rebalance(void *num, int start, int end, int sz);


bst* bst_init(int sz,
              int(*comp)(const void* a, const void* b),
              char*(*prnt)(const void* a)   ) {
   bst* b;
   b = (bst *)calloc(1, sizeof(bst));
   b->elsz = sz;
   b->top = NULL;
   b->compare = comp;
   b->prntnode = prnt;
   return b;
}

bstnode* node_init(int sz) {

   bstnode* n = (bstnode *)calloc(1, sizeof(bstnode));
   n->data = (void *)calloc(1, sz);
   assert(n->left == NULL);
   return n;
}

/* Insert 1 item into the node */
void node_insert(bst *b, bstnode* n, void* v) {

   /* If the item is smaller than the current node */
   if(b->compare(n->data,v) > 0) {
      /* Check the left node is empty */
      if(n->left==NULL) {
         n->left = node_init(b->elsz);
         memcpy(n->left->data, v, b->elsz);
         return;   
      }
      /* If not, find the left nodes recursively */
      else
         node_insert(b, n->left, v);
   }
   /* If the item is larger than the current node */
   else if(b->compare(n->data,v) < 0) {
      /* Check the right node is empty */
      if(n->right==NULL) {
         n->right = node_init(b->elsz);
         memcpy(n->right->data, v, b->elsz);
         return;
      }
      /* If not, find the right nodes recursively */
      else
         node_insert(b, n->right, v);
   }
   else{return;}
}

void bst_insert(bst* b, void* v) {

   if(b==NULL) {
      ON_ERROR("Can't insert a value to the invalid bst\n");
   }
   /* If the item is already in the tree, return */
   if(bst_isin(b, v)==true) {
      return ;
   }
   /* Fill the top node */
   if(b->top==NULL) {
      b->top = node_init(b->elsz);
      memcpy(b->top->data, v, b->elsz);
   }
   assert(b->top!=NULL);
   node_insert(b, b->top, v);
}

int node_size(bst *b, bstnode *n) {

   if(n == NULL) {return 0;}
   /* Find the left and the right nodes recursively
      by incrementing 1 in each steps */ 
   else {
      return (increment + node_size(b, n->left) +
              node_size(b, n->right)    );
   }
}

int bst_size(bst* b) {
   return node_size(b, b->top);
}

int bst_maxdepth(bst* b) {
   
   int depth;
   if(b==NULL) {return 0;}
   return depth = node_maxdepth(b, b->top);
}

int node_maxdepth(bst* b, bstnode *n) {

   int leftdep, rightdep;
   if(n==NULL) {return 0;}
   else {
      leftdep = node_maxdepth(b, n->left);
      rightdep = node_maxdepth(b, n->right);
   }
   /* If the left depth is larger than the right depth,
      increment it by 1 */
   if(leftdep > rightdep) {
      return leftdep + increment;
   }
   /* If the right depth is larger than the left one,
      increment it by 1 */
   else return rightdep + increment;
}

bool bst_isin(bst* b, void* v) {

   if(b==NULL) {
      ON_ERROR("Can't find a value in the invalid bst\n");
   }
   if(node_isin(b, b->top, v) == true) {return true;}
   else return false;
}

bool node_isin(bst* b, bstnode *n, void* v) {

   if(n==NULL) {return false;}
   if(b->compare(n->data, v) == 0) {return true;}
   if(b->compare(n->data, v) > 0) {
      return node_isin(b, n->left, v);
   }
   else {
      return node_isin(b, n->right, v);
   }
}

void bst_insertarray(bst* b, void* v, int n) {

   int i;
   for(i=0;i<n;i++) {
      bst_insert(b, (char *)v+(i*b->elsz));
   } 
}

char* bst_print(bst *b) {

   char *str;
   if (b==NULL) {
      ON_ERROR("Can't print out the invalid bst\n");
   }
   str = node_print(b, b->top);
   return str;
}

char* node_print(bst* b, bstnode* n) {

   char* s1=NULL, *s2=NULL, *str=NULL;
   int n1, n2, n3;

   if(n==NULL) {
      str = (char*)calloc(1, sizeof(char));
      return str;
   }
   /* Search the left and the right nodes recursively */
   s1 = node_print(b, n->left);
   s2 = node_print(b, n->right);

   /* Find the legnths of each nodes */
   n1 = strlen(s1);
   n2 = strlen(s2);
   n3 = strlen(b->prntnode(n->data));

   /* Put them to the string in each time,
      and it needs extra spaces for brackets and '\0' */
   str = (char *)malloc(n1+n2+n3+extra);
   sprintf(str, "(%s%s%s)", b->prntnode(n->data), s1, s2);

   free(s1);
   free(s2);
   return str;
}

void bst_getordered(bst* b, void* v) {
   
   int i=0;
   if(b==NULL) {
      ON_ERROR("Can't get the bst\n");
   }
   if(v==NULL) {
      ON_ERROR("Please allocate the array\n");
   }
   node_getordered(b, b->top, v, &i);
}

/* Using In-order Traversal, make the tree get ordered */
void node_getordered(bst *b, bstnode* n, void* v, int* p) {

   if(n==NULL) {
      return ;
   }
   node_getordered(b, n->left, v, p);
   memcpy((char *)v+(*p)*b->elsz, n->data, b->elsz);
   (*p)++;
   node_getordered(b, n->right, v, p);
}

bst* bst_rebalance(bst* b) {

   bst *newb = bst_init(b->elsz, b->compare, b->prntnode);
   int i=0, size = bst_size(b), start=0, end=size-1;
   void* num = (void *)calloc(size, b->elsz);
   if(b==NULL) {
      ON_ERROR("Can't get the bst\n");
   }
   /* Firstly sotring the data from the tree */
   node_getordered(b, b->top, num, &i);
   /* Pass the data to make a new tree in balance */
   newb->top = node_rebalance(num, start, end, b->elsz);
   free(num);
   return newb;
}

bstnode* node_rebalance(void *num, int start, int end, int sz) {

   bstnode* n = NULL;
   if(start > end) {
      return n;
   }
   n = (bstnode *)calloc(1, sizeof(bstnode));
   n->data = (void *)calloc(1, sz);
   memcpy(n->data, (char*)num+((start+end)/2*sz), sz);
   n->left = node_rebalance(num, start, (start+end)/2-1, sz);
   n->right = node_rebalance(num, (start+end)/2+1, end, sz);
   
   return n;
}

void for_free(bstnode* n) {

   if(n==NULL) {
      return ;
   }
   for_free(n->left);
   for_free(n->right);
   free(n->data);
   free(n);
}

void bst_free(bst** p) {

   bst* a;
   if(p==NULL) {
      return ;
   }
   a = *p;
   for_free(a->top);
   free(a);
   *p = NULL;
}
