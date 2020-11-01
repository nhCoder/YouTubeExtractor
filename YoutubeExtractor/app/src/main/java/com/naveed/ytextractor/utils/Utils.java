package com.naveed.ytextractor.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import com.naveed.ytextractor.model.YTMedia;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	

	public static String loginCookie="YSC=yNxWJf1cSa4;VISITOR_INFO1_LIVE=sgfw6SW98bw;GPS=1;SID=2wcm6biEhIBQ7mAlI74wOiKiyb_qgHjGqRda45ja9Uo-R_YGPv7BsMw9LxwWxijRYBbl9Q.;__Secure-3PSID=2wcm6biEhIBQ7mAlI74wOiKiyb_qgHjGqRda45ja9Uo-R_YG7NLzd6XbeKd5v4FdcW0fFw.;HSID=AZUFTUl5YDEbXnxXz;SSID=Al9RcjJhYjDs0mH_8;APISID=gno4Jw69CDDSLOoA/Au2BTJQ9vzFfv6jDi;SAPISID=CS66fqRwUvDy_8qr/ASZJ0MtESshD5jMOH;__Secure-3PAPISID=CS66fqRwUvDy_8qr/ASZJ0MtESshD5jMOH;CONSENT=YES+PK.en+202010;LOGIN_INFO=AFmmF2swRQIhAMeOhq-bNVUVUTnNdJnT37v__UYLzOmcDurH2rGtqiqGAiB7hbo1chMtmz5Bn52kKsnRQl6CsWjy8AaWsDOmPQbh1Q:QUQ3MjNmejE5SmExRzFjamk2Wi0xZDJnNW5VU3gwTUVjdjNCYjFGZUo4R2RHeVZfWEFLM2hSWlNXUGpmdzNuemtSUm5MMWJfLVBiMERzemhFQ3ZCMEFjQm1Ud0M4bGpWMmdiTUtYMHpFcmo2WVY2cklBVDJfSWZjMUphMGZQMThCTmZab3J2ZjhYNmNlLXpuRkNSMjZtOXBWRFpraWN0VHEwZTJFLWZVLUF4YmlJb3hrZzVHNktwc05KRUV5aXhJSzRsYzV2N2REVWhW;SIDCC=AJi4QfHl4irM7tcfniTbzgdTmGax9V8uBSUj8uNcPKqSWJJ00Qyl9jSUL1Yhz5WmPl_AmH2XeQ;__Secure-3PSIDCC=AJi4QfFiBQavkEX4ZGQDhud12UroBrzENEaMttnSkCIMUfwlwjtzs5TUWxuVCE-HW2emhh4o";
	//donot manupilate this cookie....its a new account only used for this lib
	public static List<YTMedia> filterInvalidLinks(List<YTMedia> urls) {
		List<YTMedia> workingLinks=new ArrayList<>();
		for (YTMedia media:urls) {
			if (!media.getUrl().contains("&dur=0.0")) {
				workingLinks.add(media);
			}
		}

		return workingLinks;
	}

	public static String extractVideoID(String Url) {
		String r="(?<=(be/|v=))(.*?)(?=(&|\n| |\\z))";
		if (RegexUtils.hasMatch(r, Url)) {
			return RegexUtils.matchGroup(r, Url);
		}
		return Url;
	}

	public static boolean isListContain(List<String> arraylist, String statement) {
		for (String str : arraylist) {
			if (statement != null && statement.toLowerCase().contains(str)) {
				return true;
			}
		}
		return false;
	}

	public static void copyToBoard(String x){
		ClipboardManager clipboard = (ClipboardManager)ContextUtils.context. getSystemService(Context.CLIPBOARD_SERVICE); 
		ClipData clip = ClipData.newPlainText("text", x);
		clipboard.setPrimaryClip(clip);
	}
}
