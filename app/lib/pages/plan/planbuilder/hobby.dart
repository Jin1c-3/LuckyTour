import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:choice/choice.dart';
import 'package:flutter_svg/flutter_svg.dart';
import '../../../controllers/plan.dart';

class Hobby extends StatefulWidget {
  const Hobby({super.key});

  @override
  State<Hobby> createState() => _HobbyState();
}

class _HobbyState extends State<Hobby> {
  PlanController planController = Get.find();

  final tags = [
    {
      "name": "美食",
      "photo": "images/tag/food.svg",
    },
    {
      "name": "购物",
      "photo": "images/tag/shopping.svg",
    },
    {
      "name": "户外",
      "photo": "images/tag/outdoor.svg",
    },
    {
      "name": "历史",
      "photo": "images/tag/culture.svg",
    },
    {
      "name": "运动",
      "photo": "images/tag/sport.svg",
    },
    {
      "name": "自然",
      "photo": "images/tag/nature.svg",
    },
    {
      "name": "艺术",
      "photo": "images/tag/art.svg",
    },
    {
      "name": "音乐",
      "photo": "images/tag/music.svg",
    },
    {
      "name": "夜生活",
      "photo": "images/tag/beer.svg",
    }
  ];
  List<String> _selectedValue = [];

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
                '兴趣',
                style: TextStyle(fontSize: 30),
              ),
              Text(
                '选择你感兴趣的标签',
                style: TextStyle(fontSize: 15, color: Colors.green[200]),
              ),
              InlineChoice<String>(
                clearable: true,
                multiple: true,
                value: _selectedValue,
                onChanged: (List<String> value) {
                  setState(() => _selectedValue = value);
                },
                itemCount: tags.length,
                itemBuilder: (state, i) {
                  return ChoiceChip(
                    backgroundColor: Colors.white,
                    selectedColor: Colors.green[100],
                    selected: state.selected(tags[i]['name']!),
                    onSelected: state.onSelected(tags[i]['name']!),
                    label: Text(tags[i]['name']!),
                    avatar: SvgPicture.asset(tags[i]['photo']!),
                  );
                },
                listBuilder: ChoiceList.createWrapped(
                  spacing: 5,
                  runSpacing: 5,
                  padding: const EdgeInsets.symmetric(
                    horizontal: 20,
                    vertical: 25,
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
                child: ElevatedButton(
                  onPressed: () {
                    planController.selectedTagsInfo.clear();
                    for (var tag in tags) {
                      if (_selectedValue.contains(tag['name'])) {
                        planController.selectedTagsInfo.add(tag);
                      }
                    }
                    planController.temp['tag'] = _selectedValue;
                    Get.toNamed("/all");
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
                )),
          ),
        ],
      ),
    );
  }
}
