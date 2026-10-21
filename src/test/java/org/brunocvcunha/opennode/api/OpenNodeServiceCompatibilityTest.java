/**
 * Copyright (C) 2017 Bruno Candido Volpato da Cunha (brunocvcunha@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brunocvcunha.opennode.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.brunocvcunha.opennode.api.model.OpenNodeCharge;
import org.brunocvcunha.opennode.api.model.OpenNodeChargeStatus;
import org.brunocvcunha.opennode.api.model.OpenNodeCreateCharge;
import org.brunocvcunha.opennode.api.model.OpenNodeCurrency;
import org.brunocvcunha.opennode.api.model.OpenNodeResponse;
import org.junit.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenNodeServiceCompatibilityTest {
    @Test
    public void factorySerializesChargeWithoutSendingARequest() throws IOException {
        OpenNodeCreateCharge charge = OpenNodeCreateCharge.builder()
                .orderId("synthetic-order")
                .amount(42)
                .currency(OpenNodeCurrency.USD)
                .autoSettle(true)
                .build();
        Request request = OpenNodeServiceFactory.buildClient("synthetic-token")
                .createCharge(charge).request();
        assertEquals("https://api.opennode.com/v1/charges", request.url().toString());
        assertEquals("POST", request.method());
        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        JsonObject body = new Gson().fromJson(buffer.readUtf8(), JsonObject.class);
        assertEquals("synthetic-order", body.get("order_id").getAsString());
        assertEquals(42, body.get("amount").getAsDouble(), 0);
        assertEquals("USD", body.get("currency").getAsString());
        assertEquals(true, body.get("auto_settle").getAsBoolean());
    }

    @Test
    public void authenticatedTransportDeserializesGenericDataAndPreservesHttpErrors()
            throws IOException {
        AtomicInteger requests = new AtomicInteger();
        OkHttpClient client = OpenNodeServiceFactory.createClientInterceptor("synthetic-token")
                .newBuilder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    assertEquals("synthetic-token", request.header("Authorization"));
                    int index = requests.getAndIncrement();
                    assertEquals(index == 0 ? "/v1/charges" : "/v1/charge/missing",
                            request.url().encodedPath());
                    String json = index == 0
                            ? "{\"data\":[{\"id\":\"synthetic-charge\",\"status\":\"paid\",\"created_at\":123}]}"
                            : "{\"message\":\"not found\"}";
                    return new okhttp3.Response.Builder()
                            .request(request)
                            .protocol(Protocol.HTTP_1_1)
                            .code(index == 0 ? 200 : 404)
                            .message(index == 0 ? "OK" : "Not Found")
                            .body(ResponseBody.create(json, MediaType.get("application/json")))
                            .build();
                })
                .build();
        OpenNodeService service = new Retrofit.Builder()
                .baseUrl("https://example.invalid/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenNodeService.class);
        Response<OpenNodeResponse<List<OpenNodeCharge>>> response = service.listCharges().execute();
        OpenNodeCharge charge = response.body().getData().get(0);
        assertEquals("synthetic-charge", charge.getId());
        assertEquals(OpenNodeChargeStatus.PAID, charge.getStatus());
        assertEquals(123, charge.getCreatedAt());
        Response<OpenNodeResponse<OpenNodeCharge>> missing = service.getCharge("missing").execute();
        assertFalse(missing.isSuccessful());
        assertEquals(404, missing.code());
        assertNotNull(missing.errorBody());
        missing.errorBody().close();
        assertEquals(2, requests.get());
    }
}
