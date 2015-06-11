package de.rentoudu.mensa.model;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Menu implements Serializable {
	
	public String title;
	public String mainCourse;
	public String data;
    public String type;

	public Menu() {}

    public Menu(String title, String html, String type) {
        //Set type
        this.setType(type);

        //Find title (between first <b> and last </b>
        int start = html.indexOf("<b>") + "<b>".length();
        int end = html.lastIndexOf("</b>");

        //Search title and set (there might be no title)
        try {
            if(start > 0 && end > 0) {
                String mainCourse = this.optimizeString(html.substring(start, end));
                this.setMainCourse(mainCourse);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Set title
        this.setTitle(title);

        //Set rest as data
        try {
            this.setData(this.optimizeString(html));
        } catch(Exception e) {
            e.printStackTrace();

        }
    }
	
	public String getId() {
        //Cancel if main course is null
        if(this.mainCourse == null) {
            return "";
        }

        //Replace all German HTML entities with their UTF-8 signs
        //This is necessary as with the old system these umlauts were
        //not HTML encoded and the ratings are stored under those unescaped titles
		String id = this.mainCourse;
        id = id.replace("&ouml;", "\u00F6");
        id = id.replace("&auml;", "\u00E4");
        id = id.replace("&uuml;", "\u00FC");
        id = id.replace("&Ouml;", "\u00D6");
        id = id.replace("&Auml;", "\u00C4");
        id = id.replace("&Uuml;", "\u00DC");
        return id.replace("&szlig;", "\u00DF");
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMainCourse(String title) {
		this.mainCourse = title;
	}

	public Spanned getTitle() {
		return this.title != null ? Html.fromHtml(this.title) : null;
	}

    public Spanned getData() {
        return this.data != null ? Html.fromHtml(this.data) : null;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * Optimizes the given String by deleting unrelevant characters or replacing them by better ones.
     * @param s The String object that is going to be optimized
     * @return
     */
    private String optimizeString(String s) {

        //Remove all spaces at the end
        s = s.replaceAll("( )*$", "");

        //Remove all spaces at the beginning
        s = s.replaceAll("^( )*", "");

        //Remove all new lines and tabs
        s = s.replace("\n", "");
        s = s.replace("\r", "");
        s = s.replace("\t", "");

        //Remove underlining
        s = s.replaceAll("<u>", "");
        s = s.replaceAll("</u>", "");

        //Replace multiple spaces in a row with a single on
        s = s.replaceAll(" +", " ");

        //Remove boldness
        s = s.replaceAll("<b>", "");
        s = s.replaceAll("</b>", "");

        //Remove <br> tags at the end of the text
        s = s.replaceAll("(<br( )*(/){0,1}( )*/>)*$", "");

        //Remove <br> tags at the start of the text
        s = s.replaceAll("^(<br( )*(/){0,1}( )*/>)*", "");

        if(s.indexOf("<span class=\"zusatzsstoffe\"") >= 0) {
            int start = s.indexOf("<span class=\"zusatzsstoffe\"");
            int end = s.indexOf("</span>") + "</span>".length();
            String span = s.substring(start, end);

            int start2 = span.indexOf(": ") + ": ".length();
            int end2 = span.indexOf("</span>");
            span = span.substring(start2, end2);
            span = span.replace(",", ", ");
            span = "<br />(" + span + ")";

            s = s.substring(0, start) + span + s.substring(end);

        }

        return s;
    }
}
