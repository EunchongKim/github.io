#include <stdio.h>
#include <stdlib.h>
#include "neillncurses.h"
#define M 50
#define MAX 250

int initial(int *row, int *col);
void movecell(char a[M][M], int row, int col);
int checkadj(char a[M][M], int row, int col);
int randnum(int min, int max);
void printarray(char a[M][M]);


int main(void) {

    int row, col;
    char p[M][M];

    NCURS_Simplewin sw;

    /* Fill the array */
    for (row = 0; row < M; row++) {
        for (col = 0; col < M; col++) {
            p[row][col] = ' ';
        }
    }

    /* Set the middle one */
    p[M/2][M/2] = '#';

    Neill_NCURS_Init(&sw);
    /* Fill the blank with black */
    Neill_NCURS_CharStyle(&sw, " ", COLOR_BLACK, COLOR_BLACK, A_NORMAL);
    /* Fill # with red */
    Neill_NCURS_CharStyle(&sw, "#", COLOR_RED, COLOR_RED, A_NORMAL);

    /* Unless there is an event to exit, do the functions with a little delay */
    do {
        Neill_NCURS_PrintArray(&p[0][0], M, M, &sw);
        /* Select the initial, move it until meeting the adjacent occupied one,
           and occupy it */
        initial(&row, &col);
        movecell(p, row, col);

        Neill_NCURS_Delay(50.0);
        Neill_NCURS_Events(&sw);
    } while (!sw.finished);

    atexit(Neill_NCURS_Done);
    exit(EXIT_SUCCESS);

    return 0;
}

/* Select the start cell from the perimeters */
int initial(int *row, int *col) {

    int start_r, start_c, cnt;

    start_r = randnum(0, M-1);
    start_c = randnum(0, M-1);

    do {
        cnt = 0;

        /* Set the random point as the start cell if it is on the borders */
        if (start_r == 0 || start_c == 0 ||
            start_r == M-1 || start_c == M-1) {
            *row = start_r;
            *col = start_c;
            cnt++;
        }

        /* If not, generate it again */
        else
            start_r = randnum(0, M-1);
            start_c = randnum(0, M-1);
    } while (cnt != 1);

    return 0;
}

/* Move the cell until it meets an occupied adjacent one */
void movecell(char a[M][M], int row, int col) {

    int cnt, direc;
    enum select {up, right, down, left};
    
    do {
        cnt = 0;

        /* Check if the adjacent four cells are occupied and fill the current cell if so.
           If the current cell is on the border, moving is allowed to other 3 directions,
           but not allowed to outside the border */
        if (checkadj(a, row, col)) {
            a[row][col] = '#';
            cnt++;
        }

        else {

            /* Randomly decide a direction among 4 -- left, right, up and down */
            direc = randnum(0, 3);
            
            /* If direction is up, move up */
            if (direc == up) {
                row = row - 1;
                /* If it meets the border, return it to the bottom */
                if(row < 0) {
                    row = M-1;
                }
            }
            
            /* If direction is right, move right */
            else if (direc == right) {
                col = col + 1;
                /* If it meets the border, return it to the left */
                if(col == M) {
                    col = 0;
                }
            }
            
            /* If direction is down, move down */
            else if (direc == down) {
                row = row + 1;
                /* If it meets the border, return it to the top */
                if(row == M) {
                    row = 0;
                }
            }
            
            /* If direction is left, move left */
            else if (direc == left) {
                col = col - 1;
                /* If it meets the border, return it to the right */
                if(col < 0) {
                    col = M-1;
                }
            }
        }
    } while (cnt != 1);
}

int checkadj(char a[M][M], int row, int col) {

    /* Check if the above cell is occupied */
    if (a[row+1 == M ? row : row+1][col] == '#') {
        return 1;
    }
    /* Check if the below cell is occupied */
    else if (a[row-1 < 0 ? row : row-1][col] == '#') {
        return 1;
    }
    /* Check if the right cell is occupied */
    else if (a[row][col+1 == M ? col : col+1] == '#') {
        return 1;
    }
    /* Check if the left cell is occupied */
    else if (a[row][col-1 < 0 ? col : col-1] == '#') {
        return 1;
    }

    return 0;
}

/* Generate random number between minimum and maximum numbers */
int randnum(int min, int max) {
    return min + (rand() % (max + 1 - min));
}

void printarray(char a[M][M]) {

    int i, j;

    for (i = 0; i < M; i++) {
        for (j = 0; j < M; j++) {
            printf("%c", a[i][j]);
        }
        printf("\n");
    }
}
