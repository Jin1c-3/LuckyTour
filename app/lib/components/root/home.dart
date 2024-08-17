import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:badges/badges.dart' as badges;
import 'package:lucky_tour/controllers/app.dart';
// import 'package:lucky_tour/controllers/user.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  AppController appController = Get.find();
  final List _destinationList = [
    {
      "name": "上海",
      "description": "探索上海的美丽",
    },
    {
      "name": "北京",
      "description": "探索北京的美丽",
    },
    {
      "name": "广州",
      "description": "探索广州的美丽",
    },
    {
      "name": "深圳",
      "description": "探索深圳的美丽",
    },
  ];
  final List _scenicList = [
    {
      "name": "上海迪士尼",
      "description": "探索上海迪士尼的美丽",
    },
    {
      "name": "故宫",
      "description": "探索故宫的美丽",
    },
    {
      "name": "广州塔",
      "description": "探索广州塔的美丽",
    },
    {
      "name": "深圳欢乐谷",
      "description": "探索深圳欢乐谷的美丽",
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("首页"),
        actions: [
          Obx(
            () => badges.Badge(
              position: badges.BadgePosition.topEnd(top: 10, end: 10),
              showBadge: appController.hasNotification.value,
              ignorePointer: false,
              onTap: () {},
              badgeAnimation: const badges.BadgeAnimation.rotation(
                animationDuration: Duration(seconds: 1),
                colorChangeAnimationDuration: Duration(seconds: 1),
                loopAnimation: false,
                curve: Curves.fastOutSlowIn,
                colorChangeAnimationCurve: Curves.easeInCubic,
              ),
              badgeStyle: badges.BadgeStyle(
                shape: badges.BadgeShape.square,
                badgeColor: Colors.red,
                padding: const EdgeInsets.all(5),
                borderRadius: BorderRadius.circular(15),
                elevation: 0,
              ),
              child: IconButton(
                icon: Icon(
                  Icons.notifications,
                  color: Colors.green[900],
                ),
                onPressed: () {
                  appController.hasNotification.value = false;
                  Get.toNamed("/notifications");
                },
              ),
            ),
          ),
        ],
      ),
      body: ListView(
        children: [
          Container(
            margin: const EdgeInsets.all(10),
            height: 200,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(10),
              image: const DecorationImage(
                image:
                    NetworkImage("https://picsum.photos/seed/picsum/800/500"),
                fit: BoxFit.cover,
              ),
            ),
          ),
          ListTile(
            title: const Text("2024年度分析报告",
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            subtitle: Text(
              "2024年最值得去的11个旅游城市",
              style: TextStyle(color: Colors.green[100]),
            ),
            trailing: ElevatedButton(
              onPressed: () {},
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.green,
              ),
              child: const Text(
                "查看",
                style: TextStyle(color: Colors.black),
              ),
            ),
            onTap: () => Get.toNamed("/strategy"),
          ),
          const Padding(
            padding: EdgeInsets.all(10),
            child: Text("热门城市",
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          SizedBox(
            height: 200,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: _destinationList.length,
              itemBuilder: (BuildContext context, int index) {
                return Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                      margin: const EdgeInsets.all(10),
                      width: 130,
                      height: 130,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        image: DecorationImage(
                          image: NetworkImage(
                              "https://picsum.photos/500/500?random=${index + 5}"),
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 10),
                      child: Text(
                        _destinationList[index]['name'],
                        style: const TextStyle(
                          fontSize: 16,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 10),
                      child: Text(
                        _destinationList[index]['description'],
                        style: TextStyle(color: Colors.green[100]),
                      ),
                    ),
                  ],
                );
              },
            ),
          ),
          const Padding(
            padding: EdgeInsets.all(10),
            child: Text("热门景点",
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          SizedBox(
            height: 200,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: _scenicList.length,
              itemBuilder: (BuildContext context, int index) {
                return Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                      margin: const EdgeInsets.all(10),
                      width: 130,
                      height: 130,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        image: DecorationImage(
                          image: NetworkImage(
                              "https://picsum.photos/500/500?random=$index"),
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 10),
                      child: Text(
                        _scenicList[index]['name'],
                        style: const TextStyle(
                          fontSize: 16,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 10),
                      child: Text(
                        _scenicList[index]['description'],
                        style: TextStyle(color: Colors.green[100]),
                      ),
                    ),
                  ],
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
