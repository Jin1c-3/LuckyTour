import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/apis/strategy_apis.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:lucky_tour/controllers/user.dart';
import '../../apis/user_apis.dart';

class PersonDetails extends StatefulWidget {
  const PersonDetails({super.key});

  @override
  State<PersonDetails> createState() => _PersonDetailsState();
}

class _PersonDetailsState extends State<PersonDetails> {
  PlanController planController = Get.find();
  UserController userController = Get.find();
  Map selectedUserInfo = {};
  int followingCount = 0;
  int followerCount = 0;
  bool _isLoading = true;
  List _blogList = [];

  void _follow(id) async {
    final result = await UserApi().followOrUnfollow({
      "followedUid": id,
    });
    if (result['code'] == 200) {
      userController.isFollow.value = !userController.isFollow.value;
      final follows = await UserApi().getFollowings();
      userController.followings.value = List.from(follows['data']);
    }
  }

  void _checkIsCollect() async {
    final index = userController.collections.indexWhere(
      (element) => element['bid'] == planController.selectedStrategyItem['bid'],
    );
    userController.isCollect.value = index != -1;
  }

  void _getInfo() async {
    final userInfo = await UserApi().getOtherUserInfo({
      "uid": userController.selectedPersonId.value,
    });
    final followings = await UserApi().getFollowingCount({
      "uid": userController.selectedPersonId.value,
    });
    final followers = await UserApi().getFollowerCount({
      "uid": userController.selectedPersonId.value,
    });
    final result = await UserApi().getUserBlogList({
      "uid": userController.userInfo["id"],
    });
    setState(() {
      selectedUserInfo = Map.from(userInfo['data']);
      followingCount = followings['data'];
      followerCount = followers['data'];
      _blogList = [];
      _blogList = result["data"];
      for (var i = 0; i < _blogList.length; i++) {
        _blogList[i]['content'] = jsonDecode(_blogList[i]['content']) as List;
      }
      _isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    _getInfo();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Stack(
        children: [
          Visibility(
            visible: _isLoading,
            child: const Center(
              child: SpinKitSpinningLines(
                color: Colors.green,
                size: 50.0,
              ),
            ),
          ),
          Visibility(
            visible: !_isLoading,
            child: Container(
              width: double.infinity,
              height: double.infinity,
              padding: const EdgeInsets.only(
                left: 20,
                right: 20,
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  CircleAvatar(
                    radius: 50,
                    backgroundImage:
                        NetworkImage(selectedUserInfo["avatar"] ?? ""),
                  ),
                  Text(selectedUserInfo["nickname"] ?? "",
                      style: const TextStyle(
                          fontSize: 25, fontWeight: FontWeight.bold)),
                  Text(selectedUserInfo['email'] ?? "",
                      style: const TextStyle(fontSize: 15, color: Colors.grey)),
                  Row(
                    children: [
                      Text("$followingCount",
                          style: const TextStyle(fontSize: 20)),
                      const Text("关注",
                          style: TextStyle(fontSize: 15, color: Colors.grey)),
                      const SizedBox(width: 10),
                      Text("$followerCount",
                          style: const TextStyle(fontSize: 20)),
                      const Text("粉丝",
                          style: TextStyle(fontSize: 15, color: Colors.grey)),
                    ],
                  ),
                  Obx(
                    () => Visibility(
                      visible: !userController.isFollow.value,
                      child: Padding(
                        padding: const EdgeInsets.all(5),
                        child: ElevatedButton(
                          onPressed: () =>
                              _follow(userController.selectedPersonId),
                          style: ElevatedButton.styleFrom(
                            fixedSize: const Size(350, 30),
                            shape: const RoundedRectangleBorder(
                              borderRadius: BorderRadius.all(
                                Radius.circular(25),
                              ),
                            ),
                          ),
                          child: const Text("关注"),
                        ),
                      ),
                    ),
                  ),
                  Obx(
                    () => Visibility(
                      visible: userController.isFollow.value,
                      child: Padding(
                        padding: const EdgeInsets.all(5),
                        child: ElevatedButton(
                          onPressed: () =>
                              _follow(userController.selectedPersonId),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.green,
                            fixedSize: const Size(350, 30),
                            shape: const RoundedRectangleBorder(
                              borderRadius: BorderRadius.all(
                                Radius.circular(25),
                              ),
                            ),
                          ),
                          child: const Text("取消关注",
                              style: TextStyle(color: Colors.white)),
                        ),
                      ),
                    ),
                  ),
                  const Padding(
                    padding: EdgeInsets.only(top: 10, bottom: 10),
                    child: Text(
                      "攻略",
                      style:
                          TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                      shrinkWrap: true,
                      padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
                      itemCount: _blogList.length,
                      itemBuilder: (BuildContext context, int index) {
                        return Padding(
                          padding: const EdgeInsets.all(10),
                          child: InkWell(
                            borderRadius: BorderRadius.circular(10),
                            child: Column(
                              children: [
                                Container(
                                  height: 150,
                                  decoration: BoxDecoration(
                                    image: DecorationImage(
                                      image: NetworkImage(
                                        _blogList[index]['content'][0]['photos']
                                                    .length >
                                                0
                                            ? _blogList[index]['content'][0]
                                                ['photos'][0]
                                            : "",
                                      ),
                                      fit: BoxFit.cover,
                                    ),
                                    borderRadius: BorderRadius.circular(10),
                                  ),
                                ),
                                ListTile(
                                  title: Text(_blogList[index]['title'] ?? ""),
                                  trailing: IconButton(
                                    icon: const Icon(
                                      Icons.more_vert,
                                      color: Colors.grey,
                                    ),
                                    onPressed: () => Get.bottomSheet(
                                      Container(
                                        height: 200,
                                        decoration: const BoxDecoration(
                                          color: Colors.white,
                                          borderRadius: BorderRadius.only(
                                            topLeft: Radius.circular(15),
                                            topRight: Radius.circular(15),
                                          ),
                                        ),
                                        child: const Column(
                                          children: [
                                            ListTile(
                                              title: Text('编辑'),
                                              leading: Icon(Icons.edit),
                                            ),
                                            ListTile(
                                              title: Text('删除'),
                                              leading: Icon(Icons.delete),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                            onTap: () async {
                              planController.selectedStrategyItem.value =
                                  _blogList[index];
                              _checkIsCollect();
                              await StrategyApi().addClickCount({
                                "bid": _blogList[index]['bid'],
                              });
                              Get.toNamed("/map_points_details");
                            },
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
