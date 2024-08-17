import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:flutter_svg/flutter_svg.dart';
import '../../../controllers/plan.dart';

class Budget extends StatefulWidget {
  const Budget({super.key});

  @override
  State<Budget> createState() => _BudgetState();
}

class _BudgetState extends State<Budget> {
  PlanController planController = Get.find();

  final _items = [
    {
      "name": '经济',
      "description": '最低的价格最高的性价比',
      "image": 'images/budget/cheap.svg'
    },
    {
      "name": '舒适',
      "description": '用适当的价格换取最舒适的旅行',
      "image": 'images/budget/moderate.svg'
    },
    {
      "name": '豪华',
      "description": '将旅行服务拉到最满意的程度',
      "image": 'images/budget/luxury.svg'
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
                '预算',
                style: TextStyle(fontSize: 30),
              ),
              Text(
                '你想花费多少为这次旅行',
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
                      planController.temp['budget'] = item['name'];
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
                  onPressed: planController.temp['budget'] == null
                      ? null
                      : () {
                          Get.toNamed("/traffic");
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
