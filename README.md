# DateRangePickerDemo
Date Range Picker from Material Components with max range and custom text

### Added features

#### Behaviour of max range exceeded added
<img src="https://i.imgur.com/dfxrnXq.gif" title="Toast showing max range error" /></a>

1. Show a toast / dialog when the defined max range is exceeded
2. Disable the confirmation button while the range is invalid

#### Customization of fields added in the fork -> [balves42/material-components-android](https://github.com/balves42/material-components-android)
<img src="https://imgur.com/SPFO3Sh.jpg" title="Custom fields added" /></a>

#### Before we begin
Since there wasn't a direct approach to perform this, we needed an external view to be passed to our custom class (in this case, the button that is clicked). A listener is implemented where we check when the focus changes and we have the calendar opened. Then, we will be looking for changes in the calendar (when the range is selected). Using its view id, we will be disabling the button when needed.

#### Implementation

~~~~~
val customMaterialDatePicker = CustomMaterialDatePicker(
  this, //Current view
  btnOpenCalendar, //View that opens the calendar
  TimeUnit.DAYS.toMillis(7), //Max number of days for the range in milliseconds
  "Title text", //Text for the title
  "Save button", //Text for the button
  Pair("startHeader", "endHeader"), //Text for the headers
  "en-GB" //ISO code for the locale
) 
~~~~~

##### Imlement the desired behaviours

###### Behaviour when a range is selected
~~~~~
override fun selectedBehavior(range: Pair<Long, Long>?) {
  //Update view
}
~~~~~

###### Behaviour when range is exceeded
~~~~~
override fun maxDateRangeBehaviour() {
  //Display warning in the view
}
~~~~~

###### Don't forget to remove the listeners
~~~~~
override fun onDestroy() {
  super.onDestroy()
  customMaterialDatePicker.removeListeners()
}
~~~~~

