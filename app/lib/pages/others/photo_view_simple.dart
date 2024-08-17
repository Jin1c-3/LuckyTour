import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:photo_view/photo_view.dart';
import 'package:get/get.dart';

class PhotoViewSimpleScreen extends StatefulWidget {
  const PhotoViewSimpleScreen({super.key});

  @override
  State<PhotoViewSimpleScreen> createState() => _PhotoViewSimpleScreenState();
}

class _PhotoViewSimpleScreenState extends State<PhotoViewSimpleScreen> {
  late final image;

  @override
  void initState() {
    super.initState();
    if (Get.arguments['photo'].startsWith('http://') ||
        Get.arguments['photo'].startsWith('https://')) {
      image = NetworkImage(Get.arguments['photo']);
    } else {
      image = MemoryImage(base64Decode(Get.arguments['photo'].split(',')[1]));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: PhotoView(
        imageProvider: image,
      ),
    );
  }
}
