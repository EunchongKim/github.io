CFLAGS = `sdl2-config --cflags` -O2 -Wall -Wextra -pedantic -ansi -Wfloat-equal
INCS = teletext.h
TARGET_TEST = tele_test
SOURCES_TEST = $(TARGET_TEST).c teletext.c
LIBS =  `sdl2-config --libs` -lm
CC = gcc


all: $(TARGET_TEST)

$(TARGET_TEST): $(SOURCES_TEST) $(INCS)
	$(CC) $(SOURCES_TEST) -o $(TARGET_TEST) $(CFLAGS) $(LIBS)

clean:
	rm -f $(TARGET_TEST)

run: all
	./$(TARGET_TEST)

