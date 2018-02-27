# Generic Collectible Card Game (GCCG) - React

Proof of concept developing a web/desktop/mobile app from the same Clojure(Script)
codebase. React wrapper is reagent/re-frame.

[GCCG](http://gccg.sourceforge.net/) is an open source project for playing card
games (such as Magic: The Gathering, Lord of the Rings, etc.) written in C++
with SDL as the graphics engine.

## Original Sources

I combined this repo from a bunch of different sources.

* Generic Collectible Card Game: [GCCG](http://gccg.sourceforge.net/)
* Electron for ClojureScript: [cljs-electron](https://github.com/Gonzih/cljs-electron)
* Re-natal for React Native: [re-natal](https://github.com/drapanjanas/re-natal)
* Figwheel Template for web: [figwheel-template](https://github.com/bhauman/figwheel-template)

## Rationale

I chose GCCG because it is an app I used to use whose user base seems to have eroded.
I wanted to create a more modern version that supports more platforms, namely mobile
and web. Also, the UI was not very user friendly, and I thought I could improve upon
that. I hope if I can finish it then it would find more users again.

More broadly, learning programming hasn't changed very much in a long time. We're still
teaching new programmers to write console only programs using tools that are very
disconnected from the actual running program. Also, any apps written are difficult to
publish for other platforms or for sharing with anyone who isn't also a programmer.

If anyone asked me what to learn programming-wise in 2018, I would recommend React. As
an added bonus, REPL driven development in Clojure(Script) brings the developer
as tight a feedback loop and powerful tooling as can be found anywhere.

There are many languages/frameworks that get close to true cross-platform
development, but the combo of ClojureScript + React is my preferred choice. This
project is my attempt at showing how easy and productive it can be to target
every platform.

## Shared Code Breakdown

* All platforms: 181 lines
* Web/Desktop: 140 lines
* Mobile: 214 lines
* Backend: 79 lines
* Platform specific: 311 lines

## Current and Planned Functionality

Right now, you can just browse through the XML data, which is all the historical
Magic: The Gathering cards.

I plan to add these features most likely in this order:

* Search and build decks
* Responsive layout for different screen sizes
* Script parser (GCCG has its own scripting language)
* Play GCCG against a real opponent on a real server
* Add proxy/reimplement server in Clojure (for new features)

## Implementation Notes

Check out [my notes](docs/notes.md) on some interesting things I found during implementation.

## Running it

Each target requires different instructions. Start by cloning this repo.

    git clone https://github.com/baritonehands/gccg-react.git
    cd gccg-react

### Install GCCG

I refer to GCCG locally and update using gccg-core.

```
curl -O http://gccg.sourceforge.net/modules/gccg-core-1.0.10.tgz
mkdir gccg-core
tar xzvf gccg-core-1.0.10.tgz -C gccg-core
```

Update and install Mtg.

```
cd gccg-core

./gccg_package update

./gccg_package install mtg mtg-cards*

cd ..
```

It will take awhile to download and unpack all those images.

If Mtg is too heavyweight for you, you could instead install poker and
modify [common/events.cljs](src/common/gccg/common/events.cljs) with
```:name "poker"```.

### Setup Links

Each platform has a separate folder, so you'll need to link them all to
your local gccg-core directory.

```
# Web
ln -s ../gccg-core/xml resources/xml
ln -s ../../gccg-core/graphics resources/public/graphics

# Electron
ln -s ../gccg-core/xml electron_app/xml
ln -s ../gccg-core/graphics electron_app/graphics

# Android
ln -s ../../../../gccg-core/xml android/app/src/main/assets/xml
ln -s ../../../../gccg-core/graphics android/app/src/main/assets/graphics

# iOS is already linked in the xcode project
```

### Install deps

    yarn install

### Web App

Running this is similar to a standard Figwheel app.

From the console:

    lein figwheel web

From the REPL:

    lein repl

    (start-figwheel "web")

Then open your browser to [http://localhost:3449](http://localhost:3449).

### Electron

Read the instructions for [cljs-electron](https://github.com/Gonzih/cljs-electron) for installation, except it's nested in electron_app.

One time, build the main .js file:

    lein cljsbuild once electron-main

From the console:

    lein figwheel electron-ui

From the REPL:

    lein repl

    (start-figwheel "electron-ui")

Then run electron in another terminal:

    cd electron_app
    electron .

### iOS/Android

Read the instructions in the [re-natal](https://github.com/drapanjanas/re-natal) repository for installation.

Setup your device calling `re-natal` before you can run figwheel.

From the console:

    re-natal use-figwheel

    lein figwheel ios

    # OR

    lein figwheel android

From the REPL:

    lein repl

    (start-figwheel "ios") ; or "android"

Then run react-native cli in another terminal:

    react-native run-ios

    # OR

    react-native run-android

## Releasing

The prod builds have an alias in project.clj:

    lein prod-build

Read the links for each original project above for packaging instructions.
