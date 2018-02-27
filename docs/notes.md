# Implementation Notes

I tried to organize everything to be as reusable as possible.

## All platforms

All the common re-frame events/subs are shared, except image caching
which was mobile only. I used the same namespace for platform specific
implementations of XML and file handling (see (xml|file).wrapper.cljs).

## Web/Desktop

The html/css/cljs here was almost 100% reusable. The only platform specific code
was reading the file system vs. AJAX requests to fetch xml data. Also,
electron's main process was its own code.

I did some performance testing of clojure.xml.data vs tubax.core and
found clojure.xml.data to be faster (using native DOMParser in JS) for
most of the XML files I was reading.

## Mobile

I separated Android/iOS root component mostly to use the different
components for root navigation. I also needed to use react-native-fs
in order to read the huge volume of XML/images, because the react-native
shared image folder timed out on compilation.

I also added an image caching layer which dynamically loads the visible
images on scroll, to avoid loading about 1GB of data into memory at once.

One cool thing CLJS could do was dynamically reading the filesystem on
iOS, which helped debug where and how to integrate the files into XCode.

For XML, DOMParser is not available in react-native, so I had to use the
tubax.core/sax-js implementation. This avoided needing a platform specific
native xml lib for iOS/Android.

