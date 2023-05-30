package com.fraporitmos


data class Video(
  val description: String,
  val url: String,
  val subtitle: String,
  val thumb: String,
  val title: String,
  val width: Int,
  val height: Int
)

val videos = listOf(
  Video(
    description = "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\n\nLicensed under the Creative Commons Attribution license\nhttp://www.bigbuckbunny.org",
    url = "https://res.cloudinary.com/frapoteam/video/upload/v1683606908/video_ukueyo.mp4",
    subtitle = "By Blender Foundation",
    thumb = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
    title = "Big Buck Bunny",
    width = 1500,
    height = 1800
  ),
  Video(
    description = "The first Blender Open Movie from 2006",
    url = "https://res.cloudinary.com/frapoteam/video/upload/v1683855338/ssstik.io_1683855316118_jsfp9t.mp4",
    subtitle = "By Blender Foundation",
    thumb = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
    title = "Elephant Dream",
    width = 1500,
    height = 1500
  ),
  Video(
    description = "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes. For $35.\nLearn how to use Chromecast with HBO GO and more at google.com/chromecast.",
    url = "https://res.cloudinary.com/frapoteam/video/upload/v1683855345/ssstik.io_1683855340318_ruqcmq.mp4",
    subtitle = "By Google",
    thumb = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
    title = "For Bigger Blazes",
    width = 1500,
    height = 1500
  ),
  Video(
    description = "Introducing Chromecast. The easiest way to enjoy online video and music on your TV—for when Batman's escapes aren't quite big enough. For $35. Learn how to use Chromecast with Google Play Movies and more at google.com/chromecast.",
    url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
    subtitle = "By Google",
    thumb = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
    title = "For Bigger Escape",
    width = 1200,
    height = 1500
  ),
)
