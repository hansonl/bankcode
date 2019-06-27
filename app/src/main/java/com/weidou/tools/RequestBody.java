package com.weidou.tools;

import java.util.List;

public class RequestBody {

    /**
     * request : {"header":{"appId":"","appVersion":"4.4","device":{"osType":"03","osVersion":"","uuid":""}},"body":{"pageSize":10,"currentIndex":0,"pageNo":1,"orderFlag":"3","currType":[],"miniAmt":[],"prdType":[],"timeLimit":[]}}
     */

    public RequestBean request;

    public static class RequestBean {
        /**
         * header : {"appId":"","appVersion":"4.4","device":{"osType":"03","osVersion":"","uuid":""}}
         * body : {"pageSize":10,"currentIndex":0,"pageNo":1,"orderFlag":"3","currType":[],"miniAmt":[],"prdType":[],"timeLimit":[]}
         */

        public HeaderBean header;
        public BodyBean body;

        public static class HeaderBean {
            /**
             * appId :
             * appVersion : 4.4
             * device : {"osType":"03","osVersion":"","uuid":""}
             */

            public String appId;
            public String appVersion;
            public DeviceBean device;

            public static class DeviceBean {
                /**
                 * osType : 03
                 * osVersion :
                 * uuid :
                 */

                public String osType;
                public String osVersion;
                public String uuid;
            }
        }

        public static class BodyBean {
            /**
             * pageSize : 10
             * currentIndex : 0
             * pageNo : 1
             * orderFlag : 3
             * currType : []
             * miniAmt : []
             * prdType : []
             * timeLimit : []
             */

            public int pageSize;
            public int currentIndex;
            public int pageNo;
            public String orderFlag;
            public List<?> currType;
            public List<?> miniAmt;
            public List<?> prdType;
            public List<?> timeLimit;
        }
    }
}
