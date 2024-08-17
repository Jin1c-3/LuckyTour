import 'package:flutter/material.dart';
import 'package:lucky_tour/controllers/app.dart';
import 'package:get/get.dart';

class Notifications extends StatefulWidget {
  const Notifications({super.key});

  @override
  State<Notifications> createState() => _NotificationsState();
}

class _NotificationsState extends State<Notifications> {
  AppController appController = Get.find();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("通知"),
      ),
      body: Obx(
        () => ListView.builder(
          itemCount: appController.notificationList.length,
          itemBuilder: (context, index) {
            return ListTile(
              leading: Container(
                decoration: BoxDecoration(
                  border: Border.all(color: Colors.grey),
                  borderRadius: BorderRadius.circular(10),
                ),
                width: 40,
                height: 40,
                child: Image.asset('images/app.png'),
              ),
              title: Text(appController.notificationList[index]['title']),
              subtitle:
                  Text(appController.notificationList[index]['description']),
              trailing: Column(
                children: [
                  const SizedBox(
                    height: 6,
                  ),
                  Text(
                    appController.notificationList[index]['time'],
                    style: const TextStyle(fontSize: 15),
                  ),
                ],
              ),
              onTap: () {
                Get.bottomSheet(
                  Container(
                    height: 100,
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(15),
                        topRight: Radius.circular(15),
                      ),
                    ),
                    child: Column(
                      children: [
                        ListTile(
                          title: const Text('删除'),
                          leading: const Icon(Icons.delete),
                          onTap: () {
                            appController.notificationList.removeAt(index);
                            Get.back();
                          },
                        ),
                      ],
                    ),
                  ),
                );
              },
            );
          },
        ),
      ),
    );
  }
}
