# TutorialTooltip
A simple and easy way to add targeted tutorial messages to your app.

# Work in progress
*This library is still very much a work in progress and not ready to use.*

# Usage

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

# Why?

I needed a better way to create step by step tutorials for my app(s) and even though there were existing libraries to help with this they didn't offer the flexibility I was looking for. I could have taken an existing library and customize it for my needs, but it was just a pain to get through the existing code and I didnt learn much about how to build stuff like this. So I thought to myself - why dont you build it yourself from the ground up?. And here I am.

# Attributions

I want to give a big shoutout to Alessandro Crugnola (sephiroth74) who has built his great android-target-tooltip library (https://github.com/sephiroth74/android-target-tooltip) that adresses the same issue. His work greatly impacted the way I am building this library and really helped me figure out how to things right.


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
