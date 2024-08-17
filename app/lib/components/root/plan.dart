import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:lucky_tour/controllers/app.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:lucky_tour/controllers/user.dart';
import 'package:wave/config.dart';
import 'package:wave/wave.dart';
import '../../apis/plan_apis.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:badges/badges.dart' as badges;

class Plan extends StatefulWidget {
  const Plan({super.key});

  @override
  State<Plan> createState() => _PlanState();
}

class _PlanState extends State<Plan> {
  AppController appController = Get.find();
  UserController userController = Get.find();
  PlanController planController = Get.find();
  List _createdPlans = [];
  bool _isLoading = true;

  void _getPlanList() async {
    final result = await PlanApi().getPlanList({
      "uid": userController.userInfo['id'],
    });

    setState(() {
      _isLoading = false;
      if (result['code'] == 200) {
        _createdPlans.clear();
        _createdPlans = result['data'];
        for (var i = 0; i < _createdPlans.length; i++) {
          _createdPlans[i]['content'] = jsonDecode(_createdPlans[i]['content']);
        }
      }
    });
  }

  void _open(index) {
    if (GetStorage().read('dynamicPlanState') != true) {
      Get.dialog(
        AlertDialog(
          title: const Text('温馨提示'),
          content: const Text('需先同意打开"设置"中的"智能服务"才能开启旅行计划的动态服务，是否同意？'),
          actions: [
            TextButton(
              onPressed: () {
                Get.back();
              },
              child: const Text(
                '取消',
                style: TextStyle(color: Colors.red),
              ),
            ),
            TextButton(
              onPressed: () {
                Get.back();
                GetStorage().write('dynamicPlanState', true);
              },
              child: const Text(
                '确定',
                style: TextStyle(color: Colors.green),
              ),
            ),
          ],
        ),
      );
      return;
    }
    if (GetStorage().read('dynamicPlanId') != null) {
      Get.snackbar(
        '失败',
        '已有计划开启了动态旅行，请先关闭后再开启新的计划',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Colors.red,
        colorText: Colors.white,
        margin: const EdgeInsets.all(20),
      );
      return;
    }
    GetStorage().write('dynamicPlanId', _createdPlans[index]['pid']);
    GetStorage().write('dynamicPlan', _createdPlans[index]);
    Map gones = {};
    for (var key in _createdPlans[index]['content'].keys) {
      for (var item in _createdPlans[index]['content'][key]) {
        gones[item['name']] = false;
      }
    }
    GetStorage().write('dynamicPlanGones', gones);
    Get.back();
    setState(() {});
  }

  void _close() {
    GetStorage().remove('dynamicPlanId');
    GetStorage().remove('dynamicPlanGones');
    GetStorage().remove('dynamicPlan');
    Get.back();
    setState(() {});
  }

  void _deletePlan(index) async {
    final result = await PlanApi().deletePlan({
      "pid": _createdPlans[index]['pid'],
    });
    if (result['code'] == 200) {
      Get.back();
      _getPlanList();
    } else {
      Get.snackbar(
        '失败',
        '请先删除计划对应生成的攻略',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Colors.red,
        colorText: Colors.white,
        margin: const EdgeInsets.all(20),
      );
    }
  }

