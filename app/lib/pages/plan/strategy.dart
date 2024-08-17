import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:lucky_tour/controllers/user.dart';
import '../../apis/user_apis.dart';
import '../../apis/strategy_apis.dart';

class Strategy extends StatefulWidget {
  const Strategy({super.key});

  @override
  State<Strategy> createState() => _StrategyState();
}

class _StrategyState extends State<Strategy> {
  UserController userController = Get.find();
  PlanController planController = Get.find();
  bool _isLoading = true;
  List _blogList = [];

  void _getBlogList() async {
    final result = await UserApi().getUserBlogList({
      "uid": userController.userInfo["id"],
    });
    if (result["code"] == 200) {
      setState(() {
        _isLoading = false;
        _blogList = [];
        _blogList = result["data"];
        for (var i = 0; i < _blogList.length; i++) {
          _blogList[i]['content'] = jsonDecode(_blogList[i]['content']) as List;
        }
      });
    }
  }

  void _checkIsCollect() async {
    final index = userController.collections.indexWhere(
      (element) => element['bid'] == planController.selectedStrategyItem['bid'],
    );
    userController.isCollect.value = index != -1;
  }

  void _deleteBlog(index) async {
    final result = await StrategyApi().deleteStrategy({
      "bid": _blogList[index]['bid'],
    });
    if (result["code"] == 200) {
      Get.back();
      _getBlogList();
    }
  }

  @override
  void initState() {
    super.initState();
    _getBlogList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("攻略"),
      ),
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
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
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
                                      child: Column(
                                        children: [
                                          const ListTile(
                                            title: Text('编辑'),
                                            leading: Icon(Icons.edit),
                                          ),
                                          ListTile(
                                            title: const Text('删除'),
                                            leading: const Icon(Icons.delete),
                                            onTap: () => _deleteBlog(index),
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
        ],
      ),
    );
  }
}
