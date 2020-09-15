package com.jrp.pma.model;

import com.jrp.pma.utils.XSource_Constants;
import org.jsoup.nodes.Element;

public class XSource_Content {

    private String title;
    private String imgUrl;
    private String videoUrl;
    private String userUrl;
    private String duration;
    private String viewsCount;

    private XSource_Content(XSource_Content_Builder builder) {
        this.title = builder.title;
        this.imgUrl = builder.imgUrl;
        this.videoUrl = builder.videoUrl;
        this.userUrl = builder.userUrl;
        this.duration = builder.duration;
        this.viewsCount = builder.viewsCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }

    public static class XSource_Content_Builder{

        private String title;
        private String imgUrl;
        private String videoUrl;
        private String userUrl;
        private String duration;
        private String viewsCount;
        private Element contentLIBlock;

        public XSource_Content_Builder(Element contentLIBlock) {
            this.contentLIBlock = contentLIBlock;
        }

        public XSource_Content build() throws Exception {

            // extract title name
            Element contentTitle = this.contentLIBlock.select(XSource_Constants.XSource_LI_IMG_CSS_SELECT).first();
            if (contentTitle != null) {
                title = contentTitle.attr(XSource_Constants.XSource_LI_IMG_TITLE_ATTR_CSS_SELECT);
            } else {
                title = null;
            }


            // extract image url
            Element contentImgUrl = this.contentLIBlock.select(XSource_Constants.XSource_LI_IMG_CSS_SELECT).first();
            if (contentImgUrl != null) {
                imgUrl = contentImgUrl.attr(XSource_Constants.XSource_LI_IMG_ATTR_CSS_SELECT);
            } else {
                imgUrl = null;
            }

            // extract duration
            Element contentDuration = this.contentLIBlock.select(XSource_Constants.XSource_LI_A_DURATION_CSS_SELECT).first();
            if (contentDuration != null) {
                duration = contentDuration.text();

            } else {
                duration = null;
            }

            // extract user poster name
            Element contentUserName = this.contentLIBlock.select(XSource_Constants.XSource_LI_VIDEO_BlOCK_1_CSS_SELECT).first()
                    .select(XSource_Constants.XSource_LI_VIDEO_BlOCK_2_CSS_SELECT).first();
            if (contentUserName != null) {
                userUrl = contentUserName.text();
                //System.out.println(contentUserName.text());
            } else {
                userUrl = null;
            }

            // extract video url
            Element contentVideoUrl = this.contentLIBlock.select(XSource_Constants.XSource_LI_A_CSS_SELECT).first();
            if (contentVideoUrl != null) {
                videoUrl = XSource_Constants.XSource_WEBSITE_URI + contentVideoUrl.attr(XSource_Constants.XSource_LI_A_HREF_CSS_SELECT);
            } else {
                videoUrl = null;
            }

            // extract views count
            Element contentViews = this.contentLIBlock.select(XSource_Constants.XSource_LI_VIDEO_BlOCK_1_CSS_SELECT).first()
                    .select(XSource_Constants.XSource_LI_VIDEO_VIEWS_CSS_SELECT).first();
            if (contentViews != null) {
                try {
                    //TODO : later change back to integer
                   // viewsCount = Integer.valueOf(contentViews.text());
                    viewsCount = contentViews.text();
                }catch(Exception e){
                    throw new Exception(e.getMessage());
                }
            } else {
                viewsCount = null;
            }


            return new XSource_Content(this);
        }
    }

    @Override
    public String toString() {
        return "XSource_Content{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", userUrl='" + userUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", viewsCount='" + viewsCount + '\'' +
                '}';
    }
}
