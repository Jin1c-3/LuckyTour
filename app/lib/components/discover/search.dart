import 'package:flutter/material.dart';
import 'package:choice/choice.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import '../../apis/strategy_apis.dart';
import '../../controllers/plan.dart';

class Search extends StatefulWidget {
  const Search({super.key});

  @override
  State<Search> createState() => _SearchState();
}

class _SearchState extends State<Search> {
  PlanController planController = Get.find();
  final _searchController = TextEditingController();
  String selectedValue = "";
  final choices = [];

  void setSelectedValue(String? value) {
    setState(() => selectedValue = value!);
  }

  void remember(value) {
    final history = GetStorage().read("searchHistory");
    if (history != null) {
      if (!history.contains(value)) {
        history.add(value);
        GetStorage().write("searchHistory", history);
      }
    } else {
      GetStorage().write("searchHistory", [value]);
    }
  }

  void search(String value) async {
    remember(value);
    final result = await StrategyApi().searchStrategy({"string": value});
    if (result["code"] == 200) {
      planController.strategyList.value = result["data"];
      Get.back();
    }
  }

  @override
  void initState() {
    super.initState();
    final box = GetStorage();
    final history = box.read("searchHistory");
    if (history != null) {
      choices.addAll(history);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 40, 20, 10),
            child: TextFormField(
              controller: _searchController,
              cursorColor: Colors.green,
              decoration: InputDecoration(
                isCollapsed: true,
                contentPadding: const EdgeInsets.all(10),
                fillColor: Colors.green[50],
                filled: true,
                border: const OutlineInputBorder(
                  borderSide: BorderSide.none,
                  borderRadius: BorderRadius.all(Radius.circular(10)),
                ),
                prefixIcon: const Icon(Icons.search),
                prefixIconColor: Colors.green[900],
              ),
              onFieldSubmitted: (value) => search(value),
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 10, 20, 0),
            child: Row(
              children: [
                const Text("搜索历史", style: TextStyle(fontSize: 20)),
                const Spacer(),
                IconButton(
                  icon: Icon(
                    Icons.delete,
                    color: Colors.green[900],
                  ),
                  onPressed: () {
                    GetStorage().remove("searchHistory");
                    setState(() => choices.clear());
                  },
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(10),
            child: Wrap(
              spacing: 10,
              runSpacing: 10,
              children: choices
                  .map(
                    (e) => TextButton(
                      onPressed: () => search(e),
                      style: ButtonStyle(
                        overlayColor:
                            MaterialStateProperty.all(Colors.transparent),
                      ),
                      child: Text(e,
                          style: TextStyle(
                              fontSize: 16, color: Colors.green[200])),
                    ),
                  )
                  .toList(),
            ),
          ),
        ],
      ),
    );
  }
}
