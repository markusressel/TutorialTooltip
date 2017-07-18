# TutorialTooltip
A simple and easy way to add targeted tutorial messages to your app.

# Work in progress
*This library is still very much a work in progress and not ready to use.*

# Build Status

| Master       | Beta | Dev               |
|--------------|------|-------------------|
| ![Master](https://travis-ci.org/markusressel/TutorialTooltip.svg?branch=master) | ![Beta](https://travis-ci.org/markusressel/TutorialTooltip.svg?branch=beta) | ![Dev](https://travis-ci.org/markusressel/TutorialTooltip.svg?branch=dev) |

# Why?

I needed a better way to create step by step tutorials for my app(s) and even though there were existing libraries to help with this they didn't offer the flexibility I was looking for. I could have taken an existing library and customize it for my needs, but it was just a pain to get through the existing code and I didnt learn much about how to build stuff like this. So I thought to myself - why dont you build it yourself from the ground up? And here I am.

# Usage

## Gradle
To use this library just include it in your depencencies using

    repositories {
        ...
        maven { url "https://jitpack.io" }
    }

in your project build.gradle file and

    dependencies {
        compile 'com.github.markusressel:TutorialTooltip:v0.9.0'
    }

in your desired module build.gradle file.

## Create a TutorialTooltip

To create a ```TutorialTooltip``` you can use the builder pattern:

##### Using an Anchor View

    TutorialTooltipBuilder tutorialTooltipBuilder =
        new TutorialTooltipBuilder(this)
            .anchor(button1) // anchor view
            .build();

##### Using an Anchor Point

    TutorialTooltipBuilder tutorialTooltipBuilder =
            new TutorialTooltipBuilder(this)
                .anchor(new Point(200, 300)) // anchor point
                .build();

This is the most basic ```TutorialTooltip``` you can create.

## Show a TutorialTooltip

If you used the builder to create your ```TutorialTooltip``` you can afterwards show it very easily by calling:

    înt tutorialTooltipId = TutorialTooltip.show(tutorialTooltipBuilder);

If you used ```TutorialTooltip.make(tutorialTooltipBuilder)``` you can show it using:

    înt tutorialTooltipId = TutorialTooltip.show(tutorialTooltipView);

## Remove a TutorialTooltip

To remove a ```TutorialTooltip``` either hold a reference to its view and call:

    tutorialTooltipView.remove();

on the respective view object.

Or (if you attached it to an activity) you can use a static method and remove it by its ID:

    TutorialTooltip.remove(activity, tutorialTooltipId);

## Customization

The first example will show a default ```TutorialTooltipIndicator``` and default ```TutorialTooltipMessage``` so you can test things without getting to much into the details.
Of course this small example is not enough for everyday usage, so let's start with some more advanced ones and increase complexity down the road.

FYI: In it's current state you can only create and customize TutorialTooltips in code. Styling via theme attributes or xml views may be added at a later stage.

### Message

##### Basic
---

The ```TutorialTooltip``` library allows you to customize the message in a fast and easy way using the builder pattern (again). To customize the look of the message use something like this in your ```TutorialTooltipBuilder```:

    .message(new MessageBuilder()
        .text("This is a tutorial message!")
        .build()
    )

##### Advanced
---

There are other builder methods you can use to further customize the look of the message. Just have a look at the ```MessageBuilder``` class.

A more complex example would look something like this:

    .message(new MessageBuilder()
        .customView(new CardMessageView(activity))
        .text("This is a tutorial message!")
        .textColor(Color.BLACK)
        .backgroundColor(Color.WHITE)
        .gravity(TutorialTooltipView.Gravity.LEFT) // relative to the indicator
        .onClick(new OnMessageClickedListener() {
            @Override
            public void onMessageClicked(int id, TutorialTooltipMessage message, View messageView) {
                TutorialTooltip.remove(activity, id);
            }
        })
        .build()
    )

##### Geek
---

If you don't like the look of the included message you can override it completely with a custom view. To use a custom view as a message you have to make it:

1. extend ```android.view.View``` (at least indirectly like with f.ex. ```LinearLayout```)
2. implement the ```TutorialTooltipMessage``` interface included in this library

This makes it possible to use the ```MessageBuilder``` even when using a completely self written ```TutorialTooltipMessage``` view which hopefully cleans up the code quite a bit.

Just add this line to your  ```MessageBuilder ```:

    .customView(new CardMessageView(activity))

### Indicator

##### Basic
---

The indicator view can be customized in the same way as the message.
Customize the indicator using the ```MessageBuilder``` in your ```TutorialTooltipBuilder``` like so::

    .indicator(new IndicatorBuilder()
        .size(100, 100) // size values in pixel
        .build()
    )

##### Advanced
---

Just like with the message you can further customize the indicator with something similar to this:

    .indicator(new IndicatorBuilder()
        .size(100, 100) // size values in pixel
        .offset(50, 50) // offset values in pixel
        .onClick(new OnIndicatorClickedListener() {
            @Override
            public void onIndicatorClicked(int id, TutorialTooltipIndicator indicator, View indicatorView) {
                TutorialTooltip.remove(activity, id);
            }
        })
        .build()
    )

Have a look at the ```MessageBuilder``` class for a full list of options.

##### Geek
---

If you don't like the look of the included indicator you can override it completely with a custom view. To use a custom view as an indicator you have to make it:

1. extend ```android.view.View``` (at least indirectly like with f.ex. ```LinearLayout```)
2. implement the ```TutorialTooltipIndicator``` interface included in this library

This makes it possible to use the ```IndicatorBuilder``` even when using a completely self written ```TutorialTooltipIndicator``` view which hopefully cleans up the code quite a bit.

Just add this line to your  ```IndicatorBuilder ```:

     .customView(new WaveIndicatorView(activity))

### Bringing it all together

A fully customized TutorialTooltip can then look something like this:

    // custom message view
    CardMessageView cardMessageView = new CardMessageView(getActivity());

    // custom indicator view
    WaveIndicatorView waveIndicatorView = new WaveIndicatorView(getActivity());
    waveIndicatorView.setStrokeWidth(10); // customization that is not included in the IndicatorBuilder

    TutorialTooltipBuilder tutorialTooltipBuilder = new TutorialTooltipBuilder(getActivity())
    .anchor(button)
    .attachToDialog(getDialog())
    .message(new MessageBuilder()
        .customView(cardMessageView)
        .offset(0, 0)
        .text("This is a tutorial message!")
        .textColor(Color.BLACK)
        .backgroundColor(Color.WHITE)
        .gravity(TutorialTooltipView.Gravity.TOP) // relative to the indicator
        .onClick(new OnMessageClickedListener() {
            @Override
            public void onMessageClicked(int id, TutorialTooltipView tutorialTooltipView, TutorialTooltipMessage message, View messageView) {
                // this will intercept touches only for the message view
                // if you don't want the main OnTutorialTooltipClickedListener listener to react to touches here
                // just specify an empty OnMessageClickedListener

                TutorialTooltip.remove(getDialog(), id);
            }
        })
        .size(MessageBuilder.WRAP_CONTENT, MessageBuilder.WRAP_CONTENT)
        .build()
    )
    .indicator(new IndicatorBuilder()
        .customView(waveIndicatorView)
        .offset(0, 0)
        .size(200, 200)
        .color(Color.BLUE)
        .onClick(new OnIndicatorClickedListener() {
            @Override
            public void onIndicatorClicked(int id, TutorialTooltipView tutorialTooltipView, TutorialTooltipIndicator indicator, View indicatorView) {
                // this will intercept touches only for the indicator view
                // if you don't want the main OnTutorialTooltipClickedListener listener to react to touches here
                // just specify an empty OnIndicatorClickedListener

                TutorialTooltip.remove(getDialog(), id);
            }
        })
        .build())
    .onClick(new OnTutorialTooltipClickedListener() {
        @Override
        public void onTutorialTooltipClicked(int id, TutorialTooltipView tutorialTooltipView) {
            // this will intercept touches of the complete window
            // if you don't specify additional listeners for the indicator or
            // message view they will be included

            TutorialTooltip.remove(getDialog(), id);
        }
    })
    .build();

    TutorialTooltip.show(tutorialTooltipBuilder);

## Troubleshooting

### .build()
---

Always remember to finish your builder with the ```.build()``` call. This makes sure you don't change your builder after already using it.
This is necessary for all builders including ```TutorialTooltipBuilder```, ```MessageBuilder``` and ```IndicatorBuilder```.

### Dialogs
---

If the ```TutorialTooltip``` is used in a ```Dialog``` (f.ex. ```DialogFragment```) you have to additionally call:

    .attachToDialog(getDialog())

in the ```TutorialTooltipBuilder```. This will attach the ```TutorialTooltip``` to the ```DecorView``` of the ```Dialog``` instead of the ```Activity```.

### Attach to Window
---

If somehow the ```TutorialTooltip``` is still not rendered above the content you want it to, you can attach it to the ```Window``` instead of the ```Activity``` using:

    .attachToWindow()

This will create a dedicated ```Window``` just for the ```TutorialTooltip``` and (should) always render above other content.
When using this method you can only show one ```TutorialTooltip``` at a time though and onClick handling works a little different, so you should only use this as a last resort.

---
---
---

# Attributions

I want to give a big shoutout to Alessandro Crugnola ([sephiroth74](https://github.com/sephiroth74 "sephiroth74 GitHub Profile")) who has built his great [android-target-tooltip](https://github.com/sephiroth74/android-target-tooltip "android-target-tooltip on GitHub") library that adresses the same issue. His work greatly impacted the way I am building this library and really helped me figure out how to do things right.


# License

    Copyright (c) 2016 Markus Ressel
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
