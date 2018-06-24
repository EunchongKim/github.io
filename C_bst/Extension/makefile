CFLAGS = -Wall -Wextra -Werror -Wfloat-equal -pedantic -ansi
INCS = bst.h

all: testbst spl testbst_d spl_d testthreaded testthreaded_d hash hash_d

testbst:  bst.c testbst.c $(INCS)
	$(CC) bst.c testbst.c -o testbst -O3 $(CFLAGS)

spl : spl.c bst.c  $(INCS)
	$(CC) bst.c spl.c -o spl -O3 $(CFLAGS)

hash : hash.c bst.c $(INCS)
	 $(CC) bst.c hash.c -o hash -O3 $(CFLAGS)

testthreaded:  bst_thr.c testthreaded.c $(INCS)
	$(CC) bst_thr.c testthreaded.c -o testthreaded -O3 $(CFLAGS)

testbst_d:  bst.c testbst.c $(INCS)
	$(CC) bst.c testbst.c -o testbst_d -g -O $(CFLAGS)

spl_d : spl.c bst.c  $(INCS)
	$(CC) bst.c spl.c -o spl_d -g -O $(CFLAGS)

hash_d : hash.c bst.c $(INCS)
	   $(CC) bst.c hash.c -o hash_d -g -O $(CFLAGS)

testthreaded_d:  bst_thr.c testthreaded.c $(INCS)
	$(CC) bst_thr.c testthreaded.c -o testthreaded_d -g -O $(CFLAGS)

run: all
	./testbst
	./spl eng_370k_shuffle.txt heart_darkness.txt
	./testthreaded
	./hash eng_370k_shuffle.txt heart_darkness.txt

memchk: testbst_d spl_d testthreaded_d hash_d
	valgrind --error-exitcode=1 --quiet --leak-check=full ./testbst_d
	valgrind --error-exitcode=1 --quiet --leak-check=full ./spl_d eng_370k_shuffle.txt heart_darkness.txt
	valgrind --error-exitcode=1 --quiet --leak-check=full ./testthreaded_d
	valgrind --error-exitcode=1 --quiet --leak-check=full ./hash_d eng_370k_shuffle.txt heart_darkness.txt

clean:
	rm -f testbst testbst_d spl spl_d testthreaded testthreaded_d hash hash_d

.PHONY: clean run memchk all
