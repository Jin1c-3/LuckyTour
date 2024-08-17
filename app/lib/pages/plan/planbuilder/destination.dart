import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../../components/plan/list_item_shimmer.dart';
import '../../../components/plan/destinations_list_item.dart';
import '../../../apis/plan_apis.dart';
import '../../../controllers/plan.dart';
import '../../../utils/chinese_input.dart';

class Destination extends StatefulWidget {
  const Destination({super.key});

  @override
  State<Destination> createState() => _DestinationState();
}

class _DestinationState extends State<Destination> {
  PlanController planController = Get.find();
  ChineseTextEditController chineseTextEditController =
      ChineseTextEditController();

  String _searchText = '';
  bool _loading = false;
  List _cities = [];

  void _search(value) async {
    setState(() {
      _searchText = value;
      _loading = true;
    });
    final result = await PlanApi().getCities({
      "character": _searchText,
    });
    if (result['code'] == 200) {
      setState(() {
        _loading = false;
        _cities = result['data'];
      });
    }
  }

  void _select(index) async {
    planController.selectedCityInfo.value = _cities[index];
    final result = await PlanApi().getLatlng({
      'address': planController.selectedCityInfo['city'],
    });
    if (result['code'] == 200) {
      final info = result['data'].split(',');
      planController.selectedCityLatlng.value = {
        'latitude': double.parse(info[1]),
        'longitude': double.parse(info[0]),
      };
      Get.toNamed('/destination_info');
    }
  }

  @override
  void initState() {
    super.initState();

    chineseTextEditController.addListener(() {
      if (_searchText != chineseTextEditController.completeText) {
        _searchText = chineseTextEditController.completeText;
        _search(_searchText);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Padding(
        padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "目的地",
              style: TextStyle(fontSize: 30),
            ),
            Text(
              "选择一座城市，开始你的旅行吧！",
              style: TextStyle(fontSize: 15, color: Colors.green[200]),
            ),
            const SizedBox(height: 30),
            TextField(
              controller: chineseTextEditController,
              cursorColor: Colors.green,
              decoration: InputDecoration(
                isCollapsed: true,
                contentPadding: const EdgeInsets.all(10),
                fillColor: Colors.green[50],
                filled: true,
                border: const OutlineInputBorder(
                  borderSide: BorderSide.none,
                  borderRadius: BorderRadius.all(Radius.circular(10)),
                ),
                hintText: '城市',
              ),
            ),
            const SizedBox(height: 20),
            Visibility(
              visible: _searchText == '',
              maintainSize: false,
              child: const SizedBox(
                height: 300,
                width: double.infinity,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      Icons.explore,
                      size: 40,
                      color: Colors.green,
                    ),
                    SizedBox(height: 10),
                    Text('立即开始搜索', style: TextStyle(color: Colors.green)),
                  ],
                ),
              ),
            ),
            Visibility(
              visible: _searchText != '' && _loading == true,
              maintainSize: false,
              child: const Expanded(
                child: ListItemShimmer(),
              ),
            ),
            Visibility(
              visible: _searchText != '' && _loading == false,
              maintainSize: false,
              child: Expanded(
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: _cities.length,
                  itemBuilder: (context, index) => DestinationsListItem(
                      photo: _cities[index]['photos'][0],
                      city: _cities[index]['city'],
                      province: _cities[index]['province'],
                      onTap: () => _select(index)),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
