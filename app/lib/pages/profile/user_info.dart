import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/apis/strategy_apis.dart';
import 'package:lucky_tour/apis/user_apis.dart';
import 'package:lucky_tour/controllers/plan.dart';
import '../../controllers/user.dart';
import 'package:draggable_home/draggable_home.dart';

class UserInfo extends StatefulWidget {
  const UserInfo({super.key});

  @override
  State<UserInfo> createState() => _UserInfoState();
}

class _UserInfoState extends State<UserInfo>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;
  PlanController planController = Get.find();
  UserController userController = Get.find();
  List followedList = [];
  int followingCount = 0;
  int followerCount = 0;
  int collectCount = 0;
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 3, vsync: this);
    _getInfo();
  }

  void _getInfo() async {
    final followeds = await UserApi().getFollowers();
    final followingcount = await UserApi().getFollowingCount({
      "uid": userController.userInfo["id"],
    });
    final followercount = await UserApi().getFollowerCount({
      "uid": userController.userInfo["id"],
    });
    setState(() {
      for (var i = 0; i < userController.collections.length; i++) {
        collectCount += userController.collections[i]['favoriteCount'] as int;
      }
      followedList = followeds['data'];
      followingCount = followingcount['data'];
      followerCount = followercount['data'];
      _isLoading = false;
    });
  }

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
    return Stack(
      children: [
        Visibility(
          visible: _isLoading,
          child: const Scaffold(
            body: Center(
              child: SpinKitSpinningLines(
                color: Colors.green,
                size: 50.0,
              ),
            ),
          ),
        ),
        Visibility(
          visible: !_isLoading,
          child: DraggableHome(
            title: const Text(''),
            headerExpandedHeight: 0.45,
            headerWidget: Obx(
              () => Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      IconButton(
                        onPressed: () => Get.toNamed("/edit_user_info"),
                        icon: const Icon(Icons.edit),
                      ),
                    ],
                  ),
                  CircleAvatar(
                      radius: 50,
                      backgroundImage: userController.userInfo['avatar'] != null
                          ? NetworkImage(userController.userInfo['avatar'])
                          : null),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(0, 10, 0, 5),
                    child: Text(
                      userController.userInfo["nickname"] ?? "",
                      style: const TextStyle(fontSize: 20),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
                    child: Text(
                      userController.userInfo["email"] ?? "",
                      style: const TextStyle(fontSize: 15),
                    ),
                  ),
                  GridView.count(
                    crossAxisCount: 3,
                    shrinkWrap: true,
                    padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
                    crossAxisSpacing: 5,
                    childAspectRatio: 1.5,
                    children: [
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('关注'),
                            Text(
                              "$followingCount",
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('粉丝'),
                            Text(
                              "$followerCount",
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('获赞'),
                            Text(
                              "$collectCount",
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            expandedBody: Obx(
              () => Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircleAvatar(
                      radius: 50,
                      backgroundImage: userController.userInfo['avatar'] != null
                          ? NetworkImage(userController.userInfo['avatar'])
                          : null),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(0, 10, 0, 5),
                    child: Text(
                      userController.userInfo["nickname"] ?? "",
                      style: const TextStyle(fontSize: 20),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(0, 5, 0, 20),
                    child: Text(
                      userController.userInfo["email"] ?? "",
                      style: const TextStyle(fontSize: 15),
                    ),
                  ),
                  GridView.count(
                    crossAxisCount: 2,
                    shrinkWrap: true,
                    padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
                    crossAxisSpacing: 10,
                    mainAxisSpacing: 10,
                    childAspectRatio: 2.2,
                    children: [
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('性别'),
                            Text(
                              userController.userInfo["sex"] ?? "",
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('生日'),
                            Text(
                              userController.userInfo["birthday"] ?? "",
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('VIP'),
                            Text(
                              userController.userInfo["vip"].toString(),
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('等级'),
                            Text(
                              userController.userInfo["level"].toString(),
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                  GridView.count(
                    crossAxisCount: 3,
                    shrinkWrap: true,
                    padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
                    crossAxisSpacing: 10,
                    mainAxisSpacing: 10,
                    childAspectRatio: 1.5,
                    children: [
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('关注'),
                            Text(
                              "$followingCount",
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('粉丝'),
                            Text(
                              '$followerCount',
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.green[50],
                          borderRadius: BorderRadius.circular(5),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text('获赞'),
                            Text(
                              "$collectCount",
                              style: const TextStyle(
                                fontWeight: FontWeight.w500,
                                fontSize: 25,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            body: [
              TabBar(
                controller: _tabController,
                isScrollable: true,
                tabAlignment: TabAlignment.start,
                indicatorColor: Colors.green,
                labelColor: Colors.green,
                tabs: const [
                  Tab(
                    text: '关注',
                  ),
                  Tab(
                    text: '粉丝',
                  ),
                  Tab(
                    text: '收藏',
                  ),
                ],
              ),
              SizedBox(
                height: MediaQuery.of(context).size.height,
                child: TabBarView(
                  controller: _tabController,
                  children: [
                    ListView.builder(
                      padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
                      itemCount: userController.followings.length,
                      itemBuilder: (BuildContext context, int index) {
                        return ListTile(
                          leading: CircleAvatar(
                            backgroundImage: NetworkImage(
                                userController.followings[index]['avatar'] ??
                                    ""),
                          ),
                          title: Text(userController.followings[index]
                                  ['nickname'] ??
                              ""),
                          onTap: () {
                            checkIsFollow(
                                userController.followings[index]['id']);
                            userController.selectedPersonId.value =
                                userController.followings[index]['id'];
                            Get.toNamed("/person_details");
                          },
                        );
                      },
                    ),
                    ListView.builder(
                      padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
                      itemCount: followedList.length,
                      itemBuilder: (BuildContext context, int index) {
                        return ListTile(
                          leading: CircleAvatar(
                            backgroundImage: NetworkImage(
                                followedList[index]['avatar'] ?? ""),
                          ),
                          title: Text(followedList[index]['nickname'] ?? ""),
                          onTap: () {
                            checkIsFollow(followedList[index]['id']);
                            userController.selectedPersonId.value =
                                followedList[index]['id'];
                            Get.toNamed("/person_details");
                          },
                        );
                      },
                    ),
                    ListView.builder(
                      shrinkWrap: true,
                      padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
                      itemCount: userController.collections.length,
                      itemBuilder: (BuildContext context, int index) {
                        return ListTile(
                          title: Column(
                            children: [
                              Column(
                                children: [
                                  Card(
                                    child: InkWell(
                                      onTap: () async {
                                        planController
                                                .selectedStrategyItem.value =
                                            userController.collections[index];
                                        _checkIsCollect();
                                        await StrategyApi().addClickCount({
                                          "bid": userController
                                              .collections[index]['bid'],
                                        });
                                        Get.toNamed("/map_points_details");
                                      },
                                      child: Container(
                                        height: 150,
                                        decoration: BoxDecoration(
                                          image: DecorationImage(
                                            image: NetworkImage(userController
                                                        .collections[index]
                                                            ['content'][0]
                                                            ['photos']
                                                        .length >
                                                    0
                                                ? userController
                                                        .collections[index]
                                                    ['content'][0]['photos'][0]
                                                : ""),
                                            fit: BoxFit.cover,
                                          ),
                                          borderRadius:
                                              BorderRadius.circular(10),
                                        ),
                                      ),
                                    ),
                                  ),
                                  ListTile(
                                    onTap: () {
                                      checkIsFollow(followedList[index]['id']);
                                      userController.selectedPersonId.value =
                                          userController.collections[index]
                                              ['uid'];
                                      Get.toNamed("/person_details");
                                    },
                                    leading: CircleAvatar(
                                      backgroundImage: NetworkImage(
                                          userController.collections[index]
                                                  ['avatar'] ??
                                              ""),
                                    ),
                                    title: Text(userController
                                            .collections[index]['title'] ??
                                        ""),
                                  ),
                                ],
                              ),
                            ],
                          ),
                        );
                      },
                    ),
                  ],
                ),
              ),
            ],
            fullyStretchable: true,
          ),
        ),
      ],
    );
  }
}
