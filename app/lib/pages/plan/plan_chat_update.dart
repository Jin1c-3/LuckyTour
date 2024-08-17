import 'dart:convert';
import 'dart:io';
import 'dart:math';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:get/get.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_chat_types/flutter_chat_types.dart' as types;
import 'package:flutter_chat_ui/flutter_chat_ui.dart';
import 'package:image_picker/image_picker.dart';
import 'package:lucky_tour/apis/plan_apis.dart';
import 'package:lucky_tour/apis/strategy_apis.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:open_filex/open_filex.dart';
import 'package:dio/dio.dart' as dio;

String randomString() {
  final random = Random.secure();
  final values = List<int>.generate(16, (i) => random.nextInt(255));
  return base64UrlEncode(values);
}

class PlanChatUpdate extends StatefulWidget {
  const PlanChatUpdate({super.key});

  @override
  State<PlanChatUpdate> createState() => _PlanChatUpdateState();
}

class _PlanChatUpdateState extends State<PlanChatUpdate> {
  PlanController planController = Get.find();
  final List<types.Message> _messages = [];
  final _user = const types.User(
    id: '82091008-a484-4a89-ae75-a22bf8d6f3ac',
  );
  final _bot = const types.User(
    id: 'f590b2e0-7b7e-4e0b-8e3e-6e6e3b3e3e3e',
    firstName: '小云',
  );
  String threadId = "";
  bool isLoading = false;
  bool isShowButton = false;

