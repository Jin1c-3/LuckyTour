import 'package:flutter/material.dart';
import 'package:bottom_bar_matu/bottom_bar_matu.dart';
// import 'package:get/get.dart';
import '../../components/root/home.dart';
import '../../components/root/discover.dart';
import '../../components/root/plan.dart';
import '../../components/root/profile.dart';

class RootPage extends StatefulWidget {
  const RootPage({super.key});

  @override
  State<RootPage> createState() => _RootPageState();
}

class _RootPageState extends State<RootPage> {
  final List<Widget> _pages = const <Widget>[
    Home(),
    Discover(),
    Plan(),
    Profile(),
  ];
  int _currentPage = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _pages[_currentPage],
      bottomNavigationBar: BottomBarBubble(
        color: Colors.green,
        selectedIndex: _currentPage,
        items: [
          BottomBarItem(
            iconData: Icons.home_outlined,
            iconSize: 30,
            label: _currentPage == 0 ? "首页" : "",
            labelTextStyle: const TextStyle(fontSize: 12),
          ),
          BottomBarItem(
            iconData: Icons.explore_outlined,
            label: _currentPage == 1 ? "发现" : "",
            labelTextStyle: const TextStyle(fontSize: 12),
            iconSize: 30,
          ),
          BottomBarItem(
            iconData: Icons.edit_outlined,
            label: _currentPage == 2 ? "计划" : "",
            labelTextStyle: const TextStyle(fontSize: 12),
            iconSize: 30,
          ),
          BottomBarItem(
            iconData: Icons.account_circle_outlined,
            label: _currentPage == 3 ? "我的" : "",
            labelTextStyle: const TextStyle(fontSize: 12),
            iconSize: 30,
          ),
        ],
        onSelect: (index) {
          setState(() {
            _currentPage = index;
          });
        },
      ),
    );
  }
}
