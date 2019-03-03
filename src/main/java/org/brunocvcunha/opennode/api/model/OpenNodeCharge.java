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

import lombok.Getter;

/**
 * Charge Data
 * @author brunovolpato
 *
 */
@Getter
public class OpenNodeCharge {
    /**
     * Charge ID
     */
    private String id;
    
    /**
     * Customer Name
     */
    private String name;
    
    /**
     * Charge Description
     */
    private String description;
    
    /**
     * Charge price in Satoshis
     */
    private double amount;

    /**
     * Charge status
     */
    private OpenNodeChargeStatus status;
    
    /**
     * Charge creation timestamp
     */
    @SerializedName("created_at")
    private long createdAt;
    
    /**
     * Charge fee in satoshis
     */
    private double fee;
    
    /**
     * Charge value at issue time
     */
    @SerializedName("fiat_value")
    private double fiatValue;
    
    /**
     * Charge notes
     */
    private String notes;
    
    
    /**
     * Charge requested instant exchange
     */
    @SerializedName("auto_settle")
    private boolean autoSettle;
    
    /**
     * Lightning Invoice
     */
    @SerializedName("lightning_invoice")
    private OpenNodeLightningInvoice lightningInvoice;
    
    /**
     * Chain Invoice
     */
    @SerializedName("chain_invoice")
    private OpenNodeChainInvoice chainInvoice;
}

