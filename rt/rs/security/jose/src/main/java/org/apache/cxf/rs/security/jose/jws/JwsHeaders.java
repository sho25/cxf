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
name|jose
operator|.
name|jws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
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
name|jose
operator|.
name|JoseConstants
import|;
end_import

begin_import
import|import
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
name|jose
operator|.
name|JoseHeaders
import|;
end_import

begin_import
import|import
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
name|jose
operator|.
name|JoseType
import|;
end_import

begin_import
import|import
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
name|jose
operator|.
name|jwa
operator|.
name|SignatureAlgorithm
import|;
end_import

begin_class
specifier|public
class|class
name|JwsHeaders
extends|extends
name|JoseHeaders
block|{
specifier|public
name|JwsHeaders
parameter_list|()
block|{     }
specifier|public
name|JwsHeaders
parameter_list|(
name|JoseType
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsHeaders
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
name|super
argument_list|(
name|headers
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|values
parameter_list|)
block|{
name|super
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsHeaders
parameter_list|(
name|SignatureAlgorithm
name|sigAlgo
parameter_list|)
block|{
name|init
argument_list|(
name|sigAlgo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwsHeaders
parameter_list|(
name|JoseType
name|type
parameter_list|,
name|SignatureAlgorithm
name|sigAlgo
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|sigAlgo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|(
name|SignatureAlgorithm
name|sigAlgo
parameter_list|)
block|{
name|setSignatureAlgorithm
argument_list|(
name|sigAlgo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSignatureAlgorithm
parameter_list|(
name|SignatureAlgorithm
name|algo
parameter_list|)
block|{
name|super
operator|.
name|setAlgorithm
argument_list|(
name|algo
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SignatureAlgorithm
name|getSignatureAlgorithm
parameter_list|()
block|{
name|String
name|algo
init|=
name|super
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
return|return
name|algo
operator|==
literal|null
condition|?
literal|null
else|:
name|SignatureAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|algo
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPayloadEncodingStatus
parameter_list|(
name|Boolean
name|status
parameter_list|)
block|{
name|super
operator|.
name|setProperty
argument_list|(
name|JoseConstants
operator|.
name|JWS_HEADER_B64_STATUS_HEADER
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getPayloadEncodingStatus
parameter_list|()
block|{
return|return
name|super
operator|.
name|getBooleanProperty
argument_list|(
name|JoseConstants
operator|.
name|JWS_HEADER_B64_STATUS_HEADER
argument_list|)
return|;
block|}
block|}
end_class

end_unit

