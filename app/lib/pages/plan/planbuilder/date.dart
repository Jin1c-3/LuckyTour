import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:scrollable_clean_calendar/utils/enums.dart';
import '../../../controllers/plan.dart';
import 'package:scrollable_clean_calendar/controllers/clean_calendar_controller.dart';
import 'package:scrollable_clean_calendar/scrollable_clean_calendar.dart';

class Date extends StatefulWidget {
  const Date({super.key});

  @override
  State<Date> createState() => _DateState();
}

class _DateState extends State<Date> {
  PlanController planController = Get.find();
  late CleanCalendarController _calendarController;

  @override
  void initState() {
    super.initState();
    _calendarController = CleanCalendarController(
      minDate: DateTime.now(),
      maxDate: DateTime.now().add(const Duration(days: 365)),
      onRangeSelected: (firstDate, secondDate) {
        planController.temp['startDate'] = firstDate.toString().split(' ')[0];
        planController.temp['endDate'] = secondDate.toString().split(' ')[0];
      },
      weekdayStart: DateTime.monday,
    );
  }

  void select(firstDate, secondDate) {}
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Stack(
        children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(15, 10, 15, 10),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '日期',
                      style: TextStyle(fontSize: 30),
                    ),
                    Text(
                      '什么时候来一场旅行？',
                      style: TextStyle(fontSize: 15, color: Colors.green[200]),
                    ),
                  ],
                ),
              ),
              Expanded(
                child: ScrollableCleanCalendar(
                  calendarController: _calendarController,
                  layout: Layout.DEFAULT,
                  calendarCrossAxisSpacing: 0,
                  dayBackgroundColor: Colors.green[50],
                  daySelectedBackgroundColorBetween: Colors.green[200],
                  daySelectedBackgroundColor: Colors.green,
                ),
              ),
            ],
          ),
          Positioned(
            bottom: 0,
            child: Container(
              width: MediaQuery.of(context).size.width,
              padding: const EdgeInsets.fromLTRB(10, 0, 10, 5),
              child: Obx(
                () => ElevatedButton(
                  onPressed: planController.temp['startDate'] == null
                      ? null
                      : () {
                          Get.toNamed("/budget");
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
                      const Text("下一步", style: TextStyle(color: Colors.white)),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
