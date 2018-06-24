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
void bst_threaded(bst *b, void *v);
void node_threaded(bst *b, bstnode *n, void *v, int *p);
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

void node_insert(bst *b, bstnode* n, void* v) {

   if(b->compare(n->data,v) > 0) {
      if(n->left==NULL) {
         n->left = node_init(b->elsz);
         memcpy(n->left->data, v, b->elsz);
         return;   
      }
      else
         node_insert(b, n->left, v);
   }
   else if(b->compare(n->data,v) < 0) {
      if(n->right==NULL) {
         n->right = node_init(b->elsz);
         memcpy(n->right->data, v, b->elsz);
         return;
      }
      else
         node_insert(b, n->right, v);
   }
   else{
      return;
   }
}

void bst_insert(bst* b, void* v) {

   if(b==NULL) {
      ON_ERROR("Can't insert a value to the invalid bst\n");
   }
   if(bst_isin(b, v)==true) {
      return ;
   }
   if(b->top==NULL) {
      b->top = node_init(b->elsz);
      memcpy(b->top->data, v, b->elsz);
   }
   node_insert(b, b->top, v);
   assert(b->top!=NULL);
}

int node_size(bst *b, bstnode *n) {

   if(n == NULL) {
      return 0;
   }
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

   if(b==NULL) {
      return 0;
   }
   return depth = node_maxdepth(b, b->top);
}

int node_maxdepth(bst* b, bstnode *n) {

   int leftdep, rightdep;

   if(n==NULL) {
      return 0;
   }
   else {
      leftdep = node_maxdepth(b, n->left);
      rightdep = node_maxdepth(b, n->right);
   }

   if(leftdep > rightdep) {
      return leftdep + increment;
   }
   else return rightdep + increment;
}

bool bst_isin(bst* b, void* v) {

   if(b==NULL) {
      ON_ERROR("Can't find a value in the invalid bst\n");
   }
   if(node_isin(b, b->top, v) == true) {
      return true;
   }
   else return false;
}

bool node_isin(bst* b, bstnode *n, void* v) {

   if(n==NULL) {
      return false;
   }
   if(b->compare(n->data, v) == 0) {
      return true;
   }
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
      free(s1);
      free(s2);
      return str;
   }
   s1 = node_print(b, n->left);
   s2 = node_print(b, n->right);

   n1 = strlen(s1);
   n2 = strlen(s2);
   n3 = strlen(b->prntnode(n->data));

   str = (char *)malloc(n1+n2+n3+extra);
   sprintf(str, "(%s%s%s)", b->prntnode(n->data), s1, s2);

   free(s1);
   free(s2);
   return str;
}

void bst_threaded(bst *b, void *v) {

   int i=0;
   if(b==NULL) {
      ON_ERROR("Can't get the bst\n");
   }
   if(v==NULL) {
      ON_ERROR("Please allocate the array\n");
   }
   node_threaded(b, b->top, v, &i);
}

void node_threaded(bst *b, bstnode *n, void *v, int *p) {

   bstnode *current, *predece;
   current = n;
   while(current != NULL) {
      /* If the current node doesn't have the left node, */
      if(current->left == NULL) {
         /* Store data to 'v' */
         memcpy((char *)v+(*p)*b->elsz, current->data, b->elsz);
         (*p)++;
         /* Change the current as the right node.
            If it is threaded, it means that going to the threaded node */
         current = current->right;
      }
      /* If it has the left node */
      else {
         /* Make the left node as predecessor, */
         predece = current->left;
         /* If the right node of current's left node is not threaded
            to the current node, and it is not NULL, */
         while(predece->right != current && predece->right != NULL) {
            /* While that condition, 
               change the predecessor to the right nodes continuously */
            predece = predece->right;
         }
         /* If the right node of predecessor is NULL, */
         if(predece->right == NULL) {
            /* Threaded the right child pointer to the current node */
            predece->right = current;
            /* Change the current to the left node of the current node */
            current = current->left;
         }
         else {
            /* If it is already visited the required nodes,
               remove the threaded line to prevent a loop,
               and store data to 'v' */
            predece->right = NULL;
            memcpy((char *)v+(*p)*b->elsz, current->data, b->elsz);
            (*p)++;
            current = current->right;
         }
      }
   }
}

bst* bst_rebalance(bst* b) {

   bst *newb = bst_init(b->elsz, b->compare, b->prntnode);
   int i=0, size = bst_size(b), start=0, end=size-1;
   void* num = (void *)calloc(size, b->elsz);
   if(b==NULL) {
      ON_ERROR("Can't get the bst\n");
   }
   node_threaded(b, b->top, num, &i);
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
