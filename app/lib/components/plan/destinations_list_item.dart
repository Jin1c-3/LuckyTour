import 'dart:convert';

import 'package:flutter/material.dart';

class DestinationsListItem extends StatelessWidget {
  final String? photo;
  final String? city;
  final String? province;
  final Function? onTap;

  const DestinationsListItem(
      {super.key, this.photo, this.city, this.province, this.onTap});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Container(
        height: 50,
        width: 50,
        margin: const EdgeInsets.fromLTRB(20, 0, 10, 0),
        decoration: BoxDecoration(
          image: DecorationImage(
            image: MemoryImage(base64Decode(photo!.split(',')[1])),
            fit: BoxFit.cover,
          ),
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
        ),
      ),
      title: Text(city!),
      subtitle: Text("${province!},${city!}"),
      onTap: () {
        onTap!();
      },
    );
  }
}