  @override
  void initState() {
    super.initState();
    _addMessage(
      types.TextMessage(
        author: _bot,
        createdAt: DateTime.now().millisecondsSinceEpoch,
        id: randomString(),
        text: "你好，我是您的旅行助手小云，请告诉我你对当前生成计划的修改意见，我将服务你获得最合适的计划安排",
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('云端'),
      ),
      body: Stack(
        children: [
          Chat(
            messages: _messages,
            onAttachmentPressed: _handleAttachmentPressed,
            onMessageTap: _handleMessageTap,
            onPreviewDataFetched: _handlePreviewDataFetched,
            onSendPressed: _handleSendPressed,
            user: _user,
            showUserNames: true,
            theme: DefaultChatTheme(
              inputMargin: const EdgeInsets.all(10),
              inputBorderRadius: const BorderRadius.all(Radius.circular(15)),
              inputBackgroundColor: Colors.green,
              inputTextCursorColor: Colors.green[900]!,
              primaryColor: Colors.green[400]!,
            ),
          ),
          Visibility(
            visible: isLoading,
            child: Positioned(
              left: MediaQuery.of(context).size.width / 2 - 25,
              bottom: 80,
              child: const SpinKitThreeBounce(
                color: Colors.green,
                size: 25.0,
              ),
            ),
          ),
          Visibility(
            visible: isShowButton,
            child: Positioned(
              width: MediaQuery.of(context).size.width,
              bottom: 0,
              child: Container(
                width: MediaQuery.of(context).size.width,
                height: 75,
                color: Colors.white,
                child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                        onPressed: () => confirm("失败"),
                        style: ButtonStyle(
                          backgroundColor:
                              MaterialStateProperty.all(Colors.white),
                          padding: MaterialStateProperty.all(
                            const EdgeInsets.fromLTRB(50, 10, 50, 10),
                          ),
                        ),
                        child: const Text(
                          '放弃',
                          style: TextStyle(color: Colors.green),
                        ),
                      ),
                      ElevatedButton(
                        onPressed: () => confirm("成功"),
                        style: ButtonStyle(
                          backgroundColor:
                              MaterialStateProperty.all(Colors.green),
                          padding: MaterialStateProperty.all(
                            const EdgeInsets.fromLTRB(50, 10, 50, 10),
                          ),
                        ),
                        child: const Text(
                          '接受',
                          style: TextStyle(color: Colors.white),
                        ),
                      ),
                    ]),
              ),
            ),
          ),
        ],
      ),
    );
  }

  void _addMessage(types.Message message) {
    setState(() {
      _messages.insert(0, message);
    });
  }

  void _handleAttachmentPressed() {
    showModalBottomSheet<void>(
      context: context,
      builder: (BuildContext context) => SafeArea(
        child: SizedBox(
          height: 150,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              TextButton(
                onPressed: () {
                  Get.back();
                  _handleImageSelection();
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.image),
                    SizedBox(width: 8),
                    Text('图片'),
                  ],
                ),
              ),
              TextButton(
                onPressed: () {
                  Get.back();
                  _handleFileSelection();
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.insert_drive_file),
                    SizedBox(width: 8),
                    Text('文件'),
                  ],
                ),
              ),
              TextButton(
                onPressed: () {
                  Get.back();
                  _addMessage(
                    types.TextMessage(
                      author: _bot,
                      createdAt: DateTime.now().millisecondsSinceEpoch,
                      id: randomString(),
                      text: "1\n2\n3\n4\n5",
                    ),
                  );
                },
                child: const Row(
                  children: <Widget>[
                    Icon(Icons.add),
                    SizedBox(width: 8),
                    Text('测试'),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _handleFileSelection() async {
    final result = await FilePicker.platform.pickFiles(
      type: FileType.any,
    );

    if (result != null && result.files.single.path != null) {
      final message = types.FileMessage(
        author: _user,
        createdAt: DateTime.now().millisecondsSinceEpoch,
        id: randomString(),
        name: result.files.single.name,
        size: result.files.single.size,
        uri: result.files.single.path!,
      );

      _addMessage(message);
    }
  }

  void _handleImageSelection() async {
    final result = await ImagePicker().pickImage(
      imageQuality: 70,
      maxWidth: 1440,
      source: ImageSource.gallery,
    );

    if (result != null) {
      final bytes = await result.readAsBytes();
      final image = await decodeImageFromList(bytes);
      final file = File(result.path);
      final multipartFile = await dio.MultipartFile.fromFile(file.path);

      final message = types.ImageMessage(
        author: _user,
        createdAt: DateTime.now().millisecondsSinceEpoch,
        height: image.height.toDouble(),
        id: randomString(),
        name: result.name,
        size: bytes.length,
        uri: result.path,
        width: image.width.toDouble(),
      );

      // _addMessage(message);
      // _sendImage(multipartFile);
    }
  }

  void _handleMessageTap(BuildContext _, types.Message message) async {
    if (message is types.FileMessage) {
      var localPath = message.uri;

      await OpenFilex.open(localPath);
    }
  }

  void _handlePreviewDataFetched(
    types.TextMessage message,
    types.PreviewData previewData,
  ) {
    final index = _messages.indexWhere((element) => element.id == message.id);
    final updatedMessage = (_messages[index] as types.TextMessage).copyWith(
      previewData: previewData,
    );

    setState(() {
      _messages[index] = updatedMessage;
    });
  }

  void _handleSendPressed(types.PartialText message) {
    final textMessage = types.TextMessage(
      author: _user,
      createdAt: DateTime.now().millisecondsSinceEpoch,
      id: randomString(),
      text: message.text,
    );

    _addMessage(textMessage);
    _sendText(message.text);
  }

  void _sendText(message) async {
    setState(() {
      isLoading = true;
    });
    final result = await PlanApi().updatePlan({
      "plan": planController.selectedPlanInfo,
      "reason": message,
    });
    setState(() {
      isLoading = false;
      isShowButton = true;
    });
    if (result['code'] == 200) {
      final textMessage = types.TextMessage(
        author: _bot,
        createdAt: DateTime.now().millisecondsSinceEpoch,
        id: randomString(),
        text: result['data'],
      );

      _addMessage(textMessage);
    }
  }

  void _sendImage(message) async {
    dio.FormData data = dio.FormData.fromMap({
      "image": message,
      "thread_id": threadId,
    });
    setState(() {
      isLoading = true;
    });
    final result = await PlanApi().generatePlanByChat(data);
    setState(() {
      isLoading = false;
    });
    if (result['code'] == 200) {
      final textMessage = types.TextMessage(
        author: _bot,
        createdAt: DateTime.now().millisecondsSinceEpoch,
        id: randomString(),
        text: result['data']['content'],
      );

      _addMessage(textMessage);

      threadId = result['data']['thread_id'];
    }
  }

  void confirm(choice) async {
    setState(() {
      isShowButton = false;
    });
    final result = await PlanApi().confirmUpdatePlan({
      "message": choice,
    });
    if (result['code'] == 200) {
      final news = await PlanApi().getPlanByPid({
        "pid": planController.selectedPlanInfo['pid'],
      });
      if (news['code'] == 200) {
        planController.selectedPlanInfo = news['data'];
      }
    }
  }
}
