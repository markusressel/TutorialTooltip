# TutorialTooltip
A simple and easy way to add targeted tutorial messages to your app.

# Work in progress
*This library is still very much a work in progress and not ready to use.*

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

## Create the TutorialTooltip

To create a TutorialTooltip you can use the builder pattern:
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

This is the most basic TutorialTooltip you can create.
You can afterwards show it very easily by calling:

    TutorialTooltip.show(tutorialTooltipBuilder);

## Customization
The first example will show a default indicator and default message so you can test things without getting to much into the details.
Of course this small example is not enough for everyday usage, so let's start with some more advanced ones and get more complex down the road.

### Indicator
The TutorialTooltip library allows you to customize the indicator in a fast and easy way using the builder pattern (again). To customize the look of the indicator use something like this in your ```TutorialTooltipBuilder```:

    .indicator(new IndicatorBuilder()
                .size(100, 100) // size values in pixel
                .build()
    )

There are other builder methods you can use to further customize the look of the indicator. Just have a look at the ```IndicatorBuilder``` class.

A complete example would look something like this:

    final Activity activity = this;

    TutorialTooltipBuilder tutorialTooltipBuilder = new TutorialTooltipBuilder(activity)
        .anchor(new Point(200, 300))
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
        .build();

    TutorialTooltip.show(tutorialTooltipBuilder);

If you don't like the look of the included indicator you can override it completely with a custom view. There are limitations to this tough:
Your CustomView has to
1. extend ```android.view.View```
2. implement the ```TutorialTooltipIndicator``` interface included in this library

This makes it possible to use the IndicatorBuilder even when using a completely self written indicator view which hopefully cleans up the code quite a bit. An example would look like this:

    .indicator(new IndicatorBuilder()
        .customView(new WaveIndicatorView(activity))
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

### Message

# Why?

I needed a better way to create step by step tutorials for my app(s) and even though there were existing libraries to help with this they didn't offer the flexibility I was looking for. I could have taken an existing library and customize it for my needs, but it was just a pain to get through the existing code and I didnt learn much about how to build stuff like this. So I thought to myself - why dont you build it yourself from the ground up? And here I am.

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
