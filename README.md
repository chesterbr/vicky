# Vicky

This hackday project was a chatbot for closed mobile chat platforms. It used Android UI testing support library to scrape questions and write answers on the app running on a little phone.

For added fun, we ran it on a Raspberry Pi and mounted it all on a cardboard box, making it an autonomous "bot" of sorts:

![The project as it ran on that hackday](/cardboard_vicky.jpg?raw=true "The project as it ran on that hackday")

The code was built specifically for some 2014 version of [Kik](https://www.kik.com/) and is unlikely to run with recent builds (or to comply with their TOS), but the genereral idea was interesting enough to be preserved.
 
### Authors

[@chesterbr](http://github.com/chesterbr), [@dterror](http://github.com/dterror) and [@josedonizetti](http://github.com/josedonizetti).

### Hardware used

- Raspberry Pi (+WiFi module)
- Android (>= 4.1) device

### Overall idea

- Run the bot engine on a server (in our case it was a modified version of [Hubot](https://hubot.github.com/), connected to a chat system like Hipchat or Slack)
- Use Google's ui manipulation tool to kickstart kik on a device/emulator, talk to the bot server and respond back.

### Build instructions

#### Pre-reqs:

On your computer:

- Java SDK properly installed and configured. Tested with ([Oracle's JDK 1.7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html), but anything capable of `javac` >= v1.6 should do)
- [Apache Ant](http://ant.apache.org/) (`brew install ant` was fine)
- [ADT bundle](http://developer.android.com/sdk/index.html) properly installed (specifically, we need to run `adb` properly)
- In this repository's `local.properties`, point `sdk.dir` to the `sdk` folder inside your ADT bundle

On the Android (>= 4.1) device

- Install [Kik](http://kik.com)
- Register/login the user to be impersonated.

#### Running locally

- (optional) Edit `LaunchSettings.java` (specifically, `getResponse` method) to change Vicky's behaviour whenever it receives a message (currently, it connects to a Hubot instance to respond to messages, that is, Vicky *is* Hubot).
- `./run.sh` will:
  - use Ant to build `vicky.jar` with the behaviour above
  - install it on the phone
  - close Kik client on the if running
  - run [uiautomator](http://developer.android.com/tools/help/uiautomator/index.html)

#### Running on a Raspberry Pi

- Install a recent Raspbian with standard, boot-to-command line options & some interwebz
- Download [adb for ARM](http://forum.xda-developers.com/showthread.php?t=1924492) (specifically [this file](http://forum.xda-developers.com/attachment.php?attachmentid=1392336&d=1349930509)); copy `adb` to `/usr/local/bin`
- Install a compatible `node/npm` (the one on Raspbian's default repo is too ancient, bulding from source takes forever). [This](https://gist.github.com/adammw/3245130/raw/v0.10.24/node-v0.10.24-linux-arm-armv6j-vfp-hard.tar.gz) 0.10.24 prebuilt worked fine (just copied the tree to /usr/local/)
- Download a copy of [QBert](http://github.com/uken/hubot) (*TODO*: add additional configs here) and run it
- Build `vicky.jar` on your computer (see above), then copy `run.sh` and the bin directory (which should contain `vicky.jar`) to a directory on the device
- On the device, comment first line of run.sh (you already have `vicky.jar`)


#### Legal info

This software is licensed for educational purposes under the terms of the MIT license. See LICENSE for details.

The software or its authors do not represent any of the companies mentioned, and publish it in good faith of its potential usefulness as a learning tool for development.
