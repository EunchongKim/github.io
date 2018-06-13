CFLAGS = `sdl2-config --cflags` -O2 -Wall -Wextra -pedantic -ansi -Wfloat-equal
INCS = teletext.h
TARGET = tele_base
TARGET_EXT = tele_html
SOURCES =  $(TARGET).c teletext.c
SOURCES_EXT = $(TARGET_EXT).c teletext.c
LIBS =  `sdl2-config --libs` -lm
CC = gcc


all: $(TARGET) $(TARGET_EXT)

$(TARGET): $(SOURCES) $(INCS)
	$(CC) $(SOURCES) -o $(TARGET) $(CFLAGS) $(LIBS)
$(TARGET_EXT): $(SOURCES_EXT) $(INCS)
	$(CC) $(SOURCES_EXT) -o $(TARGET_EXT) $(CFLAGS) $(LIBS)

clean:
	rm -f $(TARGET) $(TARGET_EXT)

run: all
	./$(TARGET)
	./$(TARGET_EXT)

