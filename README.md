# Lists of Key Projects

This repo is about the projects what I drive. There are three main sources of them: Course works, final thesis and previous working experience. The course works are generally based on Java and C, the final project called 'Fitness Video Recommendation' could be seen in the local-server website (which will be published with a domain soon) and  details of the previous project in the company cannot be revealed in the public area because of a confidential reason, but it could be checked in the official website (https://pico.me/home/) and the whole structure design paper what I made.

## Table of Contents
* [Fitness Video Recommendation](#fitness-video-recommendation)
   * [YouTube-8M dataset](#youtube-8m-dataset)
   * [Website for recommendations](#website-for-recommendations)
* [Java](#java)
   * [Pelican Social Life Maker](#pelican-social-life-maker)
   * [Alggagi](#Alggagi)
   * [Homemade Database](#homemade-database)
   * [Stag](#stag)
   * [Oxo](#oxo)
* [C](#c)
   * [HTML to Teletext](#html-to-teletext)
   * [Binary Search Tree](#binary-search-tree)
   * [Abstarct Data Type](#abtract-data-type)
   * [DLA](#dla)
* [Commercial Search Engine](#commercial-search-engine)

## Fitness Video Recommendation

"'Fitness that fits': Recommending Physical Exercise Videos Based on User Preferences" is the project realising recommendation lists of YouTube fitness videos to users by employing Recommender System (RS). RS algorithms are based on Hybrid RS, the combination of Content-based RS and Collaborative Filtering RS. This project is currently planning to submit to the Health RecSys Conference 2018 from the suggestion of the supervisor. ([Health RecSys 2018])

 ### YouTube-8M dataset
 
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

 ### Website for recommendations
 
This RS project is also built in the website. ([Fitness Video Recommendation], the connection is not stable due to the frequent changes) When a user enters, she/he could choose their preferences with 17 labels. After the preference choice, top 102 videos are shown by calculating the similarity between the user preference and video labels. If a specific video is clicked, the user can watch the video and related videos follow.

The system structure of this project is as follows:
<img src=https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/SystemStructure_FitnessRecSys.png width="600" height="300">

The overall process of the recommendation system and website is like:
<img src=https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/OverallProcess_FitnessRecSys.png>

The demo-playing of websites per page can be shown:
* [Index Page]
* [Main Page]
* [Video Page]


## Java

There are five products written by Java: Pelican Social Life Maker, Alggagi, Homemade Database, Stag and Oxo. These projects deploy Java and sometimes JavaFX. 'Pelican Social Life Maker' is a basically group project, however, I devoted the most effort as the team leader from deciding the game concept to developing the game with JavaFX. Other four are the individual products which all hit the highest result of the course.




[Health RecSys 2018]: https://recsys.acm.org/recsys18/healthrecsys/
[YouTube-8M]: https://research.google.com/youtube8m/
[Project Data]: https://raw.githubusercontent.com/EunchongKim/github.io/master/Fitness%20Video%20Recommendation/Metrics%202.csv
[Fitness Video Recommendation]: https://159.65.28.157:8080/
[Index Page] https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/report/indexpage.mov
[Main Page] https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/report/mainpage.mov
[Video Page] https://github.com/EunchongKim/github.io/blob/master/Fitness%20Video%20Recommendation/report/secondpage.mov
