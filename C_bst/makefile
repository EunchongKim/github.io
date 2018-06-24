CFLAGS = -Wall -Wextra -Werror -Wfloat-equal -pedantic -ansi
INCS = bst.h

all: testbst spl testbst_d spl_d threaded threaded_d hashing hashing_d

testbst:  bst.c testbst.c $(INCS)
	$(CC) bst.c testbst.c -o testbst -O3 $(CFLAGS)

spl : spl.c bst.c  $(INCS)
	$(CC) bst.c spl.c -o spl -O3 $(CFLAGS)

hashing : hashing.c bst.c $(INCS)
	 $(CC) bst.c hashing.c -o hashing -03 $(CFLAGS)

threaded:  bst_thr.c threaded.c $(INCS)
	$(CC) bst_thr.c threaded.c -o threaded -O3 $(CFLAGS)

testbst_d:  bst.c testbst.c $(INCS)
	$(CC) bst.c testbst.c -o testbst_d -g -O $(CFLAGS)

spl_d : spl.c bst.c  $(INCS)
	$(CC) bst.c spl.c -o spl_d -g -O $(CFLAGS)

hashing_d : hashing.c bst.c $(INCS)
	   $(CC) bst.c hashing.c -o hashing_d -g -0 $(CFLAGS)

threaded_d:  bst_thr.c threaded.c $(INCS)
	$(CC) bst_thr.c threaded.c -o threaded_d -g -O $(CFLAGS)

run: all
	./testbst
	./spl eng_370k_shuffle.txt heart_darkness.txt
	./threaded
	./hashing

memchk: testbst_d spl_d threaded_d hashing_d
	valgrind --error-exitcode=1 --quiet --leak-check=full ./testbst_d
	valgrind --error-exitcode=1 --quiet --leak-check=full ./spl_d eng_370k_shuffle.txt heart_darkness.txt
	valgrind --error-exitcode=1 --quiet --leak-check=full ./threaded_d
	valgrind --error-exitcode=1 --quiet --leak-check=full ./hashing_d

clean:
	rm -f testbst testbst_d spl spl_d threaded threaded_d hashing hashing_d

.PHONY: clean run memchk all
