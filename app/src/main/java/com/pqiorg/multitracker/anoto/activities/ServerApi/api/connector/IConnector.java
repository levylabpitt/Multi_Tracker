package com.pqiorg.multitracker.anoto.activities.ServerApi.api.connector;

//import com.anoto.adna.ServerApi.api.response.IResponseEvent;

import com.pqiorg.multitracker.anoto.activities.ServerApi.api.response.IResponseEvent;

public interface IConnector {
    void addEusrPtrnPage(String str, String str2, IResponseEvent<Object> iResponseEvent);

    void contentAccess(String str, IResponseEvent<Object> iResponseEvent);

    void contentLog(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, IResponseEvent<Object> iResponseEvent);

    void deleteContentAccess(String str, String str2, IResponseEvent<Object> iResponseEvent);

    void eusrLogin(String str, String str2, IResponseEvent<Object> iResponseEvent);

    void eusrPtrnPage(String str, IResponseEvent<Object> iResponseEvent);

    void eusrPtrnPageArea(String str, String str2, IResponseEvent<Object> iResponseEvent);

    void event_schedule(String str, IResponseEvent<Object> iResponseEvent);

    void event_schedule_other_server(String str, IResponseEvent<Object> iResponseEvent);

    void getScanCoordinate(String str, String str2, String str3, String str4, String str5, String str6, IResponseEvent<Object> iResponseEvent);

    void namecard(String str, IResponseEvent<Object> iResponseEvent);

    void namecard_other_server(String str, IResponseEvent<Object> iResponseEvent);

    void scan(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, IResponseEvent<Object> iResponseEvent);

    void scanPageArea(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, IResponseEvent<Object> iResponseEvent);

    void sendTapeRegister(String str, String str2, String str3, String str4, String str5, String str6, IResponseEvent<Object> iResponseEvent);

    void signupUserEmail(String str, String str2, String str3, IResponseEvent<Object> iResponseEvent);

    void updEusrPtrnPageArea(String str, String str2, String str3, String str4, String str5, String str6, String str7, IResponseEvent<Object> iResponseEvent);

    void version(IResponseEvent<Object> iResponseEvent);
}
