import 'package:flutter/material.dart';

class PlanCardItem extends StatelessWidget {
  final String title;
  final Function? onTap;

  const PlanCardItem({super.key, this.onTap, required this.title});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        title: Text(title),
        onTap: () {
          onTap!();
        },
      ),
    );
  }
}
