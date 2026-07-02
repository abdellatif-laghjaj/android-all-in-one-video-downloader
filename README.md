# ClipSave

![GitHub Downloads](https://img.shields.io/github/downloads/abdellatif-laghjaj/android-all-in-one-video-downloader/total.svg)

**Free, open-source media downloader for Android.** Grab images, video, and audio from
1000+ websites — YouTube, Instagram, TikTok, X/Twitter, Reddit, Facebook, Pinterest,
Twitch, Vimeo, SoundCloud, Bilibili, and many more — straight to
`/storage/emulated/0/Download/ClipSave/`.

No ads. No telemetry. No tracking. MIT licensed.

---

## Features

- **1000+ sites** via a bundled **yt-dlp** engine (with **ffmpeg** merging + **aria2c**).
- **Metadata-scraper fallback** for pure image posts (Open Graph / Twitter cards).
- **Reddit** handled natively through the public JSON API.
- **Share to ClipSave** — share any link from any app to download it.
- **One-tap floating button** (Accessibility) while browsing Instagram / TikTok / X.
- **Audio-only** extraction (m4a) for any supported video.
- **Foreground service** downloads with live progress notifications.
- **Material 3** UI with light / dark / system themes (instant switching).
- **Downloads manager**: search, filter, retry failed, delete, clear.
- Saves through **MediaStore** — no storage permission needed on Android 10+.
- **Shizuku / root status** surfaced in Settings (downloads need neither).

## Build

The app builds on **GitHub Actions** automatically on every push to `main`
(`.github/workflows/android.yml`). It produces:

- `ClipSave-debug` — installable debug APK (always).
- `ClipSave-release-unsigned` — when no signing secrets are set.
- `ClipSave-release-signed` — when keystore secrets are configured.

### Local build

```bash
./gradlew :app:assembleDebug
# output: app/build/outputs/apk/debug/app-debug.apk
```

Requirements: JDK 17, Android SDK 35.

## Tech

- Kotlin 2.1 · Jetpack Compose · Material 3
- AGP 8.8.0 · Gradle 8.10.2 · single-module, **manual DI** (no Hilt/KSP)
- OkHttp · kotlinx.serialization · DataStore
- youtubedl-android (yt-dlp) 0.18.1 · ffmpeg · aria2c
- min SDK 26 (Android 8.0) · target SDK 35 (Android 15)

## License

ClipSave is released under the [MIT License](LICENSE).

## Credits

ClipSave is based on the original [MediaGrab project](https://github.com/omersusin/MediaGrab).

> Personal use. Respect the terms of service and copyright of the platforms you download from.
