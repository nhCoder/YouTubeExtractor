YouTubeExtractor

A Youtube urls extractor for java & android for streaming and downloading purpose.

I made this on my Android Device using AIDE(IDE), So i cant update gradle, so dont ask me

Test Apk

Features • Extracts Muxed and Adaptive urls separately • Extracts Signature Protected Videos(like vevo) • Extracts Live Videos Urls(hls) • Extracts video info(title,author,description,view,etc) • Extracts Age restricted videos (Uses Cookie from a Google account)

Usage

Copy the classes or compile project.

.jar will be available soon

Dependencies Used • Gson • Mozilla Rhino • UniversalVideoView(Used only for video testing)

Example

new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner(){ Override public void onExtractionDone(List adativeStream, final List muxedStream, YoutubeMeta meta) {

     }
   @Override
   public void onExtractionGoesWrong(final ExtractorException e) 
     {         
     }
     }).Extract(YOUTUBE_ID/LINK); 



For age restricted Videos

For extraction of age restricted videos use useDefaultLogin() to Use default cookie.. OR YOU CAN override with your own cookies by method setHeaders