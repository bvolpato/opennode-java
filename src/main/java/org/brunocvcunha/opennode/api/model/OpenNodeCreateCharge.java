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
package org.brunocvcunha.opennode.api.model;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

/**
 * Charge Data
 * @author brunovolpato
 *
 */
@Data
@Builder
public class OpenNodeCreateCharge {

    private String description;
    
    /**
     * Charge's price in satoshis, unless currency parameter is used.
     */
    private long amount;

    private OpenNodeCurrency currency;
    
    /**
     * External order ID (use your platform ID)
     */
    @SerializedName("order_id")
    private String orderId;
    
    @SerializedName("customer_email")
    private String customerEmail;
    
    @SerializedName("customer_name")
    private String customerName;
    
    @SerializedName("fiat_value")
    private double fiatValue;
    
    @SerializedName("callback_url")
    private String callbackUrl;
    
    @SerializedName("success_url")
    private String successUrl;
    
    @SerializedName("auto_settle")
    private boolean autoSettle;
}
