begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Callback interface for SendingOutputStream and SendingWriter  */
end_comment

begin_interface
interface|interface
name|JMSExchangeSender
block|{
comment|/**      * Sends the outMessage of the given exchange with the given payload.      * If the exchange is not oneway a reply should be recieved      * and set as inMessage      *       * @param exchange      * @param payload      */
name|void
name|sendExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|payload
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

