import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:get/get.dart';
import '../../../controllers/plan.dart';

class Traffic extends StatefulWidget {
  const Traffic({super.key});

  @override
  State<Traffic> createState() => _TrafficState();
}

class _TrafficState extends State<Traffic> {
  PlanController planController = Get.find();

  final _items = [
    {
      "name": '飞机',
      "description": '快速到达目的地',
      "image": 'images/traffic/plane.svg'
    },
    {
      "name": '火车',
      "description": '经济实惠的选择',
      "image": 'images/traffic/train.svg'
    },
    {"name": '汽车', "description": '自由自在的旅行', "image": 'images/traffic/car.svg'},
    {
      "name": '轮船',
      "description": '浪漫的海上之旅',
      "image": 'images/traffic/boat.svg'
    },
    {
      "name": '步行',
      "description": '慢慢走，慢慢看',
      "image": 'images/traffic/walk.svg'
    },
    {
      "name": '地铁',
      "description": '城市交通的首选',
      "image": 'images/traffic/subway.svg'
    },
  ];
  int _selectedIndex = -1;

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
                '交通',
                style: TextStyle(fontSize: 30),
              ),
              Text(
                '你喜欢乘坐什么交通工具出行？',
                style: TextStyle(fontSize: 15, color: Colors.green[200]),
              ),
              for (var item in _items)
                Container(
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(15),
                    border: Border.all(color: Colors.green[200]!),
                  ),
                  margin: const EdgeInsets.fromLTRB(20, 20, 20, 0),
                  child: InkWell(
                    onTap: () {
                      setState(() {
                        _selectedIndex = _items.indexOf(item);
                      });
                      planController.temp['traffic'] = item['name'];
                    },
                    highlightColor: Colors.transparent,
                    splashColor: Colors.transparent,
                    child: ListTile(
                      title: Text(item['name']!),
                      subtitle: Text(item['description']!),
                      leading: Radio(
                        value: _items.indexOf(item),
                        groupValue: _selectedIndex,
                        activeColor: Colors.greenAccent[700],
                        onChanged: (value) {},
                      ),
                      trailing: SizedBox(
                        width: 40,
                        height: 40,
                        child: SvgPicture.asset(item['image']!),
                      ),
                    ),
                  ),
                ),
            ],
          ),
          Positioned(
            bottom: 0,
            child: Container(
              width: MediaQuery.of(context).size.width,
              padding: const EdgeInsets.fromLTRB(10, 0, 10, 5),
              child: Obx(
                () => ElevatedButton(
                  onPressed: planController.temp['traffic'] == null
                      ? null
                      : () {
                          Get.toNamed('/hobby');
                        },
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
                  child:
                      const Text("下一步", style: TextStyle(color: Colors.white)),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
