import "dart:async";
import 'package:get/get.dart';
import "package:uni_links/uni_links.dart";

class UniLinkService {
  StreamSubscription? sub;
  Uri? initialLink;

  Future<void> initUniLinks() async {
    print("initUniLinks");
    initialLink = (await getInitialLink()) as Uri?;
    handleLink(initialLink);
    sub = uriLinkStream.listen((Uri? link) {
      handleLink(link);
    }, onError: (Object err) {
      print("got err: $err");
    });
  }

  void handleLink(Uri? link) {
    if (link != null) {
      Get.toNamed(link.host, arguments: link.queryParameters);
      print("success link: $link");
    }
    print("false link: $link");
  }
}
