import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import './routes/routes.dart';
import './utils/binding.dart';
import './utils/push.dart';
import './utils/uni_link_service.dart';
import './utils/location.dart';
import './utils/notification_util.dart';

main() async {
  await GetStorage.init();

  NotificationHelper notificationHelper = NotificationHelper();
  await notificationHelper.initialize();

  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final push = Push();
  final uniLinkService = UniLinkService();
  final location = Location();

  @override
  void initState() {
    super.initState();
    push.init();
    uniLinkService.initUniLinks();
    if (GetStorage().read("isDynamicState") == true) {
      location.init();
      location.startLocation();
    }
  }

  @override
  void dispose() {
    super.dispose();
    uniLinkService.sub?.cancel();
    location.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      theme: ThemeData(
        appBarTheme: const AppBarTheme(color: Colors.white),
        scaffoldBackgroundColor: Colors.white,
      ),
      debugShowCheckedModeBanner: false,
      initialRoute: '/login_or_register',
      getPages: AppPage.routes,
      initialBinding: AllControllerBinding(),
    );
  }
}
