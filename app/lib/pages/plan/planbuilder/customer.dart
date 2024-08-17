import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:get/get.dart';
import '../../../controllers/plan.dart';

class Customer extends StatefulWidget {
  const Customer({super.key});

  @override
  State<Customer> createState() => _CustomerState();
}

class _CustomerState extends State<Customer> {
  PlanController planController = Get.find();

  final _items = [
    {
      "name": '单人',
      "description": '独享一个人的旅行',
      "image": 'images/customerType/self.svg'
    },
    {
      "name": '情侣',
      "description": '两个人的甜蜜旅程',
      "image": 'images/customerType/couple.svg'
    },
    {
      "name": '家庭',
      "description": '三个人及以上的温馨旅行',
      "image": 'images/customerType/family.svg'
    },
    {
      "name": '朋友',
      "description": '三个人及以上的有趣旅行',
      "image": 'images/customerType/friends.svg'
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
                '旅客',
                style: TextStyle(fontSize: 30),
              ),
              Text(
                '哪些人会加入这场旅途？',
                style: TextStyle(fontSize: 15, color: Colors.green[200]),
              ),
              for (var item in _items)
                Container(
                  margin: const EdgeInsets.fromLTRB(20, 20, 20, 0),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(15),
                    border: Border.all(color: Colors.green[200]!),
                  ),
                  child: InkWell(
                    onTap: () {
                      setState(() {
                        _selectedIndex = _items.indexOf(item);
                      });
                      planController.temp['customerType'] = item['name'];
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
                  onPressed: planController.temp['customerType'] == null
                      ? null
                      : () {
                          Get.toNamed('/date');
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
