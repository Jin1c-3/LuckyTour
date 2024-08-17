import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../../controllers/plan.dart';
import 'package:sliding_up_panel2/sliding_up_panel2.dart';
import '../../others/map_view.dart';

class DestinationInfo extends StatefulWidget {
  const DestinationInfo({super.key});

  @override
  State<DestinationInfo> createState() => _DestinationInfoState();
}

class _DestinationInfoState extends State<DestinationInfo> {
  PlanController planController = Get.find();
  MapView mapView = const MapView();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SlidingUpPanel(
        borderRadius: const BorderRadius.only(
            topLeft: Radius.circular(18.0), topRight: Radius.circular(18.0)),
        header: SizedBox(
          width: MediaQuery.of(context).size.width,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ForceDraggableWidget(
                child: SizedBox(
                  width: 100,
                  height: 40,
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const SizedBox(
                        height: 12.0,
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: <Widget>[
                          Container(
                            width: 30,
                            height: 7,
                            decoration: const BoxDecoration(
                                color: Colors.grey,
                                borderRadius:
                                    BorderRadius.all(Radius.circular(12.0))),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
        panelBuilder: () => Stack(
          children: [
            ListView(
              shrinkWrap: true,
              padding: const EdgeInsets.all(20),
              children: [
                Text(
                  planController.selectedCityInfo['city'],
                  style: const TextStyle(fontSize: 40),
                ),
                const Divider(
                  height: 20,
                ),
                Row(
                  children: [
                    const Icon(
                      Icons.location_on,
                      color: Colors.grey,
                    ),
                    const SizedBox(
                      width: 10,
                    ),
                    Text(
                      "${planController.selectedCityInfo['province']},${planController.selectedCityInfo['city']}",
                      style: const TextStyle(
                        fontSize: 15,
                        color: Colors.grey,
                      ),
                    ),
                  ],
                ),
                Row(
                  children: [
                    const Icon(Icons.code, color: Colors.grey),
                    const SizedBox(
                      width: 10,
                    ),
                    Text(
                      planController.selectedCityInfo['citycode'].toString(),
                      style: const TextStyle(
                        fontSize: 15,
                        color: Colors.grey,
                      ),
                    ),
                  ],
                ),
                Container(
                  margin: const EdgeInsets.fromLTRB(0, 10, 0, 10),
                  height: 100,
                  child: ListView.builder(
                    scrollDirection: Axis.horizontal,
                    itemCount: planController.selectedCityInfo['photos'].length,
                    itemBuilder: (context, index) => InkWell(
                      onTap: () {
                        Get.toNamed('/photo_view_simple', arguments: {
                          'photo': planController.selectedCityInfo['photos']
                              [index]
                        });
                      },
                      child: Container(
                        height: 100,
                        width: 100,
                        margin: const EdgeInsets.fromLTRB(5, 0, 5, 0),
                        decoration: BoxDecoration(
                          image: DecorationImage(
                            image: MemoryImage(
                              base64Decode(planController
                                  .selectedCityInfo['photos'][index]
                                  .split(',')[1]),
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
                Text(
                  planController.selectedCityInfo['description'],
                  style: const TextStyle(fontSize: 15),
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
                    Get.toNamed('/customer');
                    planController.temp['destination'] =
                        planController.selectedCityInfo['city'];
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
                      const Text('下一步', style: TextStyle(color: Colors.white)),
                ),
              ),
            ),
          ],
        ),
        body: mapView,
      ),
    );
  }
}
