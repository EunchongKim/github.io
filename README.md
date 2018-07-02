# Lists of Key Projects

This repo is about the projects what I drive. There are three main sources of them: Course works, final thesis and previous working experience. The course works are generally based on Java and C. The final project called 'Fitness Video Recommendation' could be seen in the published website. The details of the project in the previous company cannot be revealed in the public area because of a confidential reason, but it could be checked in the official website (https://pico.me/home/) and the whole structure design paper what I made at the end of this page.

## Table of Contents
* [Fitness Video Recommendation](#fitness-video-recommendation)
   * [YouTube-8M Dataset](#youtube-8m-dataset)
   * [Website for Recommendations](#website-for-recommendations)
* [Java](#java)
   * [Pelican Social Life Maker](#pelican-social-life-maker)
   * [Alggagi](#alggagi)
   * [Homemade Database](#homemade-database)
   * [Stag](#stag)
   * [Oxo](#oxo)
* [C](#c)
   * [HTML to Teletext](#html-to-teletext)
   * [Binary Search Tree](#binary-search-tree)
   * [Abstract Data Types](#abstract-data-types)
* [Commercial Search Engine](#commercial-search-engine)


## Fitness Video Recommendation

"'Fitness that fits': Recommending Physical Exercise Videos Based on User Preferences" is the project realising recommendation lists of YouTube fitness videos to users by employing Recommender System (RS). RS algorithms are based on Hybrid RS, the combination of Content-based RS and Collaborative Filtering RS. This project is currently planning to submit to the Health RecSys Conference 2018 from the suggestion of the supervisor. ([Health RecSys 2018])

### YouTube-8M Dataset
 
The data of this project exploits YouTube-8M dataset. ([YouTube-8M]) Among millions of labeled-video data of YouTube-8m, 17 labels focusing on fitness are selected and employed. The rawdata provided by TenserFlow is trained and validated by the basic logistics model to get the proper labels for each video. After this process, videos have multi-labels to calculate their similarity distance. This file is the processed video data ([Project Data]):

```
AbPGm9IzKek,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
CRd_ZW2tUf4,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0
DgDcVTJ8LvM,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Gu4Gxcwwuw4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0
J-G2UIPnt6s,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0
JVpsrTcL-bk,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1
KV4QjezmHBI,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0
```

### Website for Recommendations

This RS project is also built in the website. ([Fitness Video Recommendation], This website is based on the pair web technology project, and developed more to apply the real RS. The connection is not stable due to the frequent changes) When a user enters, she/he could choose their preferences with 17 labels. After the preference choice, top 102 videos are shown by calculating the similarity between the user preference and video labels. If a specific video is clicked, the user can watch the video and related videos follow.

The system structure of this project is as follows:
<img src=https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/SystemStructure_FitnessRecSys.png width="600" height="300">

The overall process of the recommendation system and website is like:
<img src=https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/OverallProcess_FitnessRecSys.png>

The demo-playing of websites per page can be shown:
* [Index Page]
* [Main Page]
* [Video Page]


## Java

There are five products written in Java: Pelican Social Life Maker, Alggagi, Homemade Database, Stag and Oxo. These projects deploy Java and sometimes JavaFX. 'Pelican Social Life Maker' is basically an well cooperative group project, additionally, I devoted the most effort as the team leader from deciding the game concept to developing the game with JavaFX. Other four are the individual products which all hit the highest result of the course.

### Pelican Social Life Maker

This game is to teach children about the danger and right use of Social Network by make them grow the pelican character in the virtual 'Pelibook' world. A user can learn 1. making a safe password 2. managing her/his social life well from this game. This game's general objective is to make the pelican character happy with keeping 'Popularity' and 'Wellness' high by choosing the right choice of the event pages. This game could be played with this executable jar file:

* [Pelican Social Life Maker]

The whole structure of this game is as follows:

<img src="https://github.com/EunchongKim/github.io/blob/master/PelicanSLM/Code%20Structure.png" width="350" height="250">

### Alggagi

This game is using the board and the stones of the traditional 'Go' game, however, exploiting a deceptively simple way of playing. By clicking a stone and dragging a line where a user wants, a stone will be bounced off according to the direction and the time. The objective of the game is to make all the opposite colour stones out of the board. In this game development, the collision check is invented from scratch with a unit testing.

The game looks like:

<img src="https://github.com/EunchongKim/github.io/blob/master/Java_Alggagi/Alggagi.png" width="800">

This game provides two modes of play, AI Play Mode and Self Play Mode.

```
AI Play Mode: Playing with AI. Black stones are player's ones as the first turn.
Self Play Mode: Playing both stones. All stones are controllable.
```
The executable file is as below:

* [Alggagi Game]

### Homemade Database

The homemade database system development literally means making the database structure and system from the very small unit only by employing Java. The objective of this work is to gain experience in an incremental development, including refactoring as well as unit testing, consequently, it starts with the basic unit, one line of data called 'Record' then develops to 'Table', 'Database' and 'Query', which makes the user interface in the command window. The operations of the database system are based on 'TreeMap' considering a time complexity, memory consumption, and natural order.

The functions and interface are borrowed from MySQL and MariaDB. The functions I developed are 'insert', 'select', 'delete', 'update', 'database constraints', 'catalogs', 'transactions', and 'journals'. All these functions are realised in the command window by analysing the real MySQL queries and parsing them.

This is the example of playing this system:
```
HomeDB [test]> INSERT INTO Animal (Id, Name, Kind, Owner) VALUES (1, Fido, dog, ab123);
Insert time (0.001 sec)
Query OK (0.001 sec)

HomeDB [test]> SELECT * FROM Animal WHERE Owner=ab123;
Select time (0.007 sec)
+----+----------+------+-------+
| Id | Name     | Kind | Owner |
+----+----------+------+-------+
| 1  | Fido     | dog  | ab123 |
| 3  | Garfield | cat  | ab123 |
+----+----------+------+-------+
Query OK (0.011 sec)
```

### Stag

Stag is the abbreviation of 'Simple Text Adventure Game'. Not just to develop this game, but to make the structure efficient, understanding about 'Objective Oriented' design of Java, especially for getting used to exploit each object as components. The general structural concept of this game is setting 'place', 'thing', and 'puzzle' to make a user wander around places, gather things, and solve puzzles to go to the next step. Naturally, each component has each class and the current status is shared by deploying 'Entity' class with inheritance.

The example of this game (Alice in Wonderland ver.) is as below:

```
You are Alice in the house in Wonderland.
You can see the weird room.
You can see the garden through the little door.
There is a box here.
> go weird room
You are in the weird room.
There is a drink me potion lying on the table.
There is a golden key lying on the table.
> take drink me potion
You pick up the drink me potion
> take golden key
You pick up the golden key
> go house
You are Alice in the house in Wonderland.
You can see the weird room.
You can see the garden through the little door.
There is a box here.
> open box
You open the box.
You get a bottle of water. Now you can have the potion.
Try open the little door.
...
```

### Oxo

Oxo, also called noughts and crosses or tic-tac-toe, is a simple and basic game. To make it with a object-oriented design, there are four classes 'Board' deciding moves, game turns, and wins, 'Display' printing a game status and receiving a user input, 'Symbol' which is a simple enumerated type class representing 'X' and 'O' symbols, and 'Oxo' controlling all the classes.

This is the AI & Graphic version of the game:

* [Oxo with AI & Graphic]


## C

### HTML to Teletext

Teletext is "a television information retrieval service created in the United Kingdom in the early 1970s". [Wikipedia about teletext] This project based on SDL of C is to firstly make the conversion program to change M7 file to teletext gif file by interpreting a 'control code', (like [Colour test card generator]) and extend it to automatically generate HTML file from teletext file. The later work demands dozens of 'parsing' to split HTML tags as needed, convert them to M7 code, and finally change it to teletext file. ([The main file of 'HTML to Teletext']) Furthermore, as a part of this work, thorough unit testing was done for the basic conversion program of teletext.

The example of HTML to teletext is as follows:

<img src="https://github.com/EunchongKim/github.io/blob/master/C_teletext/extension/HTMLtoTeletext.png" width="800">

### Binary Search Tree

This C work is to implement 'Binary search tree' from the beginning. Some functions to realise BST such as inserting a new input, checking a whole size and maximum depth of a tree, searching a node, and freeing all the memory are written in C. All the functions are examined with a unit testing. By employing this BST, more functions like a quick spell checking, tree printing, list ordering, rebalancing, threaded trees, and comparison to the performance of Hashing are executed in this work.

The simple display of BST is as below:
```
(M(K(I(D(F(G(H))))))(N))
```

### Abstract Data Types

To understand the concept of Abstract Data Types (ADTs), it makes 'set' including an underlying array ADT. The array could be an integer or string by a definition. This work implements a set from creating an empty set to copying, inserting, removing, and joining two sets together.


## Commercial Search Engine

The commercial search engine project I take a charge as a product manager in the previous company is the initial work which is the important attempt to change the company's identity from a traditional financial one to a digital-financial firm. The main goal of this project is developing a service providing abroad shopping sites to Korean users. This product's objective is not just publishing a new service, but also learning and internalising digital-firm properties which are different from the previous many web or app services of financial products.

It starts from setting up a small vertical search engine only for abroad commercial sites by employing Elastic Search and crawling data from those sites and develops NLP to make the search advanced and algorithms to recommend items and sites to users. Finally, this application is released this year, could be briefly checked in the website (https://pico.me/home/) and downloaded ([For Apple], [For Android], but not available in English version since its target user is Korean) 

* The detailed whole system structure of 'Commercial Search Engine' project will be updated

[Health RecSys 2018]: https://recsys.acm.org/recsys18/healthrecsys/
[YouTube-8M]: https://research.google.com/youtube8m/
[Project Data]: https://raw.githubusercontent.com/EunchongKim/github.io/master/Fitness%20Video%20Recommendation/Metrics%202.csv
[Fitness Video Recommendation]: http://159.65.28.157:8000/
[Index Page]: https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/report/indexpage.mov?raw=true
[Main Page]: https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/report/mainpage.mov?raw=true
[Video Page]: https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/report/secondpage.mov?raw=true
[Pelican Social Life Maker]: https://github.com/EunchongKim/github.io/blob/master/PelicanSLM/Pelican%20Social%20Life%20Maker.jar?raw=true
[Alggagi Game]: https://github.com/EunchongKim/github.io/blob/master/Java_Alggagi/Java_Alggagi.jar?raw=true
[Oxo with AI & Graphic]: https://github.com/EunchongKim/github.io/blob/master/Java_Oxo/OXO_EXTENSION/out/artifacts/OXO_EXTENSION_jar/OXO_EXTENSION.jar?raw=true
[Wikipedia about teletext]: https://en.wikipedia.org/wiki/Teletext
[Colour test card generator]: http://www.bbcbasic.co.uk/tccgen/manual/tcgen2.html
[The main file of 'HTML to Teletext']: https://github.com/EunchongKim/github.io/blob/master/C_teletext/extension/tele_html.c
[For Apple]: https://itunes.apple.com/kr/app/id1333452558?mt=8
[For Android]: https://play.google.com/store/apps/details?id=com.hyundaicard.cse
