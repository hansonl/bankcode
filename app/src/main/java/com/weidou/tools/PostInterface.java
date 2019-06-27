package com.weidou.tools;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostInterface {

        @POST("m_app/QryTransferProdListOnMarket.do")
        Observable<ListData> getList(
                @Body RequestBody requestBody
        );


}
