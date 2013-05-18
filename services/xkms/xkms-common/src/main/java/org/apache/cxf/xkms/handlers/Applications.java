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
name|xkms
operator|.
name|handlers
package|;
end_package

begin_enum
specifier|public
enum|enum
name|Applications
block|{
comment|/**      * Certificate Subject Name      */
name|PKIX
argument_list|(
literal|"urn:ietf:rfc:2459"
argument_list|)
block|,
comment|/**      * DNS address of http server      */
name|TLS_HTTPS
argument_list|(
literal|"urn:ietf:rfc:2818"
argument_list|)
block|,
comment|/**      * Service Endpoint Name      */
name|SERVICE_SOAP
argument_list|(
literal|"urn:apache:cxf:service:soap"
argument_list|)
block|,
comment|/**      * Certificate Issuer      */
name|ISSUER
argument_list|(
literal|"urn:x509:issuer"
argument_list|)
block|,
comment|/**      * Certificate Serial Number      */
name|SERIAL
argument_list|(
literal|"urn:x509:serial"
argument_list|)
block|,
comment|/**      * SMTP email address of subject      */
name|PGP
argument_list|(
literal|"urn:ietf:rfc:2440"
argument_list|)
block|;
specifier|private
name|String
name|uri
decl_stmt|;
specifier|private
name|Applications
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|this
operator|.
name|uri
return|;
block|}
block|}
end_enum

end_unit

