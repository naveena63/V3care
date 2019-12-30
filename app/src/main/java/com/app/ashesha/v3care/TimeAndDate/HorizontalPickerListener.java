package com.app.ashesha.v3care.TimeAndDate;


public interface HorizontalPickerListener {
    void onStopDraggingPicker();
    void onDraggingPicker();
    void onDateSelected(Day item);
}