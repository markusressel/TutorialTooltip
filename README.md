# TutorialTooltip [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
A simple and easy way to add targeted tutorial messages to your app.

![TurorialTooltip Demo](demo.gif)

# Build Status

| Master       |
|--------------|
| [![Master](https://travis-ci.org/markusressel/TutorialTooltip.svg?branch=master)](https://travis-ci.org/markusressel/TutorialTooltip/branches) |
| [![codebeat badge](https://codebeat.co/badges/edc7ad8f-912b-4784-80eb-40d5cc7eec98)](https://codebeat.co/projects/github-com-markusressel-tutorialtooltip-master) |

# Why?

I needed a better way to create step by step tutorials for my app(s) and, even though there were existing libraries to help with this, they didn't offer the flexibility I was looking for. I could have taken an existing library and customize it for my needs, but it was just a pain to get through the existing code and I didnt learn much about how to build stuff like this. So I thought to myself - why dont you build it yourself from the ground up? And here I am.

# Usage

## Gradle
To use this library just include it in your depencencies using

    repositories {
        ...
        maven { url "https://jitpack.io" }
    }

in your project build.gradle file and

    dependencies {
        compile 'com.github.markusressel:TutorialTooltip:v2.0.1'
    }

in your desired module build.gradle file.

## Create a TutorialTooltip

To create a `TutorialTooltip` use the `TutorialTooltipBuilder`:

##### Using an Anchor View

```kotlin
val tutorialTooltipBuilder = TutorialTooltipBuilder(this)
    .anchor(button1, TutorialTooltipView.Gravity.CENTER)
    .build()
```

##### Using an Anchor Point

```kotlin
val tutorialTooltipBuilder = TutorialTooltipBuilder(this)
    .anchor(Point(200, 300))
    .build()
```

This is the most basic `TutorialTooltip` you can create.

## Show a TutorialTooltip

After you used the builder to create your `TutorialTooltip` you can show it very easily by calling:

```kotlin
val tutorialTooltipId = TutorialTooltip.show(tutorialTooltipBuilder)
```

If you used `TutorialTooltip.make(tutorialTooltipBuilder)` you can show it using:

```kotlin
val tutorialTooltipId = TutorialTooltip.show(tutorialTooltipView)
```

## Remove a TutorialTooltip

To remove a `TutorialTooltip` either hold a reference to its view and call:

```kotlin
tutorialTooltipView.remove()
```

on the respective view object.

Or (if you attached it to an activity) you can use a static method and remove it by its ID:

```kotlin
TutorialTooltip.remove(activity, tutorialTooltipId)
```

## Customization

The first example will show a default `TutorialTooltipIndicator` and default `TutorialTooltipMessage` 
so you can test things without getting to much into the details. Of course this small example 
is not enough for everyday usage, so let's start with some more advanced ones.

FYI: In it's current state you can only create and customize TutorialTooltips in code. 
Styling via theme attributes or xml views may be added at a later stage.

### Message

##### Basic
---

The `TutorialTooltip` library allows you to customize the message using the `MessageConfiguration`. 
To customize the look of the message use something like this in your `TutorialTooltipBuilder`:

```kotlin
messageConfiguration = MessageConfiguration(
    text = "This is a tutorial message!"
)
```

##### Advanced
---

There are other properties you can use to further customize the look of the message.
Have a look at the `MessageConfiguration` class to see what is available.

A more complex example would look something like this:

```kotlin
messageConfiguration = MessageConfiguration(
    customView = CardMessageView(activity),
    text = "This is a tutorial message!",
    textColor = Color.BLACK,
    backgroundColor = Color.WHITE,
    gravity = TutorialTooltipView.Gravity.LEFT, // relative to the indicator
    onClick = { id, tutorialTooltipView, message, messageView ->
        tutorialTooltipView.remove(true)
    }
)
```

##### Geek
---

If you don't like the look of the included message you can override it completely with a 
custom view. To use a custom view as a message you have to make it:

1. extend `android.view.View` (at least indirectly like with f.ex. `LinearLayout`)
2. implement the `TutorialTooltipMessage` interface included in this library

This makes it possible to use the `MessageConfiguration` even when using a completely self written 
`TutorialTooltipMessage` view.

Just add this line to your `MessageConfiguration`:

```kotlin
    customView = CardMessageView(activity)
```

### Indicator

##### Basic
---

The indicator view can be customized in the same way as the message.
Customize the indicator using the `IndicatorConfiguration` in your `TutorialTooltipBuilder` like so:

```kotlin
    indicatorConfiguration = IndicatorConfiguration(
        width = 100, // size values in pixel
        height = 100 // size values in pixel
    )
```

##### Advanced
---

Just like with the message you can further customize the indicator with something similar to this:

```kotlin
    indicatorConfiguration = IndicatorConfiguration(
        width = 100, // size values in pixel
        height = 100, // size values in pixel
        offsetX = 50, // offset values in pixel
        offsetY = 50, // offset values in pixel
        onClick = { id, tutorialTooltipView, indicator, indicatorView ->
            TutorialTooltip.remove(activity, id)
        })
```

Have a look at the `IndicatorConfiguration` class for a full list of options.

##### Geek
---

If you don't like the look of the included indicator you can override it completely with a custom view. 
To use a custom view as an indicator you have to make it:

1. extend `android.view.View` (at least indirectly like with f.ex. `LinearLayout`)
2. implement the `TutorialTooltipIndicator` interface included in this library

This makes it possible to use the `IndicatorConfiguration` even when using a completely self written 
`TutorialTooltipIndicator` view.

Just add this line to your  `IndicatorConfiguration`:

```kotlin
    customView = WaveIndicatorView(activity)
```

### Bringing it all together

A fully customized TutorialTooltip can then look something like this:

```kotlin
// custom message view
val cardMessageView = CardMessageView(this)

// custom indicator view
val waveIndicatorView = WaveIndicatorView(this)
waveIndicatorView.strokeWidth = 10F; // customization that is not included in the IndicatorBuilder

val tutorialTooltipBuilder = TutorialTooltipBuilder(
        context = this,
        indicatorConfiguration = IndicatorConfiguration(
                customView = waveIndicatorView,
                width = 200,
                height = 200,
                offsetX = 0,
                offsetY = 0,
                color = Color.BLUE,
                onClick = { id, tutorialTooltipView, indicator, indicatorView ->
                    // This will intercept touches only for the indicator view.
                    // If you don't want the main OnTutorialTooltipClickedListener listener to react to touches here
                    // just specify an empty OnIndicatorClickedListener
                    tutorialTooltipView.remove(true)
                }),
        messageConfiguration = MessageConfiguration(
                customView = cardMessageView,
                width = MessageConfiguration.WRAP_CONTENT,
                height = MessageConfiguration.WRAP_CONTENT,
                offsetX = 0,
                offsetY = 0,
                text = "This is a tutorial message!",
                textColor = Color.BLACK,
                backgroundColor = Color.WHITE,
                gravity = TutorialTooltipView.Gravity.TOP, // relative to the indicator
                onClick = { id, tutorialTooltipView, message, messageView ->
                    // this will intercept touches only for the message view
                    // if you don't want the main OnTutorialTooltipClickedListener listener to react to touches here
                    // just specify an empty OnMessageClickedListener

                    tutorialTooltipView.remove(true)
                }))
        .build()

TutorialTooltip.show(tutorialTooltipBuilder)
```

## Troubleshooting

### .build()
---

Always remember to finish your builder with the `.build()` call. This makes sure you don't change your builder after already using it.

### Dialogs
---

If the `TutorialTooltip` is used in a `Dialog` (f.ex. `DialogFragment`) you have to additionally call:

```kotlin
    .attachToDialog(getDialog())
```

in the `TutorialTooltipBuilder`. This will attach the `TutorialTooltip` to the `DecorView` 
of the `Dialog` instead of the `Activity`.

### Attach to Window
---

If somehow the `TutorialTooltip` is still not rendered above the content you want it to, you can 
attach it to the `Window` instead of the `Activity` using:

```kotlin
    .attachToWindow()
```

This will create a dedicated `Window` just for the `TutorialTooltip` and (should) always 
render above other content. When using this method you can only show one `TutorialTooltip` at a time 
though and onClick handling works a little different, so you should only use this as a last resort.

# Attributions

I want to give a big shoutout to Alessandro Crugnola ([sephiroth74](https://github.com/sephiroth74 "sephiroth74 GitHub Profile")) 
who has built his great [android-target-tooltip](https://github.com/sephiroth74/android-target-tooltip "android-target-tooltip on GitHub") 
library that addresses the same issue. His work greatly impacted the way I am building this 
library and really helped me figure out how to do things right.


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
