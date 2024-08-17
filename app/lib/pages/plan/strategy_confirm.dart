import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:lucky_tour/apis/strategy_apis.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:lucky_tour/controllers/user.dart';

class StrategyConfirm extends StatefulWidget {
  const StrategyConfirm({super.key});

  @override
  State<StrategyConfirm> createState() => _StrategyConfirmState();
}

class _StrategyConfirmState extends State<StrategyConfirm> {
  UserController userController = Get.find();
  PlanController planController = Get.find();
  bool _isLoading = true;
  final TextEditingController _titleController = TextEditingController();
  final List<TextEditingController> _controllers = [];

  Map temp = {'content': []};

  final List _imageList = [];

  XFile? _image;
  File? file;

  void _openGallery(index) async {
    _image = await ImagePicker().pickImage(source: ImageSource.gallery);
    if (_image == null) return;
    file = File(_image!.path);
    final bytes = await file!.readAsBytes();
    final base64String = base64Encode(bytes);
    temp['content'][index]['photos'].add(base64String);
    setState(() {
      _imageList[index].add(file);
    });
  }

  void _createStrategy() async {
    final result = await StrategyApi().createStrategy(temp);
    if (result['code'] == 200) {
      Get.snackbar(
        '成功',
        result['message'],
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Colors.green,
        colorText: Colors.white,
        margin: const EdgeInsets.all(20),
      );
    }
  }

  void _createCharacter() async {
    final result = await StrategyApi().aiCreateStrategy({
      "plan": jsonEncode(planController.selectedPlanInfo['content']),
    });
    print(result);
    if (result['code'] == 200) {
      setState(() {
        temp['content'] = result['data']['result'];
      });
      for (var i = 0; i < temp['content'].length; i++) {
        _controllers.add(TextEditingController());
        _controllers[i].text = temp['content'][i]['comment'];
        _imageList.add([]);
      }
      temp['uid'] = planController.selectedPlanInfo['uid'];
      temp['pid'] = planController.selectedPlanInfo['pid'];
      _isLoading = false;
    }
  }

  @override
  void initState() {
    super.initState();
    _createCharacter();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("创建攻略"),
        actions: [
          IconButton(
            onPressed: () {
              Get.dialog(
                Center(
                  child: Container(
                    width: 300,
                    height: 100,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: const Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Tips",
                          style: TextStyle(fontSize: 20, color: Colors.yellow),
                        ),
                      ],
                    ),
                  ),
                ),
              );
            },
            icon: const Icon(
              Icons.tips_and_updates,
              color: Colors.yellow,
            ),
          ),
        ],
      ),
      body: Stack(
        children: [
          Visibility(
            visible: _isLoading,
            child: const Scaffold(
              body: Center(
                child: SpinKitSpinningLines(
                  color: Colors.green,
                  size: 50.0,
                ),
              ),
            ),
          ),
          Visibility(
            visible: !_isLoading,
            child: Stack(
              children: [
                Padding(
                    padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          "标题",
                          style: TextStyle(fontSize: 25),
                        ),
                        TextField(
                          controller: _titleController,
                          decoration: const InputDecoration(
                            isCollapsed: true,
                            contentPadding: EdgeInsets.all(10),
                            fillColor: Color.fromARGB(255, 229, 229, 229),
                            filled: true,
                            border: OutlineInputBorder(
                              borderSide: BorderSide.none,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(10)),
                            ),
                          ),
                          onChanged: (value) {
                            temp['title'] = value;
                          },
                        ),
                        const SizedBox(
                          height: 10,
                        ),
                        Expanded(
                          child: ListView.builder(
                            shrinkWrap: true,
                            itemCount: temp['content'].length ?? 0,
                            itemBuilder: (BuildContext context, int index) {
                              return Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.all(5),
                                    child: Text(
                                      "${index + 1}.${temp['content'][index]['destination']}",
                                      style: const TextStyle(fontSize: 25),
                                    ),
                                  ),
                                  TextField(
                                    controller: _controllers[index],
                                    maxLines: null,
                                    decoration: const InputDecoration(
                                      isCollapsed: true,
                                      contentPadding: EdgeInsets.all(10),
                                      fillColor:
                                          Color.fromARGB(255, 229, 229, 229),
                                      filled: true,
                                      border: OutlineInputBorder(
                                        borderSide: BorderSide.none,
                                        borderRadius: BorderRadius.all(
                                            Radius.circular(10)),
                                      ),
                                    ),
                                    onChanged: (value) {
                                      temp['content'][index]['comment'] = value;
                                    },
                                  ),
                                  Row(
                                    children: [
                                      Container(
                                        margin: const EdgeInsets.fromLTRB(
                                            10, 20, 10, 50),
                                        height: 80,
                                        width: 80,
                                        decoration: BoxDecoration(
                                          color: Colors.grey,
                                          borderRadius: BorderRadius.circular(
                                              4.0), // 设置圆角的半径
                                        ),
                                        child: IconButton(
                                          onPressed: () => _openGallery(index),
                                          icon: const Icon(Icons.add),
                                          color: Colors
                                              .white, // 设置图标颜色为白色以便在灰色背景上看清
                                          style: ButtonStyle(
                                            shape: MaterialStateProperty.all(
                                              RoundedRectangleBorder(
                                                borderRadius:
                                                    BorderRadius.circular(0),
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      Expanded(
                                        child: SizedBox(
                                          height: 150,
                                          child: ListView.builder(
                                            scrollDirection: Axis.horizontal,
                                            shrinkWrap: true,
                                            itemCount:
                                                _imageList[index].length ?? 0,
                                            itemBuilder: (BuildContext context,
                                                int index_) {
                                              return Stack(
                                                children: [
                                                  Container(
                                                    margin: const EdgeInsets
                                                        .fromLTRB(
                                                        10, 20, 10, 50),
                                                    width: 80,
                                                    height: 100,
                                                    decoration: BoxDecoration(
                                                      image: DecorationImage(
                                                        image: FileImage(
                                                          _imageList[index]
                                                              [index_],
                                                        ),
                                                        fit: BoxFit.cover,
                                                      ),
                                                      color: Colors.grey,
                                                      borderRadius:
                                                          BorderRadius.circular(
                                                              10.0),
                                                    ),
                                                  ),
                                                  Positioned(
                                                    right: -12,
                                                    top: -5,
                                                    child: IconButton(
                                                      onPressed: () {
                                                        setState(() {
                                                          _imageList[index]
                                                              .removeAt(index_);
                                                        });
                                                      },
                                                      icon: const Icon(
                                                          Icons.cancel),
                                                      color: Colors.red,
                                                    ),
                                                  ),
                                                ],
                                              );
                                            },
                                          ),
                                        ),
                                      ),
                                    ],
                                  )
                                ],
                              );
                            },
                          ),
                        ),
                      ],
                    )),
                Positioned(
                  bottom: 1,
                  left: 10,
                  right: 10,
                  child: ElevatedButton(
                    onPressed: _createStrategy,
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
                    child:
                        const Text("发布", style: TextStyle(color: Colors.white)),
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
