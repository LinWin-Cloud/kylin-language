package HttpService;

public class HttpFileContentType {
    private String[] ContentTypeList = {
            "text/html",
            "text/plain",
            "text/xml",

            "image/gif",
            "image/jpeg",
            "image/png",

            "application/xml",
            "application/json",
            "application/pdf",

            "application/msword",
            "application/octet-stream",
            "application/x-www-form-urlencoded",

            "audio/x-wav",
            "audio/x-ms-wma",
            "audio/mp3",

            "video/x-ms-wmv",
            "video/mpeg4",
            "video/avi",

            "video/webm",
            "text/css"
    };
    public String getType(String name) {
        try {
            String last_name = name.substring(name.lastIndexOf(".")).trim();
            if (last_name.equals(".htm") || last_name.equals(".html") || last_name.equals(".php")) {
                return this.ContentTypeList[0];
            }
            if (last_name.equals(".txt")) {
                return this.ContentTypeList[1];
            }
            if (last_name.equals(".xml")) {
                return this.ContentTypeList[6];
            }
            if (last_name.equals(".gif")) {
                return this.ContentTypeList[3];
            }
            if (last_name.equals(".jpeg") || last_name.equals(".jpg")) {
                return this.ContentTypeList[4];
            }
            if (last_name.equals(".png")) {
                return this.ContentTypeList[5];
            }
            if (last_name.equals(".json")) {
                return this.ContentTypeList[7];
            }
            if (last_name.equals(".pdf")) {
                return this.ContentTypeList[8];
            }
            if (last_name.equals(".doc") || last_name.equals(".docx")) {
                return this.ContentTypeList[9];
            }
            if (last_name.equals(".wav")) {
                return this.ContentTypeList[12];
            }
            if (last_name.equals(".w")) {
                return this.ContentTypeList[13];
            }
            if (last_name.equals(".mp3")) {
                return this.ContentTypeList[14];
            }
            if (last_name.equals(".wmv")) {
                return this.ContentTypeList[15];
            }
            if (last_name.equals(".mp4")) {
                return this.ContentTypeList[16];
            }
            if (last_name.equals(".avi")) {
                return this.ContentTypeList[17];
            }
            if (last_name.equals(".webm")) {
                return this.ContentTypeList[18];
            }
            if (last_name.equals(".css")) {
                return this.ContentTypeList[19];
            }
            else {
                return this.ContentTypeList[10];
            }
        }
        catch (Exception exception) {
            return this.ContentTypeList[10];
        }
    }
}
