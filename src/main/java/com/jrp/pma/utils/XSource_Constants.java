package com.jrp.pma.utils;

public class XSource_Constants {
    /**
     * XSource Section
     * define the html tag here
     */
    public static final int XSource_MAX_SCRAP_PAGE = 5;
    public static final int XSource_MAX_SCRAP_DELAY = 30000; // 30000

    public static final String XSource_SEARCH_URI = "<SEARCH_URL>";
    public static final String XSource_WEBSITE_URI = "<WEB_URI>";

    public static final String XSource_UL_CSS_SELECT = "ul#videoSearchResult.videos.search-video-thumbs";
    public static final String XSource_LI_CSS_SELECT = "li.js-pop.videoblock.videoBox";

    // select the img tag
    public static final String XSource_LI_IMG_CSS_SELECT = "img";
    // select the attr inside img tag
    public static final String XSource_LI_IMG_ATTR_CSS_SELECT = "data-thumb_url";

    public static final String XSource_LI_IMG_TITLE_ATTR_CSS_SELECT = "title";

    // select the href element
    public static final String XSource_LI_A_CSS_SELECT = "a.linkVideoThumb.js-linkVideoThumb.img";
    public static final String XSource_LI_A_HREF_CSS_SELECT = "href";
    public static final String XSource_LI_A_DURATION_CSS_SELECT ="div.marker-overlays.js-noFade var.duration";

    public static final String XSource_LI_VIDEO_BlOCK_1_CSS_SELECT = "div.thumbnail-info-wrapper.clearfix";
    public static final String XSource_LI_VIDEO_BlOCK_2_CSS_SELECT = "div.videoUploaderBlock.clearfix div.usernameWrap a";

    public static final String XSource_LI_VIDEO_VIEWS_CSS_SELECT = "div.videoDetailsBlock span.views var";



}
