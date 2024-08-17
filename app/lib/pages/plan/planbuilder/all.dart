import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:lucky_tour/controllers/user.dart';
import 'package:choice/choice.dart';
import '../../../apis/plan_apis.dart';

class All extends StatefulWidget {
  const All({super.key});

  @override
  State<All> createState() => _AllState();
}

class _AllState extends State<All> {
  PlanController planController = Get.find();
  UserController userController = Get.find();
  final TextEditingController _titleController = TextEditingController();

  void _send() async {
    Get.until((route) => Get.currentRoute == "/home");
    planController.isGenerate.value = true;
    final result = await PlanApi().generatePlan({
      "uid": userController.userInfo['id'],
      "prompt": planController.temp,
      "title": _titleController.text == ""
          ? "${userController.userInfo["nickname"]}的旅行计划"
          : _titleController.text,
      "tags": planController.temp['tag'],
      "city": planController.temp['destination'],
    });
    planController.temp.value = {};
    planController.isGenerate.value = false;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Stack(
        children: [
          ListView(
            padding: const EdgeInsets.fromLTRB(15, 10, 15, 10),
            children: [
              const Text(
                '预览',
                style: TextStyle(fontSize: 30),
              ),
              Text(
                '确认创建信息是否正确',
                style: TextStyle(fontSize: 15, color: Colors.green[200]),
              ),
              const Padding(
                padding: EdgeInsets.all(10),
                child: Text("标题", style: TextStyle(fontSize: 20)),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 5, 20, 0),
                child: TextFormField(
                  controller: _titleController,
                  decoration: InputDecoration(
                    isCollapsed: true,
                    contentPadding: const EdgeInsets.all(10),
                    fillColor: Colors.green[50],
                    filled: true,
                    border: const OutlineInputBorder(
                      borderSide: BorderSide.none,
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                    ),
                  ),
                ),
              ),
              const Padding(
                padding: EdgeInsets.all(10),
                child: Text("目的地", style: TextStyle(fontSize: 20)),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
                child: ListTile(
                  leading: Image.memory(
                    base64Decode(planController.selectedCityInfo['photos'][0]
                        .split(',')[1]),
                  ),
                  title: Text(planController.selectedCityInfo['city']),
                  subtitle: Text(
                      "${planController.selectedCityInfo['province']},${planController.selectedCityInfo['city']}"),
                ),
              ),
              const Padding(
                padding: EdgeInsets.all(10),
                child: Text("旅客", style: TextStyle(fontSize: 20)),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(40, 10, 20, 10),
                child: Text(planController.temp['customerType']),
              ),
              const Padding(
                padding: EdgeInsets.all(10),
                child: Text("日期", style: TextStyle(fontSize: 20)),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(40, 10, 20, 10),
                child: Text(
                    "${planController.temp['startDate']} - ${planController.temp['endDate']}"),
              ),
              const Padding(
                padding: EdgeInsets.all(10),
                child: Text("预算", style: TextStyle(fontSize: 20)),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(40, 10, 20, 10),
                child: Text(planController.temp['budget']),
              ),
              const Padding(
                padding: EdgeInsets.all(10),
                child: Text("交通", style: TextStyle(fontSize: 20)),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(40, 10, 20, 10),
                child: Text(planController.temp['traffic']),
              ),
              const Padding(
                padding: EdgeInsets.all(10),
                child: Text("兴趣", style: TextStyle(fontSize: 20)),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(40, 10, 20, 10),
                child: InlineChoice<String>(
                  clearable: true,
                  itemCount: planController.selectedTagsInfo.length,
                  itemBuilder: (state, i) {
                    return ChoiceChip(
                      selected: false,
                      label: Text(planController.selectedTagsInfo[i]['name']),
                      avatar: SvgPicture.asset(
                          planController.selectedTagsInfo[i]['photo']),
                    );
                  },
                  listBuilder: ChoiceList.createWrapped(
                    spacing: 5,
                    runSpacing: 5,
                    padding: const EdgeInsets.symmetric(
                      horizontal: 15,
                      vertical: 5,
                    ),
                  ),
                ),
              ),
              const SizedBox(
                height: 40,
              ),
            ],
          ),
          Positioned(
            bottom: 0,
            child: Container(
                width: MediaQuery.of(context).size.width,
                padding: const EdgeInsets.fromLTRB(10, 0, 10, 5),
                child: ElevatedButton(
                  onPressed: _send,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.greenAccent[700],
                    fixedSize: const Size(350, 45),
                    // padding: const EdgeInsets.all(10),
                    shape: const RoundedRectangleBorder(
                      borderRadius: BorderRadius.all(
                        Radius.circular(10),
                      ),
                    ),
                  ),
                  child: const Text("创建我的旅行计划",
                      style: TextStyle(color: Colors.white)),
                )),
          ),
        ],
      ),
    );
  }
}
