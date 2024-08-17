import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/controllers/plan.dart';
import '../../utils/map.dart';

class PlanShow extends StatefulWidget {
  const PlanShow({super.key});

  @override
  State<PlanShow> createState() => _PlanShowState();
}

class _PlanShowState extends State<PlanShow>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;
  PlanController planController = Get.find();
  final List<Widget> _tabs = [];
  final List<Widget> _tabViews = [];

  Widget classify(item) {
    IconData iconData;
    switch (item['type']) {
      case "hotel" || "酒店":
        iconData = Icons.hotel_outlined;
        break;
      case "restaurant" || "餐馆":
        iconData = Icons.restaurant_menu_outlined;
        break;
      case "scenicSpot" || "景点":
        iconData = Icons.terrain_outlined;
        break;
      default:
        return const Text("未知类型");
    }

    return buildContainer(iconData, item);
  }

  Widget buildContainer(IconData iconData, item) {
    return Container(
      width: MediaQuery.of(context).size.width,
      padding: const EdgeInsets.all(10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Column(
            children: [
              Container(
                width: 40,
                height: 40,
                decoration: BoxDecoration(
                  color: Colors.grey[200],
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Icon(iconData),
              ),
              Padding(
                padding: const EdgeInsets.only(top: 10),
                child: Text(
                    item['time'] != null ? item['time'].split('-')[0] : ""),
              ),
              Visibility(
                visible: item['time'] != null,
                child: Container(
                  margin: const EdgeInsets.only(top: 5, bottom: 5),
                  width: 3,
                  height: 50,
                  color: Colors.grey[300],
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 10),
                child: Text(
                    item['time'] != null ? item['time'].split('-')[1] : ""),
              ),
            ],
          ),
          const SizedBox(
            width: 20,
          ),
          Expanded(
            child: InkWell(
              borderRadius: BorderRadius.circular(10),
              child: Column(
                children: [
                  Container(
                    height: 150,
                    decoration: BoxDecoration(
                      image: DecorationImage(
                        image: NetworkImage(
                          item['photos'] != null
                              ? item['photos'][0]['url']
                              : "",
                        ),
                        fit: BoxFit.cover,
                      ),
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  ListTile(
                    title: Text(item['name'] ?? ""),
                    subtitle: Text(item['address'] ?? ""),
                  ),
                ],
              ),
              onTap: () {
                planController.selectedPlanItemInfo.value = item;
                Get.toNamed("/plan_item_details");
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget addDayPlan(items) {
    return ListView.builder(
      shrinkWrap: true,
      itemCount: items.length,
      itemBuilder: (BuildContext context, int index) {
        if (index != items.length - 1) {
          return Column(
            children: [
              classify(items[index]),
              Container(
                width: MediaQuery.of(context).size.width,
                padding: const EdgeInsets.all(10),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                      width: 40,
                      height: 40,
                      margin: const EdgeInsets.only(right: 20),
                      decoration: BoxDecoration(
                        color: Colors.grey[200],
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: const Icon(Icons.near_me_outlined),
                    ),
                    Expanded(
                      child: Container(
                        height: 50,
                        padding: const EdgeInsets.only(bottom: 10),
                        child: ElevatedButton(
                          onPressed: () => _routePlan(items[index]['location'],
                              items[index + 1]['location']),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.blue[400]!,
                            fixedSize: const Size(350, 45),
                            // padding: const EdgeInsets.all(10),
                            shape: const RoundedRectangleBorder(
                              borderRadius: BorderRadius.all(
                                Radius.circular(20),
                              ),
                            ),
                          ),
                          child: const Text("路径导航规划",
                              style: TextStyle(color: Colors.white)),
                        ),
                      ),
                    )
                  ],
                ),
              )
            ],
          );
        } else {
          return classify(items[index]);
        }
      },
    );
  }

  void _addAllDayPlan() {
    for (var key in planController.selectedPlanInfo['content'].keys) {
      _tabViews
          .add(addDayPlan(planController.selectedPlanInfo['content'][key]));
    }
  }

  void _routePlan(startLocation, endLocation) {
    var start = startLocation.split(",");
    var end = endLocation.split(",");
    MapUtil.routePlan(start[0], start[1], end[0], end[1]);
  }

  @override
  void initState() {
    super.initState();
    for (var key in planController.selectedPlanInfo['content'].keys) {
      _tabs.add(Tab(text: key));
    }
    _tabController = TabController(length: _tabs.length, vsync: this);
    _addAllDayPlan();
  }

  @override
  void dispose() {
    _tabController.dispose();
    planController.selectedPlanInfo.value = {};
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.only(left: 15),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  planController.selectedPlanInfo['title'],
                  style: const TextStyle(fontSize: 25),
                ),
                Text(
                  planController.selectedPlanInfo['city'],
                  style: const TextStyle(fontSize: 15, color: Colors.grey),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    TextButton(
                      onPressed: () => {
                        Get.toNamed("/plan_chat_update"),
                      },
                      style: ButtonStyle(
                        overlayColor:
                            MaterialStateProperty.all(Colors.transparent),
                      ),
                      child: const Column(
                        children: [
                          Icon(
                            Icons.edit,
                            color: Colors.grey,
                          ),
                          Text(
                            "编辑",
                            style: TextStyle(color: Colors.grey),
                          ),
                        ],
                      ),
                    ),
                    TextButton(
                      onPressed: () => {
                        Get.toNamed("/strategy_confirm"),
                      },
                      style: ButtonStyle(
                        overlayColor:
                            MaterialStateProperty.all(Colors.transparent),
                      ),
                      child: const Column(
                        children: [
                          Icon(Icons.text_snippet_outlined, color: Colors.grey),
                          Text("攻略", style: TextStyle(color: Colors.grey)),
                        ],
                      ),
                    ),
                  ],
                )
              ],
            ),
          ),
          TabBar(
            controller: _tabController,
            isScrollable: true,
            tabAlignment: TabAlignment.start,
            tabs: _tabs,
            labelColor: Colors.green,
            indicatorColor: Colors.green,
            overlayColor: MaterialStateProperty.all(Colors.transparent),
          ),
          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: _tabViews,
            ),
          ),
        ],
      ),
    );
  }
}
