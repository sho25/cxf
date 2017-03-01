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
name|saml
package|;
end_package

begin_comment
comment|/**  * Some constant configuration options  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SAMLConstants
block|{
comment|/**      * This tag refers to a DOM Element representation of a SAML Token. If a SAML Token      * is stored on the Message Context, then the SamlFormOutInterceptor and      * SamlHeaderOutInterceptor will use this token instead of creating a new SAML Token.      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML_TOKEN_ELEMENT
init|=
literal|"rs-security.saml.token.element"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WS_SAML_TOKEN_ELEMENT
init|=
literal|"ws-security.token.element"
decl_stmt|;
specifier|private
name|SAMLConstants
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

