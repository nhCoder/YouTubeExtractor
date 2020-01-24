package com.naveed.ytextractor.model;
import java.util.List;

public class PlayerResponse {
	private PlayabilityStatus playabilityStatus;
	private StreamingData streamingData;
	private YoutubeMeta videoDetails;
	private Captions captions;

	public void setCaptions(Captions captions) {
		this.captions = captions;
	}

	public Captions getCaptions() {
		return captions;
	}
	public void setPlayabilityStatus(PlayabilityStatus playabilityStatus) {
		this.playabilityStatus = playabilityStatus;
	}

	public PlayabilityStatus getPlayabilityStatus() {
		return playabilityStatus;
	}
	
	public void setStreamingData(StreamingData streamingData) {
		this.streamingData = streamingData;
	}

	public StreamingData getStreamingData() {
		return streamingData;
	}

	public void setVideoDetails(YoutubeMeta videoDetails) {
		this.videoDetails = videoDetails;
	}

	public YoutubeMeta getVideoDetails() {
		return videoDetails;
	}
	
	
	
	

	public class Captions{
		private PlayerCaptionsTracklistRenderer playerCaptionsTracklistRenderer;

		public void setPlayerCaptionsTracklistRenderer(PlayerCaptionsTracklistRenderer playerCaptionsTracklistRenderer) {
			this.playerCaptionsTracklistRenderer = playerCaptionsTracklistRenderer;
		}

		public PlayerCaptionsTracklistRenderer getPlayerCaptionsTracklistRenderer() {
			return playerCaptionsTracklistRenderer;
		}

		
		
		public class PlayerCaptionsTracklistRenderer{
			private List<YTSubtitles> captionTracks;


			
			public void setCaptionTracks(List<YTSubtitles> captionTracks) {
				this.captionTracks = captionTracks;
			}

			public List<YTSubtitles> getCaptionTracks() {
				return captionTracks;
			}
		}
	}


	
	public class PlayabilityStatus{
		private String status;
		private boolean playableInEmbed;

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

		public void setPlayableInEmbed(boolean playableInEmbed) {
			this.playableInEmbed = playableInEmbed;
		}

		public boolean isPlayableInEmbed() {
			return playableInEmbed;
		}
	}
}


