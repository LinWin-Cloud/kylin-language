package HttpService;

public class HttpFileContentType {
    private String[] ContentTypeList = {
            "text/html",                                    //0
            "text/plain",                                   //1
            "text/xml",                                     //2

            "image/gif",                                    //3
            "image/jpeg",                                   //4
            "image/png",                                    //5

            "application/xml",                              //6
            "application/json",                             //7
            "application/pdf",                              //8

            "application/msword",                           //9
            "application/octet-stream",                     //10
            "application/x-www-form-urlencoded",            //11

            "audio/x-wav",                                  //12
            "audio/x-ms-wma",                               //13
            "audio/mp3",                                    //14

            "video/x-ms-wmv",                               //15
            "video/mpeg4",                                  //16
            "video/avi",                                    //17

            "video/webm",                                   //18
            "text/css"                                      //19
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
