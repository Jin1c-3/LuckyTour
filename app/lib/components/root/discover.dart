import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/apis/strategy_apis.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:lucky_tour/controllers/user.dart';
import '../../controllers/app.dart';
import '../discover/search.dart';
import 'package:badges/badges.dart' as badges;

class Discover extends StatefulWidget {
  const Discover({super.key});

  @override
  State<Discover> createState() => _DiscoverState();
}

class _DiscoverState extends State<Discover> {
  PlanController planController = Get.find();
  UserController userController = Get.find();
  AppController appController = Get.find();

  void checkIsFollow(id) {
    final index =
        userController.followings.indexWhere((element) => element['id'] == id);
    userController.isFollow.value = index != -1;
  }

  void _checkIsCollect() async {
    final index = userController.collections.indexWhere(
      (element) => element['bid'] == planController.selectedStrategyItem['bid'],
    );
    userController.isCollect.value = index != -1;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("发现"),
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
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
            child: InkWell(
              onTap: () =>
                  Get.bottomSheet(const Search(), isScrollControlled: true),
              child: Container(
                decoration: BoxDecoration(
                  color: Colors.green[50],
                  borderRadius: BorderRadius.circular(5),
                ),
                child: Row(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(10),
                      child: Icon(
                        Icons.search,
                        color: Colors.green[900],
                      ),
                    ),
                    const Text("搜索"),
                  ],
                ),
              ),
            ),
          ),
          Obx(
            () => Expanded(
              child: ListView.builder(
                shrinkWrap: true,
                padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                itemCount: planController.strategyList.length,
                itemBuilder: (BuildContext context, int index) {
                  return ListTile(
                      title: Column(
                    children: [
                      Column(
                        children: [
                          Card(
                            child: InkWell(
                              onTap: () async {
                                planController.selectedStrategyItem.value =
                                    userController.collections[index];
                                _checkIsCollect();
                                await StrategyApi().addClickCount({
                                  "bid": userController.collections[index]
                                      ['bid'],
                                });
                                Get.toNamed("/map_points_details");
                              },
                              child: Container(
                                height: 150,
                                width: 340,
                                decoration: BoxDecoration(
                                  image: DecorationImage(
                                    image: NetworkImage(
                                      planController
                                                  .strategyList[index]
                                                      ['content'][0]['photos']
                                                  .length >
                                              0
                                          ? planController.strategyList[index]
                                              ['content'][0]['photos'][0]
                                          : "",
                                    ),
                                    fit: BoxFit.cover,
                                  ),
                                  borderRadius: const BorderRadius.only(
                                    topLeft: Radius.circular(10),
                                    topRight: Radius.circular(10),
                                  ),
                                ),
                              ),
                            ),
                          ),
                          ListTile(
                            leading: GestureDetector(
                              onTap: () {
                                checkIsFollow(
                                    planController.strategyList[index]['uid']);
                                userController.selectedPersonId.value =
                                    planController.strategyList[index]['uid'];
                                Get.toNamed("/person_details");
                              },
                              child: CircleAvatar(
                                backgroundImage: NetworkImage(
                                  planController.strategyList[index]['avatar'],
                                ),
                              ),
                            ),
                            title: Text(
                                planController.strategyList[index]['title']),
                          ),
                        ],
                      ),
                    ],
                  ));
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}
