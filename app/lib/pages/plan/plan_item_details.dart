import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:lucky_tour/utils/map.dart';

class PlanItemDetails extends StatefulWidget {
  const PlanItemDetails({super.key});

  @override
  State<PlanItemDetails> createState() => _PlanItemDetailsState();
}

class _PlanItemDetailsState extends State<PlanItemDetails> {
  PlanController planController = Get.find();
  List _tags = [];

  @override
  void initState() {
    super.initState();
    if (planController.selectedPlanItemInfo['business'] != null) {
      if (planController.selectedPlanItemInfo['business']['tag'] != null) {
        _tags = planController.selectedPlanItemInfo['business']['tag']
            .split(',')
            .toList();
      } else {
        _tags = ['无'];
      }
    } else {
      _tags = ['无'];
    }
    print(planController.selectedPlanItemInfo);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Stack(
        children: [
          ListView(
            children: [
              Padding(
                padding: const EdgeInsets.all(15),
                child: Text(
                  planController.selectedPlanItemInfo['name'] ?? "",
                  style: const TextStyle(fontSize: 30),
                ),
              ),
              Container(
                margin: const EdgeInsets.all(15),
                padding: const EdgeInsets.fromLTRB(20, 10, 0, 10),
                decoration: BoxDecoration(
                  border: Border.all(
                    color: const Color.fromARGB(255, 99, 99, 99),
                    width: 1,
                  ),
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      planController.selectedPlanItemInfo['business']
                              ['rating'] ??
                          "暂无",
                      style: const TextStyle(
                          fontSize: 22, fontWeight: FontWeight.bold),
                    ),
                    const Text(
                      "评分",
                      style: TextStyle(color: Colors.grey, fontSize: 13),
                    ),
                  ],
                ),
              ),
              const Padding(
                padding: EdgeInsets.all(15),
                child: Text(
                  "图片",
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
              Container(
                margin: const EdgeInsets.fromLTRB(10, 10, 10, 10),
                height: 120,
                child: ListView.builder(
                  scrollDirection: Axis.horizontal,
                  itemCount:
                      planController.selectedPlanItemInfo['photos'] != null
                          ? planController.selectedPlanItemInfo['photos'].length
                          : 0,
                  itemBuilder: (context, index) => InkWell(
                    onTap: () {
                      Get.toNamed('/photo_view_simple', arguments: {
                        'photo':
                            planController.selectedPlanItemInfo['photos'] !=
                                    null
                                ? planController.selectedPlanItemInfo['photos']
                                    [index]['url']
                                : ""
                      });
                    },
                    child: Container(
                      height: 120,
                      width: 120,
                      margin: const EdgeInsets.fromLTRB(5, 0, 5, 0),
                      decoration: BoxDecoration(
                        image: DecorationImage(
                          image: NetworkImage(
                            planController.selectedPlanItemInfo['photos'] !=
                                    null
                                ? planController.selectedPlanItemInfo['photos']
                                    [index]['url']
                                : "",
                          ),
                          fit: BoxFit.cover,
                        ),
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(10),
                      ),
                    ),
                  ),
                ),
              ),
              const Padding(
                padding: EdgeInsets.all(15),
                child: Text(
                  "推荐标签",
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(10, 5, 5, 10),
                child: Wrap(
                  children: _tags
                      .map<Widget>(
                        (tag) => Container(
                          margin: const EdgeInsets.fromLTRB(5, 5, 5, 5),
                          padding: const EdgeInsets.fromLTRB(10, 5, 10, 5),
                          decoration: BoxDecoration(
                            color: Colors.grey[200],
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Text(tag),
                        ),
                      )
                      .toList(),
                ),
              ),
              const Padding(
                padding: EdgeInsets.all(15),
                child: Text(
                  "详情",
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
              ListTile(
                leading: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    borderRadius: BorderRadius.circular(5),
                  ),
                  child: const Icon(Icons.home_outlined),
                ),
                title: const Text("省/区/市"),
                subtitle: Text(
                  "${planController.selectedPlanItemInfo['pname'] ?? ""},${planController.selectedPlanItemInfo['cityname'] ?? ""},${planController.selectedPlanItemInfo['adname'] ?? ""}",
                ),
              ),
              ListTile(
                leading: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    borderRadius: BorderRadius.circular(5),
                  ),
                  child: const Icon(Icons.location_on_outlined),
                ),
                title: const Text("位置"),
                subtitle: Text(
                  "${planController.selectedPlanItemInfo['address'] ?? "暂无"}",
                ),
              ),
              ListTile(
                leading: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    borderRadius: BorderRadius.circular(5),
                  ),
                  child: const Icon(Icons.call_outlined),
                ),
                title: const Text("电话"),
                subtitle: Text(
                  "${planController.selectedPlanItemInfo['business']['tel'] ?? "暂无"}",
                ),
              ),
              ListTile(
                leading: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    borderRadius: BorderRadius.circular(5),
                  ),
                  child: const Icon(Icons.alarm_outlined),
                ),
                title: const Text("开放时间"),
                subtitle: Text(
                  "${planController.selectedPlanItemInfo['business']['opentime_week'] ?? "暂无"}",
                ),
              ),
              ListTile(
                leading: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    borderRadius: BorderRadius.circular(5),
                  ),
                  child: const Icon(Icons.search_outlined),
                ),
                title: const Text("类别"),
                subtitle: Text(
                  "${planController.selectedPlanItemInfo['business']['keytag'] ?? "暂无"}",
                ),
              ),
              const SizedBox(
                height: 60,
              ),
            ],
          ),
          Positioned(
            bottom: 0,
            child: Container(
              width: MediaQuery.of(context).size.width,
              padding: const EdgeInsets.fromLTRB(10, 0, 10, 5),
              child: ElevatedButton(
                onPressed: () {
                  MapUtil.detail(
                    planController.selectedPlanItemInfo['name'],
                    planController.selectedPlanItemInfo['location']
                        .split(',')[0],
                    planController.selectedPlanItemInfo['location']
                        .split(',')[1],
                    planController.selectedPlanItemInfo['id'],
                  );
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.greenAccent[700],
                  fixedSize: const Size(350, 45),
                  // padding: const EdgeInsets.all(10),
                  shape: const RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(
                      Radius.circular(20),
                    ),
                  ),
                ),
                child: const Text(
                  '更多详情',
                  style: TextStyle(color: Colors.white),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
