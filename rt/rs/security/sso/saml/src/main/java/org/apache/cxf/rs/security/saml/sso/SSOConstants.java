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
operator|.
name|sso
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|WSS4JConstants
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SSOConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SAML_REQUEST
init|=
literal|"SAMLRequest"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SAML_RESPONSE
init|=
literal|"SAMLResponse"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELAY_STATE
init|=
literal|"RelayState"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIG_ALG
init|=
literal|"SigAlg"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE
init|=
literal|"Signature"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_STATE_TIME
init|=
literal|2L
operator|*
literal|60L
operator|*
literal|1000L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSA_SHA1
init|=
name|WSS4JConstants
operator|.
name|RSA_SHA1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DSA_SHA1
init|=
name|WSS4JConstants
operator|.
name|DSA
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SECURITY_CONTEXT_TOKEN
init|=
literal|"org.apache.cxf.websso.context"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RACS_IS_COLLOCATED
init|=
literal|"org.apache.cxf.racs.is.collocated"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SAML2_METADATA_NS
init|=
literal|"urn:oasis:names:tc:SAML:2.0:metadata"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WS_ADDRESSING_NS
init|=
literal|"http://www.w3.org/2005/08/addressing"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMA_INSTANCE_NS
init|=
literal|"http://www.w3.org/2001/XMLSchema-instance"
decl_stmt|;
specifier|private
name|SSOConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