  @override
  void initState() {
    super.initState();
    _getPlanList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("计划"),
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
      body: Padding(
        padding: const EdgeInsets.fromLTRB(10, 0, 10, 10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Expanded(
                  flex: 1,
                  child: Container(
                    color: Colors.white,
                    padding: const EdgeInsets.all(10),
                    child: ElevatedButton(
                      onPressed: () => Get.toNamed("/plan_chat_create"),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.green,
                        fixedSize: const Size.fromHeight(60),
                        shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.all(
                            Radius.circular(10),
                          ),
                        ),
                      ),
                      child: const Text(
                        "自由模式",
                        style: TextStyle(color: Colors.white),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  flex: 1,
                  child: Container(
                    color: Colors.white,
                    padding: const EdgeInsets.all(10),
                    child: ElevatedButton(
                      onPressed: () => Get.toNamed("/destination"),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.green[50],
                        fixedSize: const Size.fromHeight(60),
                        shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.all(
                            Radius.circular(10),
                          ),
                        ),
                      ),
                      child: Text(
                        "标签模式",
                        style: TextStyle(color: Colors.green[900]),
                      ),
                    ),
                  ),
                ),
              ],
            ),
            Obx(
              () => Visibility(
                visible: planController.isGenerate.value,
                child: Container(
                  color: Colors.white,
                  height: 150,
                  width: double.infinity,
                  child: Card(
                      elevation: 12.0,
                      margin: const EdgeInsets.all(15),
                      clipBehavior: Clip.antiAlias,
                      shape: const RoundedRectangleBorder(
                          borderRadius:
                              BorderRadius.all(Radius.circular(16.0))),
                      child: Stack(
                        children: [
                          WaveWidget(
                            config: CustomConfig(
                              colors: [
                                Colors.white70,
                                Colors.white54,
                                Colors.white30,
                                Colors.white24,
                              ],
                              durations: [32000, 21000, 18000, 5000],
                              heightPercentages: [0.22, 0.23, 0.25, 0.28],
                            ),
                            backgroundColor: Colors.greenAccent[700],
                            size: const Size(double.infinity, double.infinity),
                            waveAmplitude: 0,
                          ),
                          Center(
                            child: Text(
                              "计划生成中",
                              style: TextStyle(
                                  fontSize: 20, color: Colors.green[100]),
                            ),
                          ),
                        ],
                      )),
                ),
              ),
            ),
            Container(
              color: Colors.white,
              width: double.infinity,
              padding: const EdgeInsets.only(left: 10, top: 10),
              child: const Text(
                "已创建的计划",
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
            ),
            Visibility(
              visible: _isLoading,
              child: const Expanded(
                child: Center(
                  child: SpinKitSpinningLines(
                    color: Colors.green,
                    size: 50.0,
                  ),
                ),
              ),
            ),
            Visibility(
              visible: !_isLoading && _createdPlans.isEmpty,
              child: Expanded(
                child: SizedBox(
                  width: double.infinity,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.assignment_add,
                        color: Colors.green[200],
                        size: 40,
                      ),
                      const SizedBox(
                        height: 10,
                      ),
                      Text(
                        "快来创建您的第一个计划吧！",
                        style: TextStyle(
                          fontSize: 16,
                          color: Colors.green[200],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            Visibility(
              visible: !_isLoading && _createdPlans.isNotEmpty,
              child: Expanded(
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: _createdPlans.length,
                  itemBuilder: (BuildContext context, int index) {
                    return badges.Badge(
                      position: badges.BadgePosition.topEnd(top: -2, end: 0),
                      showBadge: GetStorage().read('dynamicPlanId') ==
                          _createdPlans[index]['pid'],
                      ignorePointer: false,
                      onTap: () {},
                      badgeContent: const Icon(
                        Icons.play_arrow_rounded,
                        size: 20,
                        color: Colors.white,
                      ),
                      badgeAnimation: const badges.BadgeAnimation.scale(
                        animationDuration: Duration(seconds: 1),
                        colorChangeAnimationDuration: Duration(seconds: 1),
                        loopAnimation: false,
                        curve: Curves.fastOutSlowIn,
                        colorChangeAnimationCurve: Curves.easeInCubic,
                      ),
                      badgeStyle: badges.BadgeStyle(
                        shape: badges.BadgeShape.square,
                        badgeColor: Colors.blue,
                        padding: const EdgeInsets.all(5),
                        borderRadius: BorderRadius.circular(15),
                        elevation: 0,
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(10),
                        child: ListTile(
                          title: Text(_createdPlans[index]['title']),
                          subtitle: Text(_createdPlans[index]['city']),
                          leading: CircleAvatar(
                            backgroundImage: NetworkImage(
                              "https://picsum.photos/500/500?random=$index",
                            ),
                            radius: 25,
                          ),
                          trailing: IconButton(
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
                                child: Column(
                                  children: [
                                    ListTile(
                                      title: const Text('开启动态旅行'),
                                      leading:
                                          const Icon(Icons.play_arrow_rounded),
                                      onTap: () => _open(index),
                                    ),
                                    ListTile(
                                      title: const Text('关闭动态旅行'),
                                      leading: const Icon(Icons.close_rounded),
                                      onTap: _close,
                                    ),
                                    ListTile(
                                      title: const Text('删除'),
                                      leading: const Icon(Icons.delete),
                                      onTap: () => _deletePlan(index),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                            icon: const Icon(Icons.more_horiz_rounded),
                          ),
                          shape: RoundedRectangleBorder(
                            side: const BorderSide(
                              color: Colors.green,
                              width: 0.5,
                            ),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          onTap: () {
                            planController.selectedPlanInfo.value =
                                _createdPlans[index];
                            Get.toNamed("/plan_show");
                          },
                        ),
                      ),
                    );
                  },
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
