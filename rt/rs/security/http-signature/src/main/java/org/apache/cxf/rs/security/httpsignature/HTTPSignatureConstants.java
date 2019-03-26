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
name|rt
operator|.
name|security
operator|.
name|rs
operator|.
name|RSSecurityConstants
import|;
end_import

begin_comment
comment|/**  * Some security constants to be used with HTTP Signature.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|HTTPSignatureConstants
extends|extends
name|RSSecurityConstants
block|{
comment|/**      * The signature key id. This is a required configuration option.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_HTTP_SIGNATURE_KEY_ID
init|=
literal|"rs.security.http.signature.key.id"
decl_stmt|;
comment|/**      * This is a list of String values which correspond to the list of HTTP headers that will be signed      * in the outbound request. The default is to sign all message headers. In addition, by default a client      * will include "(request-target)" in the signed headers list.      */
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_HTTP_SIGNATURE_OUT_HEADERS
init|=
literal|"rs.security.http.signature.out.headers"
decl_stmt|;
specifier|private
name|HTTPSignatureConstants
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

