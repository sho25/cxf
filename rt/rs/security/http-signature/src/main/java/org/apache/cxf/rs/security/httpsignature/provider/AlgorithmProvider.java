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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|provider
package|;
end_package

begin_interface
annotation|@
name|FunctionalInterface
specifier|public
interface|interface
name|AlgorithmProvider
block|{
comment|/**      * @param keyId is used as lookup to find the correct configured algorithm name for this keyId      *              The keyId is sent in the message together with the signature      * @throws IllegalArgumentException if it can't provide an algorithm based on keyId      * @return the algorithm name (which is never {@code null})      */
name|String
name|getAlgorithmName
parameter_list|(
name|String
name|keyId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

